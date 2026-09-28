# Environmental & Weather Monitoring System (Bridge + Adapter Patterns)

## 1. Domain Problem
This system provides real-time and analytical monitoring of campus environmental parameters (temperature, humidity, and gas concentration). It separates high-level report generation and alerting mechanisms from underlying heterogeneous telemetry hardware and network protocols.

## 2. Why Neither Bridge Nor Adapter Alone is Sufficient
- **Why Bridge alone is not enough:** The Bridge pattern decouples the abstraction hierarchy (`WeatherMonitor`) from the implementor hierarchy (`SensorDataSource`). However, Bridge requires all implementations to natively adhere to the unified contract. It cannot integrate legacy hardware components whose source code cannot be modified.
- **Why Adapter alone is not enough:** The Adapter pattern adapts the incompatible `LegacySerialGasSensor` to conform to `SensorDataSource`. However, using Adapter alone does not decouple the abstraction variants (`ThresholdAlertMonitor`, `StatisticalSummaryMonitor`) from implementations, causing a combinatorial explosion of subclasses (e.g., `MqttAlertMonitor`, `LegacySummaryMonitor`)[cite: 1].
- **Combined Value:** Bridge prevents combinatorial subclass explosion across two independent axes of change, while Adapter enables an incompatible legacy driver to seamlessly fulfill the `Implementor` role without touching existing code[cite: 1].

## 3. Genuine Incompatibility of the Adapted Class
The `LegacySerialGasSensor` exhibits deep, non-trivial incompatibility conforming to Section 3.3[cite: 1]:
1. **Method Signature & Parameter Types:** It exposes `int pollRawData(int portNumber, byte[] buffer)` instead of the domain contract's `SensorReading fetchReading(String sensorId)`[cite: 1].
2. **Data Representation:** It mutates a caller-provided raw byte buffer with ASCII text (`"PPM:420.5"`) instead of returning a typed domain object[cite: 1].
3. **Failure Mechanism:** It signals failure using integer status codes (`-1` for timeout, `-2` for hardware error, `0` for success)[cite: 1]. The `LegacySensorAdapter` translates these codes into domain-compliant `SensorReadException` instances, ensuring no legacy constants or error models leak into the abstraction layer[cite: 1].

## 4. Open/Closed Principle (OCP) Compliance
The system satisfies OCP along both dimensions[cite: 1]:
- **New Abstraction:** Adding a new monitor (e.g., `JsonExportMonitor`) requires extending `WeatherMonitor` without changing any existing monitor or data source classes[cite: 1].
- **New Implementor:** Adding a new protocol (e.g., `BluetoothSensorDataSource`) requires implementing `SensorDataSource` without altering existing abstractions or implementors[cite: 1].

## 5. Required Complexity Module: Dynamic Implementor Selection
The project adopts **Dynamic Implementor Selection** via `SensorDataSourceRegistry`[cite: 1].
- The concrete implementor (including the adapted legacy sensor) is resolved dynamically at runtime from connection URIs (`mqtt://...`, `http://...`, `serial://port/2`)[cite: 1].
- The client remains fully decoupled and never hard-codes concrete data sources[cite: 1].

## 6. Known Limitation
The `LegacySensorAdapter` relies on synchronous, blocking operations with a fixed 64-byte buffer[cite: 1]. Under heavy telemetry volume or physical serial-port communication latency, the calling thread is blocked, which limits high-throughput concurrency[cite: 1].

## 7. UML Diagram
The UML class diagram reflecting the implemented architecture is located at `./uml_diagram.png` in this repository[cite: 1].
