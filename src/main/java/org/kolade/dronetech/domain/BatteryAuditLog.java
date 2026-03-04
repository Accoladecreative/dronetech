package org.kolade.dronetech.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "battery_audit_logs")
public class BatteryAuditLog {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String droneSerialNumber;

    @Column(nullable = false)
    private Integer batteryCapacityPercent;

    @Column(nullable = false, updatable = false)
    private Instant checkedAt;

    protected BatteryAuditLog() {
    }

    public BatteryAuditLog(String droneSerialNumber, Integer batteryCapacityPercent) {
        this.droneSerialNumber = droneSerialNumber;
        this.batteryCapacityPercent = batteryCapacityPercent;
    }

    @PrePersist
    void prePersist() {
        if (checkedAt == null) {
            checkedAt = Instant.now();
        }
    }

    public UUID getId() {
        return id;
    }

    public String getDroneSerialNumber() {
        return droneSerialNumber;
    }

    public Integer getBatteryCapacityPercent() {
        return batteryCapacityPercent;
    }

    public Instant getCheckedAt() {
        return checkedAt;
    }
}
