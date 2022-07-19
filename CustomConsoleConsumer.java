package com.info.consumer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringSerializer;

public class CustomConsoleConsumer {
	
	public static void main(String[] args) {
		
		Properties properties = new Properties();
		properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
		properties.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-consumer");
		properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		properties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
		properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringSerializer.class);
		
		@SuppressWarnings("resource")
		KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
		
		String topicName = "mytopic";
		
		consumer.subscribe(Collections.singletonList(topicName));
		
		while(true) {
			ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
			
			for (ConsumerRecord<String, String> record : records) {
				System.out.println("Key: "+record.key()+", Value: "+record.value());
			}
			
		}
		
		
	}

}
