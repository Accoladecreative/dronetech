package org.kolade.dronetech.repository;

import java.util.List;
import java.util.UUID;
import org.kolade.dronetech.domain.BatteryAuditLog;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatteryAuditLogRepository extends JpaRepository<BatteryAuditLog, UUID> {

    List<BatteryAuditLog> findByDroneSerialNumberOrderByCheckedAtDesc(String droneSerialNumber, Pageable pageable);
}
