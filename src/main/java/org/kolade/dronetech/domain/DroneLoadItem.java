package org.kolade.dronetech.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "drone_load_items")
public class DroneLoadItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "drone_id", nullable = false)
    private Drone drone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "medication_id", nullable = false)
    private Medication medication;

    @Column(nullable = false)
    private Integer quantity = 1;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    protected DroneLoadItem() {
    }

    public DroneLoadItem(Drone drone, Medication medication, Integer quantity) {
        this.drone = drone;
        this.medication = medication;
        if (quantity != null) {
            this.quantity = quantity;
        }
    }

    @PrePersist
    void prePersist() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (quantity == null) {
            quantity = 1;
        }
    }

    public UUID getId() {
        return id;
    }

    public Drone getDrone() {
        return drone;
    }

    public Medication getMedication() {
        return medication;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
