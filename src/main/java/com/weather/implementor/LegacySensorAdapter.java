package com.weather.implementor;

import com.weather.legacy.LegacySerialGasSensor;
import java.nio.charset.StandardCharsets;
import java.time.Instant;

public class LegacySensorAdapter implements SensorDataSource {
    private final LegacySerialGasSensor legacySensor;
    private final int portNumber;

    public LegacySensorAdapter(LegacySerialGasSensor legacySensor, int portNumber) {
        this.legacySensor = legacySensor;
        this.portNumber = portNumber;
    }

    @Override
    public SensorReading fetchReading(String sensorId) throws SensorReadException {
        byte[] buffer = new byte[64];
        int statusCode = legacySensor.pollRawData(portNumber, buffer);

        if (statusCode == -1) {
            throw new SensorReadException("Legacy sensor timeout on port " + portNumber);
        } else if (statusCode == -2) {
            throw new SensorReadException("Legacy hardware error on port " + portNumber);
        } else if (statusCode != 0) {
            throw new SensorReadException("Unknown legacy sensor status: " + statusCode);
        }

        try {
            String rawText = new String(buffer, StandardCharsets.UTF_8).trim();
            String[] parts = rawText.split(":");
            String unit = parts[0];
            double value = Double.parseDouble(parts[1]);
            return new SensorReading(value, unit, Instant.now());
        } catch (Exception e) {
            throw new SensorReadException("Failed to parse legacy payload", e);
        }
    }
}