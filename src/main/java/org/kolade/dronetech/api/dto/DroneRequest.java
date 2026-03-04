package org.kolade.dronetech.api.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.kolade.dronetech.domain.DroneModel;
import org.kolade.dronetech.domain.DroneState;

public record DroneRequest(
    @NotBlank @Size(max = 100) String serialNumber,
    @NotNull DroneModel model,
    @NotNull @Max(500) Integer weightLimitGrams,
    @NotNull @Min(0) @Max(100) Integer batteryCapacityPercent,
    @NotNull DroneState state
) {
}
