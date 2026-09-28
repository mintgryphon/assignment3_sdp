package com.weather.abstraction;

import com.weather.implementor.SensorDataSource;
import com.weather.implementor.SensorReadException;

public abstract class WeatherMonitor {
    protected final SensorDataSource dataSource;

    protected WeatherMonitor(SensorDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public abstract String generateReport(String sensorId) throws SensorReadException;
}