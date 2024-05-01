package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.example.dto.UserResponseDto;
import org.example.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.assertj.core.api.Assertions.assertThat;

//import static org.junit.Assert.assertThat;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private UserController userController;
    private UserResponseDto expectedUserResponseDto;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        expectedUserResponseDto = new UserResponseDto();
        expectedUserResponseDto.setId(111L);
        expectedUserResponseDto.setEmail("test@gmail.com");
        expectedUserResponseDto.setAddress("test");
        expectedUserResponseDto.setFirstName("Joe");
        expectedUserResponseDto.setLastName("Doe");
        expectedUserResponseDto.setPhoneNumber("(111)111-11-11");
        expectedUserResponseDto.setBirthDate(LocalDate.parse("1900-01-01"));
    }

    @Test
    void getUserById_Ok() throws Exception {

//        UserResponseDto expectedUserResponseDto = new UserResponseDto();
//        expectedUserResponseDto.setId(111L);
//        expectedUserResponseDto.setEmail("test@gmail.com");
//        expectedUserResponseDto.setAddress("test");
//        expectedUserResponseDto.setFirstName("Joe");
//        expectedUserResponseDto.setLastName("Doe");
//        expectedUserResponseDto.setPhoneNumber("(111)111-11-11");
//        expectedUserResponseDto.setBirthDate(LocalDate.parse("1900-01-01"));

        when(userService.findById(111L)).thenReturn(expectedUserResponseDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        UserResponseDto userResponseDto = objectMapper.readValue(jsonResponse, UserResponseDto.class);

        assertThat(userResponseDto.getId()).isEqualTo(111L);
        assertThat(userResponseDto.getEmail()).isEqualTo("test@gmail.com");
        assertThat(userResponseDto.getFirstName()).isEqualTo("Joe");
        assertThat(userResponseDto.getLastName()).isEqualTo("Doe");
        assertThat(userResponseDto.getAddress()).isEqualTo("test");
        assertThat(userResponseDto.getBirthDate()).isEqualTo("1900-01-01");
        assertThat(userResponseDto.getPhoneNumber()).isEqualTo("(111)111-11-11");
    }

    @Test
    void deleteUserById_Ok() throws Exception {
        // Встановлення поведінки сервісу для методу deleteUser
//        when(userService.deleteUser(1L)).thenReturn(true);

        // Виклик DELETE-запиту
        mockMvc.perform(delete("/users/delete/1"))
                .andExpect(status().isOk());
    }

    @Test
    void createUser() {
    }

    @Test
    void updateUserPartially() {
    }

    @Test
    void updateUser() {
    }

    @Test
    void getUsersByBirthDateRange() {
    }
}