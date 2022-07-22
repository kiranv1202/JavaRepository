package com.info;

import com.info.serde.AppSerdes;
import com.info.types.PaymentConfirmation;
import com.info.types.PaymentRequest;
import com.info.types.TransactionStatus;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Duration;
import java.util.Properties;


public class KStreamJoinDemo {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(StreamsConfig.STATE_DIR_CONFIG, AppConfigs.stateStoreName);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KStream<String, PaymentRequest> kStream1 = streamsBuilder.stream(AppConfigs.paymentRequestTopicName,
            Consumed.with(AppSerdes.String(), AppSerdes.PaymentRequest())
                .withTimestampExtractor(AppTimestampExtractor.PaymentRequest())
        );

        KStream<String, PaymentConfirmation> kStream2 = streamsBuilder.stream(AppConfigs.paymentConfirmationTopicName,
            Consumed.with(AppSerdes.String(), AppSerdes.PaymentConfirmation())
                .withTimestampExtractor(AppTimestampExtractor.PaymentConfirmation())
        );

        kStream1.join(kStream2, (requestObject, confirmationObject) ->
                        new TransactionStatus()
                                .withTransactionID(requestObject.getTransactionID())
                                .withStatus((requestObject.getOTP().equals(confirmationObject.getOTP()) ? "Success" : "Failure")),
                JoinWindows.of(Duration.ofMinutes(5)),
                Joined.with(AppSerdes.String(), AppSerdes.PaymentRequest(), AppSerdes.PaymentConfirmation())
        ).print(Printed.toSysOut());

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
        }));

    }
}
