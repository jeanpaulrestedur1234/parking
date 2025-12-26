package com.zybo.parking.mapper;

import com.zybo.parking.dto.VehicleDTO;
import com.zybo.parking.dto.VehicleRequest;
import com.zybo.parking.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {
    @Mapping(source = "user.id", target = "userId")
    VehicleDTO toDto(Vehicle entity);

    @Mapping(source = "userId", target = "user.id")
    Vehicle toEntity(VehicleDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "userId", target = "user.id")
    Vehicle toEntity(VehicleRequest dto);
}
