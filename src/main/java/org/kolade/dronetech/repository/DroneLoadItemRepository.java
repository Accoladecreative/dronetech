package org.kolade.dronetech.repository;

import java.util.List;
import java.util.UUID;
import org.kolade.dronetech.domain.DroneLoadItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DroneLoadItemRepository extends JpaRepository<DroneLoadItem, UUID> {

    List<DroneLoadItem> findByDrone_SerialNumber(String serialNumber);
}
