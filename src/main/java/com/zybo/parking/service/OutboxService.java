package com.zybo.parking.service;

import com.zybo.parking.entity.EventStatus;
import com.zybo.parking.entity.OutboxEvent;
import com.zybo.parking.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxService {
    private final OutboxEventRepository outboxEventRepository;

    @Transactional
    public void saveEvent(String type, String payload) {
        OutboxEvent event = OutboxEvent.builder()
                .eventType(type)
                .payload(payload)
                .status(EventStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();
        outboxEventRepository.save(event);
    }

    @Transactional
    public List<OutboxEvent> dispatchEvents() {
        List<OutboxEvent> pendingEvents = outboxEventRepository.findByStatus(EventStatus.PENDING);
        pendingEvents.forEach(event -> {
            log.info("Dispatching event: {} with payload: {}", event.getEventType(), event.getPayload());
            event.setStatus(EventStatus.SENT);
        });
        return outboxEventRepository.saveAll(pendingEvents);
    }
}
