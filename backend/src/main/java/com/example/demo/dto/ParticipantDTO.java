package com.example.demo.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ParticipantDTO {
    private Long id;
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name can be up to 100 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email can be up to 100 characters")
    private String email;
    // Exclude 'groups' to prevent recursion
}
