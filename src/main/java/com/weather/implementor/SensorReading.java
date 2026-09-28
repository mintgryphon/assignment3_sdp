package com.weather.implementor;

import java.time.Instant;

public record SensorReading(double value, String unit, Instant timestamp) {}