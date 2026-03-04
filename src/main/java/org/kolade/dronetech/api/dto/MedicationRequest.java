package org.kolade.dronetech.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record MedicationRequest(
    @NotBlank @Pattern(regexp = "^[A-Za-z0-9_-]+$") String name,
    @NotNull @Min(1) Integer weightGrams,
    @NotBlank @Pattern(regexp = "^[A-Z0-9_]+$") String code,
    String image
) {
}
