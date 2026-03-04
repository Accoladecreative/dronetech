package org.kolade.dronetech.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record DroneLoadItemRequest(
    @NotNull UUID droneId,
    @NotNull UUID medicationId,
    @NotNull @Min(1) Integer quantity
) {
}
