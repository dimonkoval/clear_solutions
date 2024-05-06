package org.example.service.Impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.exception.CreateUserException;
import org.example.exception.InvalidDateRangeException;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {
    private final static Long USER_ID = 1L;
    private final static int MIN_AGE = 20;
    private final static int MIN_AGE_TEST = 40;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private UserServiceImpl userService;
    private UserResponseDto expectedUserResponseDto;
    private UserRequestDto expectedUserRequestDto;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        userService.setMinAge(MIN_AGE);
        expectedUserResponseDto = new UserResponseDto();
        expectedUserResponseDto.setId(USER_ID);
        expectedUserResponseDto.setEmail("test@gmail.com");
        expectedUserResponseDto.setAddress("test");
        expectedUserResponseDto.setFirstName("Joe");
        expectedUserResponseDto.setLastName("Doe");
        expectedUserResponseDto.setPhoneNumber("(111)111-11-11");
        expectedUserResponseDto.setBirthDate(LocalDate.parse("1900-01-01"));
        expectedUser = new User();
        expectedUser.setId(USER_ID);
        expectedUser.setEmail("test@gmail.com");
        expectedUser.setAddress("response");
        expectedUser.setFirstName("Joe");
        expectedUser.setLastName("Doe");
        expectedUser.setPhoneNumber("(111)111-11-11");
        expectedUser.setBirthDate(LocalDate.parse("1900-01-01"));
        expectedUserRequestDto = new UserRequestDto();
        expectedUserRequestDto.setEmail("test@gmail.com");
        expectedUserRequestDto.setAddress("request");
        expectedUserRequestDto.setFirstName("Joe");
        expectedUserRequestDto.setLastName("Doe");
        expectedUserRequestDto.setPhoneNumber("(111)111-11-11");
        expectedUserRequestDto.setBirthDate(LocalDate.parse("1900-01-01"));
    }

    @Test
    void getMinAge_Ok() {
        userService.setMinAge(MIN_AGE_TEST);
        assertEquals(MIN_AGE_TEST, userService.getMinAge());
    }

    @Test
    void findById_Ok() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(expectedUser));
        when(userMapper.toDto(expectedUser)).thenReturn(expectedUserResponseDto);
        assertEquals(expectedUserResponseDto, userService.findById(USER_ID));
    }

    @Test
    void create_Ok() {
        when(userMapper.toModel(expectedUserRequestDto)).thenReturn(expectedUser);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userMapper.toDto(expectedUser)).thenReturn(expectedUserResponseDto);

        UserResponseDto actualResponseDto = userService.create(expectedUserRequestDto);

        assertEquals(expectedUserResponseDto, actualResponseDto);
    }

    @Test
    void create_CheckMinAge_NotOk() {
        expectedUserRequestDto.setBirthDate(LocalDate.now());

        when(userMapper.toModel(expectedUserRequestDto)).thenReturn(expectedUser);
        when(userRepository.save(any(User.class))).thenReturn(expectedUser);
        when(userMapper.toDto(expectedUser)).thenReturn(expectedUserResponseDto);

        CreateUserException exception = assertThrows(CreateUserException.class, () -> userService.create(expectedUserRequestDto));
        assertEquals("User must be at least " + userService.getMinAge() + " years old.", exception.getMessage());
    }

    @Test
    void updateUser_Ok() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(expectedUser));
        when(userMapper.updateFromDto(expectedUserRequestDto, expectedUser)).thenReturn(expectedUser);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        when(userMapper.toDto(expectedUser)).thenReturn(expectedUserResponseDto);
        UserResponseDto actualUserResponseDto = userService.updateUser(USER_ID, expectedUserRequestDto);
        assertEquals(expectedUserResponseDto, actualUserResponseDto);
    }

    @Test
    void updateUserPartially_Ok() {

        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "update@update.com");
        updates.put("firstName", "Update");

        User updatedUser = expectedUser;
        updatedUser.setId(USER_ID);
        updatedUser.setEmail("Update@update.com");
        updatedUser.setFirstName("Update");

        UserResponseDto expectedDto = expectedUserResponseDto;
        expectedDto.setEmail("Update@update.com");
        expectedDto.setFirstName("Update");

        when(userRepository.findById(USER_ID)).thenReturn(Optional.ofNullable(expectedUser));
        when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        when(userMapper.toDto(updatedUser)).thenReturn(expectedDto);

        UserResponseDto actualUserResponseDto = userService.updateUserPartially(USER_ID, updates);
        assertEquals(expectedDto, actualUserResponseDto);
    }

    @Test
    void deleteUser_Ok() {
        when(userRepository.findById(USER_ID)).thenReturn(java.util.Optional.of(expectedUser));
        userService.deleteUser(USER_ID);

        verify(userRepository, times(1)).findById(USER_ID);
        verify(userRepository, times(1)).delete(expectedUser);
    }

    @Test
    void getUsersByBirthDateRange_Ok() {
        List<User> expectedUsers = new ArrayList<>();
        expectedUsers.add(expectedUser);
        when(userRepository.findByBirthDateBetween(LocalDate.parse("1900-01-01"), LocalDate.now()))
                .thenReturn(expectedUsers);
        when(userMapper.toDto(any(User.class))).thenReturn(expectedUserResponseDto);
        List<UserResponseDto> expectedDtoList = expectedUsers
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        List<UserResponseDto> actualListDto =
                userService.getUsersByBirthDateRange(LocalDate.parse("1900-01-01"), LocalDate.now(), false);
        assertEquals(expectedDtoList, actualListDto);
    }

    @Test
    void getUsersByBirthDateRange_ToMoreFrom_NotOk() {
        InvalidDateRangeException exception = assertThrows(InvalidDateRangeException.class, () ->
                userService.getUsersByBirthDateRange( LocalDate.now(), LocalDate.parse("1900-01-01"),false));
        assertEquals("\"From\" date must be less than \"To\" date", exception.getMessage());

    }
}