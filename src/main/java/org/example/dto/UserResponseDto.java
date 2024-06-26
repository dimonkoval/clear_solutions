package org.example.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private boolean isDeleted = false;
    private String address;
    private String phoneNumber;
}
