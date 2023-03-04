package com.lleonardus.studentmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateDTO{
    @JsonIgnore
    private Long id;

    @NotBlank(message = "The name field is required")
    @Length(min = 4, message = "name field must be at least {min} characters long")
    private String name;

    @NotBlank(message = "The lastName field is required")
    @Length(min = 4, message = "lastName field must be at least {min} characters long")
    private String lastName;

    @NotBlank(message = "The enrollment field is required")
    @Length(min = 4, message = "enrollment field must be at least {min} characters long")
    private String enrollment;
}

