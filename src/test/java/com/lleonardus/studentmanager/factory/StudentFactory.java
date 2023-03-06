package com.lleonardus.studentmanager.factory;

import com.lleonardus.studentmanager.dto.*;
import com.lleonardus.studentmanager.model.Phone;
import com.lleonardus.studentmanager.model.Student;

import java.util.List;
import java.util.Set;

public class StudentFactory {
    public static final Long ID = 1L;
    public static final String NAME = "name";
    public static final String LAST_NAME = "last name";
    public static final String ENROLLMENT = "123-A";

    public static Student createStudent(){
        return Student.builder()
                .id(ID)
                .enrollment(ENROLLMENT)
                .name(NAME)
                .lastName(LAST_NAME)
                .phones(List.of(new Phone(null, "4002-8922")))
                .build();
    }

    public static StudentGetDTO createStudentGetDTO(){
        return StudentGetDTO.builder()
                .id(ID)
                .enrollment(ENROLLMENT)
                .name(NAME)
                .lastName(LAST_NAME)
                .phones(List.of(new PhoneGetDTO(null, "4002-8922")))
                .build();
    }

    public static StudentInsertDTO createStudentInsertDTO(){
        return StudentInsertDTO.builder()
                .id(ID)
                .enrollment(ENROLLMENT)
                .name(NAME)
                .lastName(LAST_NAME)
                .phones(Set.of(new PhoneInsertDTO("4002-8922")))
                .build();
    }

    public static StudentUpdateDTO createStudentUpdateDTO(){
        return StudentUpdateDTO.builder()
                .id(ID)
                .enrollment(ENROLLMENT)
                .name(NAME)
                .lastName(LAST_NAME)
                .build();
    }
}
