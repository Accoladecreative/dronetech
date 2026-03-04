package org.kolade.dronetech.api.dto;

import java.time.Instant;

public record ApiErrorResponse(
    String error,
    String message,
    Instant timestamp,
    String path
) {
}
