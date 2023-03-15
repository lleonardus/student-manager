package com.lleonardus.studentmanager.service;

import com.lleonardus.studentmanager.dto.StudentGetDTO;
import com.lleonardus.studentmanager.dto.StudentInsertDTO;
import com.lleonardus.studentmanager.dto.StudentUpdateDTO;
import com.lleonardus.studentmanager.model.Student;
import com.lleonardus.studentmanager.repository.StudentRepository;
import com.lleonardus.studentmanager.service.exceptions.DataIntegrityViolationException;
import com.lleonardus.studentmanager.service.exceptions.ObjectNotFoundException;
import com.lleonardus.studentmanager.specification.StudentSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository repository;

    public List<StudentGetDTO> find(Long id, String enrollment, String name, String lastName){
        Specification<Student> specification = new StudentSpec(id, enrollment, name, lastName).toSpec();

        return repository.findAll(specification).stream().map(StudentGetDTO::valueOf).toList();
    }

    public StudentGetDTO findByEnrollment(String enrollment) {
        Student student = repository.findByEnrollment(enrollment)
                .orElseThrow(() -> new ObjectNotFoundException("Student not found"));
        return StudentGetDTO.valueOf(student);
    }

    public StudentGetDTO insert(StudentInsertDTO studentInsertDTO){
        this.isEnrollmentUnique(studentInsertDTO.getEnrollment(), studentInsertDTO.getId());
        Student student = repository.save(studentInsertDTO.toStudent());

        return StudentGetDTO.valueOf(student);
    }

    public StudentGetDTO update(Long id, StudentUpdateDTO studentUpdateDTO){
        this.isEnrollmentUnique(studentUpdateDTO.getEnrollment(), id);
        Student student = findByIdOrElseThrowObjectNotFoundException(id);
        student.setName(studentUpdateDTO.getName());
        student.setLastName(studentUpdateDTO.getLastName());
        student.setEnrollment(studentUpdateDTO.getEnrollment());

        return StudentGetDTO.valueOf(repository.save(student));
    }

    public void deleteById(Long id){
        Student student = this.findByIdOrElseThrowObjectNotFoundException(id);
        repository.delete(student);
    }

    private Student findByIdOrElseThrowObjectNotFoundException(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Student not found"));
    }

    private void isEnrollmentUnique(String enrollment, Long id){
        Optional<Student> optionalStudent = repository.findByEnrollment(enrollment);

        if (optionalStudent.isPresent() && !optionalStudent.get().getId().equals(id)){
            throw new DataIntegrityViolationException("Enrollment already exists");
        }
    }
}