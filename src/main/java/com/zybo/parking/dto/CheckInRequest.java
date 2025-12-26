package com.zybo.parking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInRequest {
    @NotNull(message = "vehicleId is required")
    private Long vehicleId;
}
