package org.kolade.dronetech.service;

import java.util.List;
import org.kolade.dronetech.api.dto.BatteryAuditResponse;
import org.kolade.dronetech.domain.BatteryAuditLog;
import org.kolade.dronetech.domain.Drone;
import org.kolade.dronetech.repository.BatteryAuditLogRepository;
import org.kolade.dronetech.repository.DroneRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BatteryAuditService {

    private final DroneRepository droneRepository;
    private final BatteryAuditLogRepository batteryAuditLogRepository;

    public BatteryAuditService(DroneRepository droneRepository, BatteryAuditLogRepository batteryAuditLogRepository) {
        this.droneRepository = droneRepository;
        this.batteryAuditLogRepository = batteryAuditLogRepository;
    }

    @Scheduled(fixedRateString = "${dronetech.audit.fixed-rate-ms:60000}")
    @Transactional
    public void auditDroneBatteries() {
        List<BatteryAuditLog> logs = droneRepository.findAll()
            .stream()
            .map(this::toAuditLog)
            .toList();

        if (!logs.isEmpty()) {
            batteryAuditLogRepository.saveAll(logs);
        }
    }

    @Transactional(readOnly = true)
    public List<BatteryAuditResponse> getAuditLogs(String serialNumber, int limit) {
        return batteryAuditLogRepository
            .findByDroneSerialNumberOrderByCheckedAtDesc(serialNumber, PageRequest.of(0, limit))
            .stream()
            .map(log -> new BatteryAuditResponse(log.getDroneSerialNumber(), log.getBatteryCapacityPercent(), log.getCheckedAt()))
            .toList();
    }

    private BatteryAuditLog toAuditLog(Drone drone) {
        return new BatteryAuditLog(drone.getSerialNumber(), drone.getBatteryCapacityPercent());
    }
}
