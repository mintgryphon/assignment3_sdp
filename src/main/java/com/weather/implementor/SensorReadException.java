package com.weather.implementor;

public class SensorReadException extends Exception {
    public SensorReadException(String message) {
        super(message);
    }
    public SensorReadException(String message, Throwable cause) {
        super(message, cause);
    }
}