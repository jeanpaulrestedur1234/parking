package com.zybo.parking.controller;

import com.zybo.parking.entity.OutboxEvent;
import com.zybo.parking.service.OutboxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Outbox event management")
public class OutboxController {
    private final OutboxService outboxService;

    @PostMapping("/dispatch")
    @Operation(summary = "Dispatch pending outbox events")
    public List<OutboxEvent> dispatchEvents() {
        return outboxService.dispatchEvents();
    }
}
