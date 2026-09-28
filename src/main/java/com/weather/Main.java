package com.weather;

import com.weather.abstraction.*;
import com.weather.implementor.SensorDataSource;
import com.weather.registry.SensorDataSourceRegistry;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("=== SCENARIO 1: Temperature Sensor (MQTT) ===");
            SensorDataSource mqttSource = SensorDataSourceRegistry.resolve("mqtt://broker/campus/temp");
            WeatherMonitor alertMonitor = new ThresholdAlertMonitor(mqttSource, 20.0);
            System.out.println(alertMonitor.generateReport("TEMP-ROOM-101"));

            System.out.println("\n=== SCENARIO 2: Humidity Sensor (REST API) ===");
            SensorDataSource httpSource = SensorDataSourceRegistry.resolve("http://api.weather.campus/humidity");
            WeatherMonitor summaryMonitor = new StatisticalSummaryMonitor(httpSource);
            System.out.println(summaryMonitor.generateReport("HUMIDITY-LAB-3"));

            System.out.println("\n=== SCENARIO 3: Gas Sensor via ADAPTER (COM Port 2) ===");
            SensorDataSource legacySource = SensorDataSourceRegistry.resolve("serial://port/2");
            WeatherMonitor gasAlertMonitor = new ThresholdAlertMonitor(legacySource, 500.0);
            System.out.println(gasAlertMonitor.generateReport("GAS-SENSOR-OLD"));

            System.out.println("\n=== SCENARIO 4: Sensor Error Handling (Timeout on Port 99) ===");
            SensorDataSource brokenSource = SensorDataSourceRegistry.resolve("serial://port/99");
            WeatherMonitor brokenMonitor = new ThresholdAlertMonitor(brokenSource, 100.0);
            System.out.println(brokenMonitor.generateReport("BROKEN-SENSOR"));

        } catch (Exception e) {
            System.err.println("Caught contract exception: " + e.getMessage());
        }
    }
}