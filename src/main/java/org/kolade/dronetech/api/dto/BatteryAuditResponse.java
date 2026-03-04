package org.kolade.dronetech.api.dto;

import java.time.Instant;

public record BatteryAuditResponse(
    String droneSerialNumber,
    Integer batteryCapacityPercent,
    Instant checkedAt
) {
}
