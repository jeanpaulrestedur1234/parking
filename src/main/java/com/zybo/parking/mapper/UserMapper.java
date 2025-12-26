package com.zybo.parking.mapper;

import com.zybo.parking.dto.UserDTO;
import com.zybo.parking.dto.UserRequest;
import com.zybo.parking.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "vehicles", ignore = true)
    User toEntity(UserDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    User toEntity(UserRequest request);

    UserDTO toDto(User entity);
}
