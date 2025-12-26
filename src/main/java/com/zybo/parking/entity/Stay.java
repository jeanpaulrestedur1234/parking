package com.zybo.parking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "stays")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "entry_time", nullable = false)
    private LocalDateTime entryTime;

    @Column(name = "exit_time")
    private LocalDateTime exitTime;

    private Long minutes;

    @Column(name = "amount_charged")
    private Integer amountCharged;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StayStatus status;

    @Version
    private Long version;
}
