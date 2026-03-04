package org.kolade.dronetech.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneModel model;

    @Column(nullable = false)
    private Integer weightLimitGrams;

    @Column(nullable = false)
    private Integer batteryCapacityPercent;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneState state;

    protected Drone() {
    }

    public Drone(String serialNumber, DroneModel model, Integer weightLimitGrams, Integer batteryCapacityPercent, DroneState state) {
        this.serialNumber = serialNumber;
        this.model = model;
        this.weightLimitGrams = weightLimitGrams;
        this.batteryCapacityPercent = batteryCapacityPercent;
        this.state = state;
    }

    public UUID getId() {
        return id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public DroneModel getModel() {
        return model;
    }

    public Integer getWeightLimitGrams() {
        return weightLimitGrams;
    }

    public Integer getBatteryCapacityPercent() {
        return batteryCapacityPercent;
    }

    public DroneState getState() {
        return state;
    }

    public void setState(DroneState state) {
        this.state = state;
    }
}
