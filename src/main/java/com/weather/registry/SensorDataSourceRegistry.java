package com.weather.registry;

import com.weather.implementor.*;
import com.weather.legacy.LegacySerialGasSensor;

public class SensorDataSourceRegistry {
    public static SensorDataSource resolve(String connectionUri) {
        if (connectionUri.startsWith("mqtt://")) {
            return new MqttSensorDataSource();
        } else if (connectionUri.startsWith("http://") || connectionUri.startsWith("https://")) {
            return new HttpRestSensorDataSource();
        } else if (connectionUri.startsWith("serial://")) {
            int port = Integer.parseInt(connectionUri.replaceAll("\\D+", ""));
            return new LegacySensorAdapter(new LegacySerialGasSensor(), port);
        }
        throw new IllegalArgumentException("Unsupported sensor protocol: " + connectionUri);
    }
}