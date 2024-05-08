package org.example.mapper;

import org.example.config.MapperConfig;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    @Mapping(source = "id", target = "id")
    UserResponseDto toDto(User user);

    User toModel(UserRequestDto requestDto);

    User updateFromDto(UserRequestDto dto, @MappingTarget User user);
}
