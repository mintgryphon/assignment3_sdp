package com.weather.abstraction;

import com.weather.implementor.SensorDataSource;
import com.weather.implementor.SensorReadException;
import com.weather.implementor.SensorReading;

public class StatisticalSummaryMonitor extends WeatherMonitor {
    public StatisticalSummaryMonitor(SensorDataSource dataSource) {
        super(dataSource);
    }

    @Override
    public String generateReport(String sensorId) throws SensorReadException {
        SensorReading reading = dataSource.fetchReading(sensorId);
        return String.format("--- Summary Report ---\nSensor: %s\nValue: %.2f %s\nRecorded At: %s",
                sensorId, reading.value(), reading.unit(), reading.timestamp());
    }
}