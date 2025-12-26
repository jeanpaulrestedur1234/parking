package com.zybo.parking.mapper;

import com.zybo.parking.dto.StayDTO;
import com.zybo.parking.entity.Stay;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StayMapper {
    @Mapping(source = "vehicle.id", target = "vehicleId")
    StayDTO toDto(Stay entity);
    
    @Mapping(source = "vehicleId", target = "vehicle.id")
    Stay toEntity(StayDTO dto);
}
