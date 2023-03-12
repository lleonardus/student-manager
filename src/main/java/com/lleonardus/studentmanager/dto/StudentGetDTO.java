package com.lleonardus.studentmanager.dto;

import com.lleonardus.studentmanager.model.Phone;
import com.lleonardus.studentmanager.model.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentGetDTO {
    private Long id;
    private String enrollment;
    private String name;
    private String lastName;
    private List<PhoneGetDTO> phones;

    public static StudentGetDTO valueOf(Student student){
        List<PhoneGetDTO> phoneGetDTOS = new ArrayList<>();

        if(student.getPhones() != null) {
            for (Phone p : student.getPhones()) {
                phoneGetDTOS.add(new PhoneGetDTO(p.getId(), p.getNumber()));
            }
        }

        return StudentGetDTO.builder()
                .id(student.getId())
                .enrollment(student.getEnrollment())
                .name(student.getName())
                .lastName(student.getLastName())
                .phones(phoneGetDTOS)
                .build();
    }
}