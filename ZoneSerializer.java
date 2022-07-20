package com.info.serde;

import java.io.Closeable;

import org.apache.kafka.common.serialization.Serializer;

import com.google.gson.Gson;
import com.info.entity.TaxiZone;

public class ZoneSerializer implements Closeable, AutoCloseable, Serializer<TaxiZone> {
	
	static private Gson gson = new Gson();

	@Override
	public byte[] serialize(String topic, TaxiZone taxiZone) {
		String line = gson.toJson(taxiZone);	// converting java object to json
		return line.getBytes();
	}

}
