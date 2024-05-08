package org.example.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Data;

@Data
public class UserRequestDto {
    @Email(message = "Invalid email")
    private String email;
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Only Latin letters, digits, hyphens, and spaces are allowed")
    private String firstName;
    @Pattern(regexp = "^[a-zA-Z0-9\\s-]+$", message = "Only Latin letters, digits, hyphens, and spaces are allowed")
    private String lastName;
    @Past(message = "Date must be in the past")
    private LocalDate birthDate;
    private boolean isDeleted = false;
    @Size(min = 0, max = 255, message = "maximum number of characters cannot exceed 255")
    private String address;
    @Pattern(regexp = "^\\(\\d{3}\\)\\d{3}-\\d{2}-\\d{2}$", message = "Phone number must be in format (XXX)XXX-XX-XX")
    private String phoneNumber;
}
