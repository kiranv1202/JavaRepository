package com.info;

import com.info.types.UserDetails;
import com.info.types.UserLogin;
import com.info.serde.AppSerdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;

import java.util.Properties;

public class KTableJoinDemo {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, AppConfigs.applicationID);
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, AppConfigs.bootstrapServers);
        props.put(StreamsConfig.STATE_DIR_CONFIG, AppConfigs.stateStoreName);
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, 0);

        StreamsBuilder streamsBuilder = new StreamsBuilder();
        KTable<String, UserDetails> kTable1 = streamsBuilder.table(AppConfigs.userMasterTopic,
            Consumed.with(AppSerdes.String(), AppSerdes.UserDetails())
        );

        KTable<String, UserLogin> kTable2 = streamsBuilder.table(AppConfigs.lastLoginTopic,
            Consumed.with(AppSerdes.String(), AppSerdes.UserLogin())
        );

        KTable<String, UserDetails> joinedTable = kTable1.join(kTable2, (userDetails, userLogin) -> {
            userDetails.setLastLogin(userLogin.getCreatedTime());
            return userDetails;
        });

        joinedTable.toStream().print(Printed.toSysOut());
        //kt.toStream().to("");

        KafkaStreams streams = new KafkaStreams(streamsBuilder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            streams.close();
        }));

    }
}
