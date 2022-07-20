package com.info.serde;

import java.io.Closeable;

import org.apache.kafka.common.serialization.Deserializer;

import com.google.gson.Gson;
import com.info.entity.TaxiZone;

public class ZoneDeserializer implements Closeable, AutoCloseable, Deserializer<TaxiZone> {
	static private Gson gson = new Gson();

	@Override
	public TaxiZone deserialize(String topic, byte[] data) {

		String zone = new String(data);
		return gson.fromJson(zone, TaxiZone.class);
	}

	
}
