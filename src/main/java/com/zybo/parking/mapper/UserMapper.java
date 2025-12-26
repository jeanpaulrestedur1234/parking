package com.zybo.parking.mapper;

import com.zybo.parking.dto.UserDTO;
import com.zybo.parking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "vehicles", ignore = true)
    User toEntity(UserDTO dto);
    
    UserDTO toDto(User entity);
}
