package com.info.producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;

import com.info.entity.TaxiZone;
import com.info.serde.ZoneSerializer;

public class TaxiZoneProducer {
	
	static Producer<Integer, TaxiZone> producer = null;
	
	public TaxiZoneProducer() {
		createProducer();
	}
	
	private void createProducer() {
		Properties properties = new Properties();
		properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
		properties.put(ProducerConfig.CLIENT_ID_CONFIG, "Client-1");
		properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
		properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ZoneSerializer.class);
	
		producer = new KafkaProducer<Integer, TaxiZone>(properties);
	}

	public void produceMessage(Integer key, TaxiZone taxiZone)throws InterruptedException {
		
		ProducerRecord<Integer, TaxiZone> record = new ProducerRecord<Integer, TaxiZone>("taxizone-topic", key, taxiZone);
		producer.send(record);
		
		System.out.println(taxiZone);
		Thread.sleep(1000);
	}

}
