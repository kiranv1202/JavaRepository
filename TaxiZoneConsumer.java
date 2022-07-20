package com.info.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import com.info.entity.TaxiZone;
import com.info.serde.ZoneDeserializer;

public class TaxiZoneConsumer {

	KafkaConsumer<Integer, TaxiZone> consumer = null;

	public TaxiZoneConsumer() {
		createConsumer();
	}
	
	public void createConsumer() {
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "Group-1");
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ZoneDeserializer.class);
		
		consumer = new KafkaConsumer<Integer, TaxiZone>(properties);
		consumer.subscribe(Collections.singletonList("taxizone-topic"));
	}
	
	public void consumeMessage() {
		
		while(true) {
			ConsumerRecords<Integer, TaxiZone> records = consumer.poll(Duration.ofMillis(100));

			for (ConsumerRecord<Integer, TaxiZone> record : records) {
				System.out.println(record);
			}
		}
	}
	
	public static void main(String[] args) {
		new TaxiZoneConsumer().consumeMessage();
	}
	
}
