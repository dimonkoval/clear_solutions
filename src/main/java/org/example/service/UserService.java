package org.example.service;

import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserResponseDto findById(Long id);

    UserResponseDto create(UserRequestDto requestDto);

    UserResponseDto updateUserPartially(Long id, Map<String, Object> updates);

    UserResponseDto updateUser(Long id, UserRequestDto requestDto);

    void deleteUser(Long id);

    List<UserResponseDto> getUsersByBirthDateRange(LocalDate from, LocalDate to,
                                                   Integer count, Integer page, String sortBy);
}
