package org.kolade.dronetech.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.kolade.dronetech.api.dto.LoadDroneRequest;
import org.kolade.dronetech.api.dto.LoadMedicationItemRequest;
import org.kolade.dronetech.api.dto.DroneResponse;
import org.kolade.dronetech.domain.Drone;
import org.kolade.dronetech.domain.DroneModel;
import org.kolade.dronetech.domain.DroneState;
import org.kolade.dronetech.domain.Medication;
import org.kolade.dronetech.exception.ConflictException;
import org.kolade.dronetech.repository.DroneRepository;
import org.kolade.dronetech.repository.MedicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class DroneServiceTests {

    @Autowired
    private LoadingService loadingService;

    @Autowired
    private DroneService droneService;

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private MedicationRepository medicationRepository;

    @Test
    void loadingFailsWhenBatteryIsBelowTwentyFivePercent() {
        Drone lowBatteryDrone = droneRepository.save(
            new Drone("TST-LOW-BAT", DroneModel.LIGHTWEIGHT, 200, 20, DroneState.IDLE)
        );

        Medication medication = medicationRepository.save(
            new Medication("TestMed_Battery", 10, "TEST_MED_BATTERY", "https://example.com/test-battery.png")
        );

        LoadDroneRequest request = new LoadDroneRequest(
            List.of(new LoadMedicationItemRequest(medication.getCode(), 1))
        );

        assertThatThrownBy(() -> loadingService.loadDrone(lowBatteryDrone.getSerialNumber(), request))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("below 25%");
    }

    @Test
    void loadingFailsWhenWeightExceedsLimit() {
        Drone limitedDrone = droneRepository.save(
            new Drone("TST-WEIGHT-LIMIT", DroneModel.LIGHTWEIGHT, 50, 90, DroneState.IDLE)
        );

        Medication heavyMedication = medicationRepository.save(
            new Medication("HeavyMed_Test", 30, "HEAVY_MED_TEST", "https://example.com/heavy-med.png")
        );

        LoadDroneRequest request = new LoadDroneRequest(
            List.of(new LoadMedicationItemRequest(heavyMedication.getCode(), 2))
        );

        assertThatThrownBy(() -> loadingService.loadDrone(limitedDrone.getSerialNumber(), request))
            .isInstanceOf(ConflictException.class)
            .hasMessageContaining("exceeds drone weight limit");
    }

    @Test
    void availableDronesReturnsOnlyIdleWithBatteryAtLeastTwentyFive() {
        Drone availableDrone = droneRepository.save(
            new Drone("TST-AVAILABLE", DroneModel.LIGHTWEIGHT, 150, 75, DroneState.IDLE)
        );
        Drone lowBatteryDrone = droneRepository.save(
            new Drone("TST-LOW-BATTERY-UNAVAILABLE", DroneModel.LIGHTWEIGHT, 150, 20, DroneState.IDLE)
        );
        Drone nonIdleDrone = droneRepository.save(
            new Drone("TST-NON-IDLE-UNAVAILABLE", DroneModel.LIGHTWEIGHT, 150, 80, DroneState.LOADING)
        );

        List<DroneResponse> available = droneService.getAvailableDrones();

        assertThat(available)
            .extracting(DroneResponse::serialNumber)
            .contains(availableDrone.getSerialNumber())
            .doesNotContain(lowBatteryDrone.getSerialNumber(), nonIdleDrone.getSerialNumber());

        assertThat(available)
            .allSatisfy(drone -> {
                assertThat(drone.state()).isEqualTo(DroneState.IDLE);
                assertThat(drone.batteryCapacityPercent()).isGreaterThanOrEqualTo(25);
            });
    }
}
