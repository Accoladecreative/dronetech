package org.kolade.dronetech.api.dto;

public record LoadedMedicationItemResponse(
    String medicationCode,
    String medicationName,
    Integer weightGrams,
    Integer quantity,
    Integer totalWeightGrams
) {
}
