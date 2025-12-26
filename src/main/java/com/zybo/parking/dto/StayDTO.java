package com.zybo.parking.dto;

import com.zybo.parking.entity.StayStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StayDTO {
    private Long id;
    private Long vehicleId;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private Long minutes;
    private Integer amountCharged;
    private StayStatus status;
}
