package com.zybo.parking.repository;

import com.zybo.parking.entity.Vehicle;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    Optional<Vehicle> findByPlate(String plate);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Vehicle> findById(Long id);
}
