package com.weather;

import com.weather.abstraction.*;
import com.weather.implementor.*;
import com.weather.legacy.LegacySerialGasSensor;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class WeatherSystemTest {

    // 1. Тест Abstraction 1 (ThresholdAlertMonitor): проверка генерации алерта
    @Test
    void testThresholdAlertMonitor_TriggerAlert() throws SensorReadException {
        SensorDataSource stubSource = sensorId -> new SensorReading(35.0, "°C", Instant.now());

        WeatherMonitor monitor = new ThresholdAlertMonitor(stubSource, 30.0);
        String report = monitor.generateReport("TEMP-01");

        assertTrue(report.contains("[ALERT]"));
        assertTrue(report.contains("TEMP-01"));
        assertTrue(report.contains("°C"));
    }

    // 2. Тест Abstraction 2 (StatisticalSummaryMonitor): проверка формирования сводки
    @Test
    void testStatisticalSummaryMonitor_Success() throws SensorReadException {
        SensorDataSource stubSource = sensorId -> new SensorReading(60.0, "%", Instant.now());

        WeatherMonitor monitor = new StatisticalSummaryMonitor(stubSource);
        String report = monitor.generateReport("HUM-01");

        assertTrue(report.contains("Summary Report"));
        assertTrue(report.contains("HUM-01"));
        assertTrue(report.contains("%"));
    }

    // 3. Тест Adapter: успешное чтение и парсинг данных
    @Test
    void testLegacySensorAdapter_SuccessPath() throws SensorReadException {
        LegacySerialGasSensor sensor = new LegacySerialGasSensor();
        SensorDataSource adapter = new LegacySensorAdapter(sensor, 2);

        SensorReading reading = adapter.fetchReading("GAS-PORT-2");

        assertEquals(420.5, reading.value());
        assertEquals("PPM", reading.unit());
    }

    // 4. Тест Adapter (Failure Translation): трансляция таймаута в SensorReadException
    @Test
    void testLegacySensorAdapter_FailureTranslation_Timeout() {
        LegacySerialGasSensor sensor = new LegacySerialGasSensor();
        SensorDataSource adapter = new LegacySensorAdapter(sensor, 99);

        SensorReadException ex = assertThrows(SensorReadException.class, () -> {
            adapter.fetchReading("GAS-PORT-99");
        });
        assertTrue(ex.getMessage().contains("timeout"));
    }

    // 5. Тест Adapter (Failure Translation): трансляция аппаратного сбоя в SensorReadException
    @Test
    void testLegacySensorAdapter_FailureTranslation_HardwareError() {
        LegacySerialGasSensor sensor = new LegacySerialGasSensor();
        SensorDataSource adapter = new LegacySensorAdapter(sensor, -1);

        SensorReadException ex = assertThrows(SensorReadException.class, () -> {
            adapter.fetchReading("GAS-PORT-INVALID");
        });
        assertTrue(ex.getMessage().contains("hardware error"));
    }
}