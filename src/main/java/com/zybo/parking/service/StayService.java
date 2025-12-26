package com.zybo.parking.service;

import com.zybo.parking.dto.CheckInRequest;
import com.zybo.parking.dto.StayDTO;
import com.zybo.parking.entity.Stay;
import com.zybo.parking.entity.StayStatus;
import com.zybo.parking.entity.Vehicle;
import com.zybo.parking.exception.ConflictException;
import com.zybo.parking.exception.ResourceNotFoundException;
import com.zybo.parking.mapper.StayMapper;
import com.zybo.parking.repository.StayRepository;
import com.zybo.parking.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StayService {
    private final StayRepository stayRepository;
    private final VehicleRepository vehicleRepository;
    private final OutboxService outboxService;
    private final StayMapper stayMapper;

    private static final int RATE_PER_MINUTE = 100;

    @SuppressWarnings("null")
    @Transactional
    public StayDTO checkIn(CheckInRequest request) {
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        stayRepository.findByVehicleAndStatus(vehicle, StayStatus.OPEN)
                .ifPresent(s -> {
                    throw new ConflictException("Vehicle already has an OPEN stay");
                });

        Stay stay = Stay.builder()
                .vehicle(vehicle)
                .entryTime(LocalDateTime.now())
                .status(StayStatus.OPEN)
                .build();

        return stayMapper.toDto(stayRepository.save(stay));
    }

    @Transactional
    public StayDTO checkOut(Long stayId) {
        @SuppressWarnings("null")
        Stay stay = stayRepository.findById(stayId)
                .orElseThrow(() -> new ResourceNotFoundException("Stay not found"));

        if (stay.getStatus() == StayStatus.CLOSED) {
            throw new ConflictException("Stay is already CLOSED");
        }

        LocalDateTime exitTime = LocalDateTime.now();
        long minutes = Math.max(1, Duration.between(stay.getEntryTime(), exitTime).toMinutes());
        int amount = (int) (minutes * RATE_PER_MINUTE);

        stay.setExitTime(exitTime);
        stay.setMinutes(minutes);
        stay.setAmountCharged(amount);
        stay.setStatus(StayStatus.CLOSED);

        Stay savedStay = stayRepository.save(stay);

        // Register Outbox Event
        String payload = String.format(
                "{\"stayId\": %d, \"vehicleId\": %d, \"amountCharged\": %d, \"exitTime\": \"%s\"}",
                savedStay.getId(), savedStay.getVehicle().getId(), savedStay.getAmountCharged(),
                savedStay.getExitTime());
        outboxService.saveEvent("STAY_CLOSED", payload);

        return stayMapper.toDto(savedStay);
    }
}
