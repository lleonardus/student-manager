package com.lleonardus.studentmanager.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneInsertDTO {

    @NotBlank(message = "phone number cannot be blank")
    private String number;
}
