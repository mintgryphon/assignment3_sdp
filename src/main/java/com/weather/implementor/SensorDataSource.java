package com.weather.implementor;

public interface SensorDataSource {
    SensorReading fetchReading(String sensorId) throws SensorReadException;
}