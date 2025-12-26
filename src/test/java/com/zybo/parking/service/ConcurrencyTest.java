package com.zybo.parking.service;

import com.zybo.parking.dto.CheckInRequest;
import com.zybo.parking.entity.User;
import com.zybo.parking.entity.Vehicle;
import com.zybo.parking.exception.ConflictException;
import com.zybo.parking.repository.StayRepository;
import com.zybo.parking.repository.UserRepository;
import com.zybo.parking.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ConcurrencyTest {

    @Autowired
    private StayService stayService;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StayRepository stayRepository;

    private Long vehicleId;

    @BeforeEach
    void setUp() {
        stayRepository.deleteAll();
        vehicleRepository.deleteAll();
        userRepository.deleteAll();

        User user = userRepository.save(User.builder().name("Test").document("123").phone("456").build());
        Vehicle vehicle = vehicleRepository.save(Vehicle.builder().plate("ABC-123").user(user).build());
        vehicleId = vehicle.getId();
    }

    @Test
    void testConcurrentCheckIn() throws InterruptedException {
        int threads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        CheckInRequest request = new CheckInRequest(vehicleId);

        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    stayService.checkIn(request);
                    successCount.incrementAndGet();
                } catch (ConflictException e) {
                    failureCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(1, successCount.get(), "Only one check-in should succeed");
        assertEquals(1, failureCount.get(), "One check-in should fail with ConflictException");
    }

    @Test
    void testConcurrentCheckOut() throws InterruptedException {
        // First, check-in
        var stayDto = stayService.checkIn(new CheckInRequest(vehicleId));
        Long stayId = stayDto.getId();

        int threads = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threads);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        for (int i = 0; i < threads; i++) {
            executor.execute(() -> {
                try {
                    latch.await();
                    stayService.checkOut(stayId);
                    successCount.incrementAndGet();
                } catch (ConflictException | ObjectOptimisticLockingFailureException e) {
                    failureCount.incrementAndGet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        latch.countDown();
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        assertEquals(1, successCount.get(), "Only one check-out should succeed");
        assertEquals(1, failureCount.get(), "One check-out should fail (Optimistic lock or Business rule)");
    }
}
