package org.kolade.dronetech.api.dto;

public record BatteryResponse(
    String serialNumber,
    Integer batteryCapacityPercent
) {
}
