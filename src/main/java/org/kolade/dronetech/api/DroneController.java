package org.kolade.dronetech.api;

import jakarta.validation.Valid;
import java.util.List;
import org.kolade.dronetech.api.dto.BatteryResponse;
import org.kolade.dronetech.api.dto.DroneRequest;
import org.kolade.dronetech.api.dto.DroneResponse;
import org.kolade.dronetech.api.dto.LoadDroneRequest;
import org.kolade.dronetech.api.dto.LoadedMedicationItemResponse;
import org.kolade.dronetech.service.DroneService;
import org.kolade.dronetech.service.LoadingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/drones")
public class DroneController {

    private final DroneService droneService;
    private final LoadingService loadingService;

    public DroneController(DroneService droneService, LoadingService loadingService) {
        this.droneService = droneService;
        this.loadingService = loadingService;
    }

    @PostMapping
    public ResponseEntity<DroneResponse> registerDrone(@Valid @RequestBody DroneRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(droneService.registerDrone(request));
    }

    @PostMapping("/{serialNumber}/load")
    public DroneResponse loadDrone(@PathVariable String serialNumber, @Valid @RequestBody LoadDroneRequest request) {
        return loadingService.loadDrone(serialNumber, request);
    }

    @GetMapping("/{serialNumber}/medications")
    public List<LoadedMedicationItemResponse> getLoadedMedications(@PathVariable String serialNumber) {
        return loadingService.getLoadedMedications(serialNumber);
    }

    @GetMapping("/available")
    public List<DroneResponse> getAvailableDrones() {
        return droneService.getAvailableDrones();
    }

    @GetMapping("/{serialNumber}/battery")
    public BatteryResponse getBatteryLevel(@PathVariable String serialNumber) {
        return droneService.getBatteryLevel(serialNumber);
    }
}
