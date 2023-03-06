package com.lleonardus.studentmanager.service;

import com.lleonardus.studentmanager.dto.StudentGetDTO;
import com.lleonardus.studentmanager.dto.StudentInsertDTO;
import com.lleonardus.studentmanager.dto.StudentUpdateDTO;
import com.lleonardus.studentmanager.factory.StudentFactory;
import com.lleonardus.studentmanager.model.Student;
import com.lleonardus.studentmanager.repository.StudentRepository;
import com.lleonardus.studentmanager.service.exceptions.DataIntegrityViolationException;
import com.lleonardus.studentmanager.service.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class StudentServiceTest {
    @InjectMocks
    StudentService service;

    @Mock
    StudentRepository repository;

    private Student student;
    private StudentGetDTO studentGetDTO;
    private StudentInsertDTO studentInsertDTO;
    private StudentUpdateDTO studentUpdateDTO;


    @BeforeEach
    void setUp() {
        student = StudentFactory.createStudent();
        studentGetDTO = StudentFactory.createStudentGetDTO();
        studentInsertDTO = StudentFactory.createStudentInsertDTO();
        studentUpdateDTO = StudentFactory.createStudentUpdateDTO();

        when(repository.findAll(any(Specification.class))).thenReturn(List.of(student));
        when(repository.findById(StudentFactory.ID)).thenReturn(Optional.of(student));
        when(repository.save(student)).thenReturn(student);
        when(repository.findByEnrollment(StudentFactory.ENROLLMENT)).thenReturn(Optional.of(student));

        doNothing().when(repository).delete(student);
    }

    @Test
    @DisplayName("find returns a list of students")
    void find_ReturnsAListOfStudents() {
        List<StudentGetDTO> response = service.find(StudentFactory.ID, StudentFactory.ENROLLMENT, StudentFactory.NAME,
                StudentFactory.LAST_NAME);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertNotNull(response.get(0));
        assertEquals(StudentGetDTO.valueOf(student), response.get(0));
    }

    @Test
    @DisplayName("findByEnrollment, when student is found, returns a student")
    void findByEnrollment_WhenStudentIsFound_ReturnsAStudent() {
        StudentGetDTO response = service.findByEnrollment(StudentFactory.ENROLLMENT);

        assertNotNull(response);
        assertEquals(studentGetDTO, response);
    }

    @Test
    @DisplayName("findByEnrollment, when student is not found, throws an ObjectNotFoundException")
    void findByEnrollment_WhenStudentIsNotFound_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.findByEnrollment(StudentFactory.ENROLLMENT + "s"));
    }

    @Test
    @DisplayName("insert, when successful, returns a student")
    void insert_WhenSuccessful_ReturnsAStudent() {
        StudentGetDTO response = service.insert(studentInsertDTO);

        assertEquals(student, studentInsertDTO.toStudent());
        assertNotNull(response);
        assertEquals(studentGetDTO, response);
    }

    @Test
    @DisplayName("insert, when register is not unique, throws a DataIntegrityViolationException")
    void insert_WhenRegisterIsNotUnique_ThrowsADataIntegrityViolationException() {
        student.setId(StudentFactory.ID + 1);

        assertThrows(DataIntegrityViolationException.class, () -> service.insert(studentInsertDTO));
    }

    @Test
    @DisplayName("update, when successful, returns a student")
    void update_WhenSuccessful_ReturnsAStudent() {
        studentUpdateDTO.setName("new name");
        studentUpdateDTO.setLastName("new last name");
        studentUpdateDTO.setEnrollment("new enrollment");

        StudentGetDTO response = service.update(StudentFactory.ID, studentUpdateDTO);

        assertNotNull(response);
        assertEquals(StudentFactory.ID, response.getId());
        assertEquals("new name", response.getName());
        assertEquals("new last name", response.getLastName());
        assertEquals("new enrollment", response.getEnrollment());
    }

    @Test
    @DisplayName("update, when register is not unique, throws a DataIntegrityViolationException")
    void update_WhenRegisterIsNotUnique_ThrowsADataIntegrityViolationException() {
        student.setId(StudentFactory.ID + 1);

        assertThrows(DataIntegrityViolationException.class, () -> service.update(StudentFactory.ID, studentUpdateDTO));
    }

    @Test
    @DisplayName("deleteById, when student is found, does nothing")
    void deleteById_WhenStudentIsFound_DoesNothing() {
        service.deleteById(StudentFactory.ID);
        verify(repository, times(1)).delete(student);
    }

    @Test
    @DisplayName("deleteById, when student is not found, throws an ObjectNotFoundException")
    void deleteById_WhenStudentIsNotFound_ThrowsAnObjectNotFoundException() {
        assertThrows(ObjectNotFoundException.class, () -> service.deleteById(StudentFactory.ID + 1));
    }
}