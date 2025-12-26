package com.zybo.parking.repository;

import com.zybo.parking.entity.Stay;
import com.zybo.parking.entity.StayStatus;
import com.zybo.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StayRepository extends JpaRepository<Stay, Long> {
    Optional<Stay> findByVehicleAndStatus(Vehicle vehicle, StayStatus status);
}
