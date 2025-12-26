package com.zybo.parking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleDTO {
    private Long id;
    
    @NotBlank(message = "Plate is required")
    private String plate;
    
    @NotNull(message = "UserId is required")
    private Long userId;
}
