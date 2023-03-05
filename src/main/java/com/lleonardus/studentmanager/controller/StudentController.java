package com.lleonardus.studentmanager.controller;

import com.lleonardus.studentmanager.dto.StudentGetDTO;
import com.lleonardus.studentmanager.dto.StudentInsertDTO;
import com.lleonardus.studentmanager.dto.StudentUpdateDTO;
import com.lleonardus.studentmanager.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    final StudentService service;

    @GetMapping
    public ResponseEntity<List<StudentGetDTO>> find(@RequestParam(required = false) Long id,
                                                    @RequestParam(required = false) String enrollment,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String lastName){

        return ResponseEntity.ok().body(service.find(id, enrollment, name, lastName));
    }

    @GetMapping("/{enrollment}")
    public ResponseEntity<StudentGetDTO> findByEnrollment(@PathVariable String enrollment){
        StudentGetDTO student = service.findByEnrollment(enrollment);

        return ResponseEntity.ok().body(student);
    }

    @PostMapping
    public ResponseEntity<StudentGetDTO> insert(@RequestBody @Valid StudentInsertDTO studentInsertDTO){
        StudentGetDTO student = service.insert(studentInsertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{enrollment}").buildAndExpand(student.getEnrollment()).toUri();

        return ResponseEntity.created(uri).body(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentGetDTO> update(@PathVariable Long id,
                                                @RequestBody @Valid StudentUpdateDTO studentUpdateDTO){
        StudentGetDTO student = service.update(id, studentUpdateDTO);

        return ResponseEntity.ok().body(student);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StudentGetDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}