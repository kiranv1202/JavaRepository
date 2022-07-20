package com.info.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.info.entity.TaxiZone;
import com.info.producer.TaxiZoneProducer;

public class MySQLSimulator {

	Connection connection;
	TaxiZoneProducer taxiZoneProducer;
	public MySQLSimulator() {
		connection = DBUtil.getMySQLDBConnection();
		taxiZoneProducer = new TaxiZoneProducer();
	}
	
	public void getData() {
		try {
			String query = "select * from taxizone";
			PreparedStatement pStatement = connection.prepareStatement(query);
			
			ResultSet resultSet = pStatement.executeQuery();
			
			
			while(resultSet.next()) {
				TaxiZone taxiZone = new TaxiZone();
				taxiZone.setLocationId(resultSet.getInt("locationid"));
				taxiZone.setBorough(resultSet.getString("borough"));
				taxiZone.setZone(resultSet.getString("zone"));
				taxiZone.setService_zone(resultSet.getString("service_zone"));

				taxiZoneProducer.produceMessage(taxiZone.getLocationId(), taxiZone);	
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		new MySQLSimulator().getData();
	}
	

}
