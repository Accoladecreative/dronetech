package org.kolade.dronetech.config;

import java.util.List;
import org.kolade.dronetech.domain.Drone;
import org.kolade.dronetech.domain.DroneModel;
import org.kolade.dronetech.domain.DroneState;
import org.kolade.dronetech.domain.Medication;
import org.kolade.dronetech.repository.DroneRepository;
import org.kolade.dronetech.repository.MedicationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedData(DroneRepository droneRepository, MedicationRepository medicationRepository) {
        return args -> {
            if (droneRepository.count() == 0) {
                droneRepository.saveAll(List.of(
                    new Drone("DRN-001", DroneModel.LIGHTWEIGHT, 100, 95, DroneState.IDLE),
                    new Drone("DRN-002", DroneModel.LIGHTWEIGHT, 120, 78, DroneState.LOADING),
                    new Drone("DRN-003", DroneModel.MIDDLEWEIGHT, 250, 82, DroneState.LOADED),
                    new Drone("DRN-004", DroneModel.MIDDLEWEIGHT, 300, 67, DroneState.DELIVERING),
                    new Drone("DRN-005", DroneModel.CRUISERWEIGHT, 350, 54, DroneState.IDLE),
                    new Drone("DRN-006", DroneModel.CRUISERWEIGHT, 380, 48, DroneState.RETURNING),
                    new Drone("DRN-007", DroneModel.HEAVYWEIGHT, 450, 91, DroneState.LOADING),
                    new Drone("DRN-008", DroneModel.HEAVYWEIGHT, 500, 64, DroneState.IDLE),
                    new Drone("DRN-009", DroneModel.MIDDLEWEIGHT, 280, 39, DroneState.DELIVERED),
                    new Drone("DRN-010", DroneModel.LIGHTWEIGHT, 150, 72, DroneState.IDLE)
                ));
            }

            if (medicationRepository.count() == 0) {
                medicationRepository.saveAll(List.of(
                    new Medication("Aspirin_500", 20, "ASPIRIN_500", "https://medication.com/images/aspirin.png"),
                    new Medication("Amox-250", 30, "AMOX_250", "https://medication.com/images/amox.png"),
                    new Medication("VitaminC_1000", 15, "VITAMINC_1000", "https://medication.com/images/vitaminc.png"),
                    new Medication("PainRelief_Extra", 25, "PAINRELIEF_01", "https://medication.com/images/painrelief.png")
                ));
            }
        };
    }
}
