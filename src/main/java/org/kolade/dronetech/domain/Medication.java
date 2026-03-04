package org.kolade.dronetech.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer weightGrams;

    @Column(nullable = false, unique = true)
    private String code;

    @Column
    private String image;

    protected Medication() {
    }

    public Medication(String name, Integer weightGrams, String code, String image) {
        this.name = name;
        this.weightGrams = weightGrams;
        this.code = code;
        this.image = image;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getWeightGrams() {
        return weightGrams;
    }

    public String getCode() {
        return code;
    }

    public String getImage() {
        return image;
    }
}
