package org.kolade.dronetech.service;

import java.util.List;
import org.kolade.dronetech.api.dto.BatteryResponse;
import org.kolade.dronetech.api.dto.DroneRequest;
import org.kolade.dronetech.api.dto.DroneResponse;
import org.kolade.dronetech.domain.Drone;
import org.kolade.dronetech.domain.DroneState;
import org.kolade.dronetech.exception.ConflictException;
import org.kolade.dronetech.exception.NotFoundException;
import org.kolade.dronetech.repository.DroneRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Transactional
    public DroneResponse registerDrone(DroneRequest request) {
        droneRepository.findBySerialNumber(request.serialNumber())
            .ifPresent(existing -> {
                throw new ConflictException("Drone with serial number '" + request.serialNumber() + "' already exists.");
            });

        DroneState state = request.state() == null ? DroneState.IDLE : request.state();
        Drone drone = new Drone(
            request.serialNumber(),
            request.model(),
            request.weightLimitGrams(),
            request.batteryCapacityPercent(),
            state
        );

        return toDroneResponse(droneRepository.save(drone));
    }

    @Transactional(readOnly = true)
    public List<DroneResponse> getAvailableDrones() {
        return droneRepository.findByStateAndBatteryCapacityPercentGreaterThanEqual(DroneState.IDLE, 25)
            .stream()
            .map(DroneService::toDroneResponse)
            .toList();
    }

    @Transactional(readOnly = true)
    public BatteryResponse getBatteryLevel(String serialNumber) {
        Drone drone = getDroneBySerialNumber(serialNumber);
        return new BatteryResponse(drone.getSerialNumber(), drone.getBatteryCapacityPercent());
    }

    @Transactional(readOnly = true)
    public Drone getDroneBySerialNumber(String serialNumber) {
        return droneRepository.findBySerialNumber(serialNumber)
            .orElseThrow(() -> new NotFoundException("Drone with serial number '" + serialNumber + "' was not found."));
    }

    public static DroneResponse toDroneResponse(Drone drone) {
        return new DroneResponse(
            drone.getSerialNumber(),
            drone.getModel(),
            drone.getWeightLimitGrams(),
            drone.getBatteryCapacityPercent(),
            drone.getState()
        );
    }
}
