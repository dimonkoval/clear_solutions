package org.example.service.impl;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exception.CreateUserException;
import org.example.exception.InvalidDateException;
import org.example.exception.UserNotFoundException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.util.AgeCalculator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${user.minAge}")
    private int minAge;

    @Override
    public UserResponseDto findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User by ID " + id + " not found"));
        return userMapper.toDto(user);
    }

    @Override
    public UserResponseDto create(UserRequestDto requestDto) {
        try {
            LocalDate birthDate = requestDto.getBirthDate();
            int age = AgeCalculator.calculateAge(birthDate);
            if (age >= minAge) {
                User user = userRepository.save(userMapper.toModel(requestDto));
                return userMapper.toDto(user);
            } else {
                throw new CreateUserException("User must be at least " + minAge + " years old.");
            }
        } catch (Exception ex) {
            throw new CreateUserException(ex.getMessage());
        }
    }

    @Override
    public UserResponseDto updateUser(Long id, UserRequestDto requestDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by ID " + id));

        existingUser = userMapper.updateFromDto(requestDto, existingUser);

        return userMapper.toDto(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by ID " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserResponseDto> getUsersByBirthDateRange(LocalDate from, LocalDate to, Boolean isDescendingOrder) {

        if (from.isAfter(to)) {
            throw new InvalidDateException("\"From\" date must be less than \"To\" date");
        }
        Comparator<UserResponseDto> userComparator = Comparator.comparing(UserResponseDto::getBirthDate);
        if (isDescendingOrder) {
            userComparator = userComparator.reversed();
        }
        return userRepository.findByBirthDateBetween(from, to)
                .stream()
                .map(userMapper::toDto)
                .sorted(userComparator)
                .collect(Collectors.toList());
    }

    @Override
    public UserResponseDto updateUserPartially(Long id, Map<String, Object> updates) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found by ID " + id));

        User finalExistingUser = existingUser;
        updates.forEach((key, value) -> {
            switch (key) {
                case "email":
                    finalExistingUser.setEmail((String) value);
                    break;
                case "firstName":
                    finalExistingUser.setFirstName((String) value);
                    break;
                case "lastName":
                    finalExistingUser.setLastName((String) value);
                    break;
                case "address":
                    finalExistingUser.setAddress((String) value);
                    break;
                case "birthDate":
                    finalExistingUser.setBirthDate(LocalDate.parse(value.toString()));
                    break;
                case "phoneNumber":
                    finalExistingUser.setPhoneNumber((String) value);
                    break;
                case "deleted":
                    finalExistingUser.setDeleted((Boolean) value);
                    break;
                default:
                    throw new InvalidDateException("Error processing map: unexpected key '" + key + "'");
            }
        });
        return userMapper.toDto(userRepository.save(existingUser));
    }
}
