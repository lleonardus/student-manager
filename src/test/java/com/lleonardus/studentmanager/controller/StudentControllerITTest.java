package com.lleonardus.studentmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lleonardus.studentmanager.dto.PhoneInsertDTO;
import com.lleonardus.studentmanager.dto.StudentInsertDTO;
import com.lleonardus.studentmanager.dto.StudentUpdateDTO;
import com.lleonardus.studentmanager.factory.StudentFactory;
import com.lleonardus.studentmanager.service.StudentService;
import com.lleonardus.studentmanager.service.exceptions.DataIntegrityViolationException;
import com.lleonardus.studentmanager.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class StudentControllerITTest {

    @MockBean
    StudentService service;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        when(service.find(StudentFactory.ID, StudentFactory.ENROLLMENT, StudentFactory.NAME, StudentFactory.LAST_NAME))
                .thenReturn(List.of(StudentFactory.createStudentGetDTO()));
        when(service.findByEnrollment(StudentFactory.ENROLLMENT)).thenReturn(StudentFactory.createStudentGetDTO());
        when(service.insert(any())).thenReturn(StudentFactory.createStudentGetDTO());
        when(service.update(any(), any())).thenReturn(StudentFactory.createStudentGetDTO());
        doNothing().when(service).deleteById(StudentFactory.ID);
    }

    @Test
    void findAll_Returns200() throws Exception{
        mockMvc.perform(get("/students").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findById_WhenStudentIsFound_Returns200() throws Exception{
        mockMvc.perform(get("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void findByEnrollment_WhenStudentIsNotFound_Returns404() throws Exception{
        when(service.findByEnrollment(StudentFactory.ENROLLMENT)).thenThrow(ObjectNotFoundException.class);

        mockMvc.perform(get("/students/{enrollment}", StudentFactory.ENROLLMENT)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WhenSuccessful_Returns200() throws Exception{
        String json = objectMapper.writeValueAsString(StudentFactory.createStudentInsertDTO());

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(header().exists("Location"))
                .andExpect(status().isCreated());

    }

    @Test
    void create_WhenRegisterIsNotUnique_Returns400() throws Exception{
        when(service.insert(any())).thenThrow(DataIntegrityViolationException.class);
        String json = objectMapper.writeValueAsString(StudentFactory.createStudentInsertDTO());

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenNameIsNull_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setName(null);

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());

    }
    @Test
    void create_WhenNameHasInvalidLength_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setName("123");

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenLastNameIsNull_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setLastName(null);

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenLastNameHasInvalidLength_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setLastName("123");

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenEnrollmentIsNull_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setEnrollment(null);

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void create_WhenEnrollmentHasInvalidLength_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setEnrollment("123");

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WhenPhoneNumberIsNull_Returns400() throws Exception{
        StudentInsertDTO studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentInsertDTO.setPhones(Set.of(new PhoneInsertDTO()));

        String json = objectMapper.writeValueAsString(studentInsertDTO);

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenSuccessful_Returns200() throws Exception{
        String json = objectMapper.writeValueAsString(StudentFactory.createStudentInsertDTO());

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void update_WhenRegisterIsNotUnique_Returns400() throws Exception{
        when(service.update(any(), any())).thenThrow(DataIntegrityViolationException.class);
        String json = objectMapper.writeValueAsString(StudentFactory.createStudentInsertDTO());

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenNameIsNull_Returns400() throws Exception{
        StudentUpdateDTO studentUpdateDTO = StudentFactory.createStudentUpdateDTO();
        studentUpdateDTO.setName(null);

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void update_WhenNameHasInvalidLength_Returns400() throws Exception{
        StudentUpdateDTO studentUpdateDTO = StudentFactory.createStudentUpdateDTO();
        studentUpdateDTO.setName("123");

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenLastNameIsNull_Returns400() throws Exception{
        StudentUpdateDTO studentUpdateDTO = StudentFactory.createStudentUpdateDTO();
        studentUpdateDTO.setLastName(null);

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void update_WhenLastNameHasInvalidLength_Returns400() throws Exception{
        StudentUpdateDTO studentUpdateDTO = StudentFactory.createStudentUpdateDTO();
        studentUpdateDTO.setLastName("123");

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_WhenEnrollmentIsNull_Returns400() throws Exception{
        StudentUpdateDTO studentUpdateDTO = StudentFactory.createStudentUpdateDTO();
        studentUpdateDTO.setEnrollment(null);

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
    @Test
    void update_WhenEnrollmentHasInvalidLength_Returns400() throws Exception{
        StudentUpdateDTO studentUpdateDTO = StudentFactory.createStudentUpdateDTO();
        studentUpdateDTO.setEnrollment("123");

        String json = objectMapper.writeValueAsString(studentUpdateDTO);

        mockMvc.perform(put("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteById_WhenStudentIsFound_Returns200() throws Exception{
        mockMvc.perform(delete("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteById_WhenStudentIsNotFound_Returns404() throws Exception{
        doThrow(ObjectNotFoundException.class).when(service).deleteById(any());

        mockMvc.perform(delete("/students/{id}", StudentFactory.ID)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}