package com.zybo.parking.service;

import com.zybo.parking.dto.VehicleDTO;
import com.zybo.parking.dto.VehicleRequest;
import com.zybo.parking.entity.User;
import com.zybo.parking.entity.Vehicle;
import com.zybo.parking.exception.ConflictException;
import com.zybo.parking.exception.ResourceNotFoundException;
import com.zybo.parking.mapper.VehicleMapper;
import com.zybo.parking.repository.UserRepository;
import com.zybo.parking.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VehicleService {
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final VehicleMapper vehicleMapper;

    @Transactional
    public VehicleDTO createVehicle(VehicleRequest vehicleDTO) {
        vehicleRepository.findByPlate(vehicleDTO.getPlate()).ifPresent(v -> {
            throw new ConflictException("Plate already exists");
        });

        User user = userRepository.findById(vehicleDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Vehicle vehicle = vehicleMapper.toEntity(vehicleDTO);
        vehicle.setUser(user);
        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    public VehicleDTO getVehicle(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Transactional
    public VehicleDTO updateVehicle(Long id, VehicleRequest vehicleDTO) {
        Vehicle vehicle = vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        vehicleRepository.findByPlate(vehicleDTO.getPlate())
                .ifPresent(v -> {
                    if (!v.getId().equals(id))
                        throw new ConflictException("Plate already exists");
                });

        User user = userRepository.findById(vehicleDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        vehicle.setPlate(vehicleDTO.getPlate());
        vehicle.setUser(user);

        return vehicleMapper.toDto(vehicleRepository.save(vehicle));
    }

    @Transactional
    public void deleteVehicle(Long id) {
        if (!vehicleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Vehicle not found");
        }
        vehicleRepository.deleteById(id);
    }
}
