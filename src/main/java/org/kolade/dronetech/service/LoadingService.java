package org.kolade.dronetech.service;

import java.util.ArrayList;
import java.util.List;
import org.kolade.dronetech.api.dto.LoadDroneRequest;
import org.kolade.dronetech.api.dto.LoadedMedicationItemResponse;
import org.kolade.dronetech.api.dto.DroneResponse;
import org.kolade.dronetech.domain.Drone;
import org.kolade.dronetech.domain.DroneLoadItem;
import org.kolade.dronetech.domain.DroneState;
import org.kolade.dronetech.domain.Medication;
import org.kolade.dronetech.exception.ConflictException;
import org.kolade.dronetech.exception.NotFoundException;
import org.kolade.dronetech.repository.DroneLoadItemRepository;
import org.kolade.dronetech.repository.DroneRepository;
import org.kolade.dronetech.repository.MedicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LoadingService {

    private final DroneService droneService;
    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneLoadItemRepository droneLoadItemRepository;

    public LoadingService(
        DroneService droneService,
        DroneRepository droneRepository,
        MedicationRepository medicationRepository,
        DroneLoadItemRepository droneLoadItemRepository
    ) {
        this.droneService = droneService;
        this.droneRepository = droneRepository;
        this.medicationRepository = medicationRepository;
        this.droneLoadItemRepository = droneLoadItemRepository;
    }

    @Transactional
    public DroneResponse loadDrone(String serialNumber, LoadDroneRequest request) {
        Drone drone = droneService.getDroneBySerialNumber(serialNumber);

        if (drone.getBatteryCapacityPercent() < 25) {
            throw new ConflictException("Drone battery level is below 25%, loading is not allowed.");
        }

        if (drone.getState() != DroneState.IDLE && drone.getState() != DroneState.LOADING) {
            throw new ConflictException("Drone state must be IDLE or LOADING to load medications.");
        }

        List<DroneLoadItem> existingItems = droneLoadItemRepository.findByDrone_SerialNumber(serialNumber);
        int existingWeight = existingItems.stream()
            .mapToInt(item -> item.getMedication().getWeightGrams() * item.getQuantity())
            .sum();

        List<DroneLoadItem> itemsToSave = new ArrayList<>();
        int newWeight = 0;

        for (var item : request.items()) {
            Medication medication = medicationRepository.findByCode(item.medicationCode())
                .orElseThrow(() -> new NotFoundException("Medication with code '" + item.medicationCode() + "' was not found."));

            newWeight += medication.getWeightGrams() * item.quantity();
            itemsToSave.add(new DroneLoadItem(drone, medication, item.quantity()));
        }

        if (existingWeight + newWeight > drone.getWeightLimitGrams()) {
            throw new ConflictException("Total medication weight exceeds drone weight limit.");
        }

        droneLoadItemRepository.saveAll(itemsToSave);
        drone.setState(DroneState.LOADED);
        droneRepository.save(drone);

        return DroneService.toDroneResponse(drone);
    }

    @Transactional(readOnly = true)
    public List<LoadedMedicationItemResponse> getLoadedMedications(String serialNumber) {
        droneService.getDroneBySerialNumber(serialNumber);

        return droneLoadItemRepository.findByDrone_SerialNumber(serialNumber)
            .stream()
            .map(item -> new LoadedMedicationItemResponse(
                item.getMedication().getCode(),
                item.getMedication().getName(),
                item.getMedication().getWeightGrams(),
                item.getQuantity(),
                item.getMedication().getWeightGrams() * item.getQuantity()
            ))
            .toList();
    }
}
