package com.weather.implementor;

import java.time.Instant;

public class MqttSensorDataSource implements SensorDataSource {
    @Override
    public SensorReading fetchReading(String sensorId) throws SensorReadException {
        if (sensorId == null || sensorId.isBlank()) {
            throw new SensorReadException("Invalid MQTT sensor ID");
        }
        return new SensorReading(24.5, "°C", Instant.now());
    }
}