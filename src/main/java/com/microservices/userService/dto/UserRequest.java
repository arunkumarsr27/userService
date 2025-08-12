package com.microservices.userService.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {
    @NotBlank (message = "Name is required")
    private String name;

    @Email(message = "Email must be valid")
    @NotBlank
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    @NotBlank
    private String password;

}
