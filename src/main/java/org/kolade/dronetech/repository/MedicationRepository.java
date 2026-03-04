package org.kolade.dronetech.repository;

import java.util.Optional;
import java.util.UUID;
import org.kolade.dronetech.domain.Medication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationRepository extends JpaRepository<Medication, UUID> {

    Optional<Medication> findByCode(String code);
}
