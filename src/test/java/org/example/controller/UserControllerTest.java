package org.example.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    private static final Long USER_ID = 1L;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;
    @InjectMocks
    private UserController userController;
    private UserResponseDto expectedUserResponseDto;
    private UserRequestDto expectedUserRequestDto;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
        expectedUserResponseDto = new UserResponseDto();
        expectedUserResponseDto.setId(USER_ID);
        expectedUserResponseDto.setEmail("test@gmail.com");
        expectedUserResponseDto.setAddress("test");
        expectedUserResponseDto.setFirstName("Response");
        expectedUserResponseDto.setLastName("Doe");
        expectedUserResponseDto.setPhoneNumber("(111)111-11-11");
        expectedUserResponseDto.setBirthDate(LocalDate.parse("1900-01-01"));
        expectedUserRequestDto = new UserRequestDto();
        expectedUserRequestDto.setEmail("test@gmail.com");
        expectedUserRequestDto.setAddress("test");
        expectedUserRequestDto.setFirstName("Request");
        expectedUserRequestDto.setLastName("Doe");
        expectedUserRequestDto.setPhoneNumber("(111)111-11-11");
        expectedUserRequestDto.setBirthDate(LocalDate.parse("1900-01-01"));
        expectedUserRequestDto.setDeleted(false);
    }

    @Test
    void getUserById_Ok() {
        when(userService.findById(ArgumentMatchers.eq(USER_ID))).thenReturn(expectedUserResponseDto);
        RestAssuredMockMvc.when()
                .get("/users/{id}", String.valueOf(USER_ID))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(USER_ID.intValue()))
                .body("email", Matchers.equalTo("test@gmail.com"))
                .body("firstName", Matchers.equalTo("Response"))
                .body("lastName", Matchers.equalTo("Doe"))
                .body("address", Matchers.equalTo("test"))
                .body("phoneNumber", Matchers.equalTo("(111)111-11-11"));
    }

    @Test
    void deleteUserById_Ok() throws Exception {
        doNothing().when(userService).deleteUser(ArgumentMatchers.eq(USER_ID));

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .when()
                .delete("/users/delete/{id}", String.valueOf(USER_ID))
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void createUser_Ok() {
        when(userService.create(ArgumentMatchers.any(UserRequestDto.class))).thenReturn(expectedUserResponseDto);
        RestAssuredMockMvc
                .given()
                .contentType(ContentType.JSON)
                .body(expectedUserRequestDto)
                .when()
                .post("/users/add")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(USER_ID.intValue()))
                .body("email", Matchers.equalTo("test@gmail.com"))
                .body("firstName", Matchers.equalTo("Response"))
                .body("lastName", Matchers.equalTo("Doe"))
                .body("address", Matchers.equalTo("test"))
                .body("phoneNumber", Matchers.equalTo("(111)111-11-11"));
    }

    @Test
    void updateUserPartially() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "update@gmail.com");
        updates.put("firstName", "Update");

        expectedUserResponseDto.setEmail((String) updates.get("email"));
        expectedUserResponseDto.setFirstName((String) updates.get("firstName"));

        when(userService.updateUserPartially(ArgumentMatchers.eq(USER_ID), ArgumentMatchers.anyMap()))
                .thenReturn(expectedUserResponseDto);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(updates)
                .when()
                .patch("/users/patch/{id}", String.valueOf(USER_ID))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(USER_ID.intValue()))
                .body("email", Matchers.equalTo("update@gmail.com"))
                .body("firstName", Matchers.equalTo("Update"))
                .body("lastName", Matchers.equalTo("Doe"))
                .body("address", Matchers.equalTo("test"))
                .body("phoneNumber", Matchers.equalTo("(111)111-11-11"));;
    }

    @Test
    void updateUser_Ok() {
        when(userService.updateUser(ArgumentMatchers.eq(USER_ID), ArgumentMatchers.any(UserRequestDto.class)))
                .thenReturn(expectedUserResponseDto);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(expectedUserResponseDto)
                .when()
                .post("/users/update/{id}", String.valueOf(USER_ID))
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", Matchers.equalTo(USER_ID.intValue()))
                .body("email", Matchers.equalTo("test@gmail.com"))
                .body("firstName", Matchers.equalTo("Response"))
                .body("lastName", Matchers.equalTo("Doe"))
                .body("address", Matchers.equalTo("test"))
                .body("phoneNumber", Matchers.equalTo("(111)111-11-11"));
    }

    @Test
    void getUsersByBirthDateRange_Ok() {
        List<UserResponseDto> userResponseDtos = new ArrayList<>();
        userResponseDtos.add(expectedUserResponseDto);
        when(userService.getUsersByBirthDateRange(LocalDate.parse("1900-01-01"), LocalDate.now(), false))
                .thenReturn(userResponseDtos);
        RestAssuredMockMvc.when()
                .get("/users//filter/birthdate?from=1900-01-01&to=" + LocalDate.now())
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", Matchers.equalTo(1))
                .body("[0].id", Matchers.equalTo(USER_ID.intValue()))
                .body("[0].email", Matchers.equalTo("test@gmail.com"))
                .body("[0].firstName", Matchers.equalTo("Response"))
                .body("[0].lastName", Matchers.equalTo("Doe"))
                .body("[0].address", Matchers.equalTo("test"))
                .body("[0].phoneNumber", Matchers.equalTo("(111)111-11-11"));
    }
}
