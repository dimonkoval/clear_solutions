package org.example.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.dto.UserRequestDto;
import org.example.dto.UserResponseDto;
import org.example.service.UserService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Tag(name = "User panel manager", description = "Endpoints for user panel")
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private static final String DEFAULT_DATE_TO = "3000-01-01";
    private final UserService userService;

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public UserResponseDto getUserById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @PostMapping("/add")
    @Operation(summary = "Create user",
            description = "It allows to register users who are more than [18] years old. "
                    + "The value [18] be taken from properties file.")
    public UserResponseDto createUser(@RequestBody @Valid UserRequestDto requestDto) {
        return userService.create(requestDto);
    }

    @PatchMapping("/patch/{id}")
    @Operation(summary = "Update one/some user fields",
            description = "Update one/some user fields")
    public UserResponseDto updateUserPartially(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return userService.updateUserPartially(id, updates);
    }

    @PostMapping("/update/{id}")
    @Operation(summary = " Update all user fields",
            description = " Update all user fields")
    public UserResponseDto updateUser(@PathVariable Long id, @RequestBody @Valid UserRequestDto requestDto) {
        return userService.updateUser(id, requestDto);
    }

    @GetMapping("/filter/birthdate")
    @Operation(summary = "Search for users by birth date range and sort.",
            description = " Search for users by birth date range and sort.")
    @ApiOperation(value = "Get users by birth date range "
            + "and sorting on [parameter]:[ASC|DESC]")
    public List<UserResponseDto> getUsersByBirthDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate from,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate to,
            @RequestParam(defaultValue = "10") @ApiParam(value = "default value is '10'") Integer count,
            @RequestParam(defaultValue = "0") @ApiParam(value = "default value is '0'") Integer page,
            @RequestParam(defaultValue = "birthDate:ASC;lastName:ASC")
            @ApiParam(value = "default value is 'id'") String sortBy) {

        return userService.getUsersByBirthDateRange(from, to, count, page, sortBy);
    }
}
