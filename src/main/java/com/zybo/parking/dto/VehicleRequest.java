package com.zybo.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class VehicleRequest {
    @NotBlank(message = "Plate is required")
    private String plate;

    @NotNull(message = "UserId is required")
    private Long userId;
}
