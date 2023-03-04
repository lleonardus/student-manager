package com.lleonardus.studentmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lleonardus.studentmanager.model.Phone;
import com.lleonardus.studentmanager.model.Student;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentInsertDTO {
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

    @Valid
    private Set<PhoneInsertDTO> phones;

    public Student toStudent(){
        List<Phone> phoneList = new ArrayList<>();

        for(PhoneInsertDTO p: phones){
            phoneList.add(new Phone(null, p.getNumber()));
        }

        return Student.builder()
                .id(id)
                .enrollment(enrollment)
                .name(name)
                .lastName(lastName)
                .phones(phoneList)
                .build();
    }
}
