package org.kolade.dronetech.api.dto;

import org.kolade.dronetech.domain.DroneModel;
import org.kolade.dronetech.domain.DroneState;

public record DroneResponse(
    String serialNumber,
    DroneModel model,
    Integer weightLimitGrams,
    Integer batteryCapacityPercent,
    DroneState state
) {
}
