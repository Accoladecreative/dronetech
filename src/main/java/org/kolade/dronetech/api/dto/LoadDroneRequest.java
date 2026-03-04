package org.kolade.dronetech.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record LoadDroneRequest(
    @NotEmpty List<@Valid LoadMedicationItemRequest> items
) {
}
