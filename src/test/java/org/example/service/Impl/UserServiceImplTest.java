package org.example.service.Impl;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import static org.mockito.ArgumentMatchers.any;
import org.example.controller.UserController;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.mapper.UserMapper;
import org.example.model.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserServiceImplTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private UserService userService;

    @MockBean
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;
    private UserResponseDto expectedUserResponseDto;
    private UserRequestDto expectedUserRequestDto;
    private User expectedUser;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        expectedUserResponseDto = new UserResponseDto();
        expectedUserResponseDto.setId(1L);
        expectedUserResponseDto.setEmail("test@gmail.com");
        expectedUserResponseDto.setAddress("test");
        expectedUserResponseDto.setFirstName("Joe");
        expectedUserResponseDto.setLastName("Doe");
        expectedUserResponseDto.setPhoneNumber("(111)111-11-11");
        expectedUserResponseDto.setBirthDate(LocalDate.parse("1900-01-01"));
        expectedUser = new User();
        expectedUser.setId(1L);
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
        assertEquals(18, userServiceImpl.getMinAge());
    }

    @Test
    void findById_Ok() {
        when(userService.findById(1L)).thenReturn(expectedUserResponseDto);
        assertEquals(expectedUserResponseDto, userService.findById(1L));
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
    void create_CheckMinAge_Ok() {
        expectedUserRequestDto.setBirthDate(LocalDate.now());

        when(userMapper.toModel(expectedUserRequestDto)).thenReturn(expectedUser);
        when(userRepository.save(expectedUser)).thenReturn(expectedUser);
        when(userMapper.toDto(expectedUser)).thenReturn(expectedUserResponseDto);

        UserResponseDto actualResponseDto = userService.create(expectedUserRequestDto);

        assertEquals(expectedUserResponseDto, actualResponseDto);
    }

    @Test
    void updateUser_Ok() {
        when(userService.findById(1L)).thenReturn(expectedUserResponseDto);
        when(userMapper.updateFromDto(expectedUserRequestDto, expectedUser)).thenReturn(expectedUser);
        when(userMapper.toDto(expectedUser)).thenReturn(expectedUserResponseDto);
    }

    @Test
    void deleteUser() {
    }

    @Test
    void getUsersByBirthDateRange() {
    }

    @Test
    void updateUserPartially() {
    }
}