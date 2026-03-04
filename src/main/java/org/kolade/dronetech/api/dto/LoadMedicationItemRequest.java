package org.kolade.dronetech.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record LoadMedicationItemRequest(
    @NotBlank @Pattern(regexp = "^[A-Z0-9_]+$") String medicationCode,
    @NotNull @Min(1) Integer quantity
) {
}
