package org.kolade.dronetech.repository;

import java.util.Optional;
import java.util.UUID;
import org.kolade.dronetech.domain.Drone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneRepository extends JpaRepository<Drone, UUID> {

    Optional<Drone> findBySerialNumber(String serialNumber);
}
