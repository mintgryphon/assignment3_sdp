package com.weather.implementor;

import java.time.Instant;

public class HttpRestSensorDataSource implements SensorDataSource {
    @Override
    public SensorReading fetchReading(String sensorId) throws SensorReadException {
        if (sensorId == null || sensorId.isBlank()) {
            throw new SensorReadException("Invalid HTTP sensor ID");
        }
        return new SensorReading(45.0, "%", Instant.now());
    }
}