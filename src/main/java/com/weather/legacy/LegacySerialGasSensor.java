package com.weather.legacy;

import java.nio.charset.StandardCharsets;

public class LegacySerialGasSensor {
    public int pollRawData(int portNumber, byte[] buffer) {
        if (portNumber < 0) {
            return -2;
        }
        if (portNumber == 99) {
            return -1;
        }
        byte[] payload = "PPM:420.5".getBytes(StandardCharsets.UTF_8);
        System.arraycopy(payload, 0, buffer, 0, payload.length);
        return 0;
    }
}