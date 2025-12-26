package com.zybo.parking.controller;

import com.zybo.parking.dto.CheckInRequest;
import com.zybo.parking.dto.StayDTO;
import com.zybo.parking.service.StayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stays")
@RequiredArgsConstructor
@Tag(name = "Stays", description = "Parking stay management (Check-in/Check-out)")
public class StayController {
    private final StayService stayService;

    @PostMapping("/check-in")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Register vehicle entry")
    public StayDTO checkIn(@Valid @RequestBody CheckInRequest request) {
        return stayService.checkIn(request);
    }

    @PostMapping("/{id}/check-out")
    @Operation(summary = "Register vehicle exit and calculate fee")
    public StayDTO checkOut(@PathVariable Long id) {
        return stayService.checkOut(id);
    }
}
