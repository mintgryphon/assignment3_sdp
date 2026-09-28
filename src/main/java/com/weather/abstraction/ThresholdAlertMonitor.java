package com.weather.abstraction;

import com.weather.implementor.SensorDataSource;
import com.weather.implementor.SensorReadException;
import com.weather.implementor.SensorReading;

public class ThresholdAlertMonitor extends WeatherMonitor {
    private final double threshold;

    public ThresholdAlertMonitor(SensorDataSource dataSource, double threshold) {
        super(dataSource);
        this.threshold = threshold;
    }

    @Override
    public String generateReport(String sensorId) throws SensorReadException {
        SensorReading reading = dataSource.fetchReading(sensorId);
        if (reading.value() > threshold) {
            return String.format("[ALERT] Sensor %s exceeded limit! Current: %.2f %s (Threshold: %.2f)",
                    sensorId, reading.value(), reading.unit(), threshold);
        }
        return String.format("[OK] Sensor %s within normal range: %.2f %s",
                sensorId, reading.value(), reading.unit());
    }
}