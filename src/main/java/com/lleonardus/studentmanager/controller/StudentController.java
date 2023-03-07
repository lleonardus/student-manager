package com.lleonardus.studentmanager.controller;

import com.lleonardus.studentmanager.controller.exceptions.StandardError;
import com.lleonardus.studentmanager.dto.StudentGetDTO;
import com.lleonardus.studentmanager.dto.StudentInsertDTO;
import com.lleonardus.studentmanager.dto.StudentUpdateDTO;
import com.lleonardus.studentmanager.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/students")
@Tag(name = "Student")
@RequiredArgsConstructor
public class StudentController {
    final StudentService service;

    @GetMapping
    @Operation(summary = "List students based on passed parameters",
    parameters = {
            @Parameter(name = "id", description = "Student id"),
            @Parameter(name = "enrollment", description = "Student enrollment"),
            @Parameter(name = "name", description = "Student name"),
            @Parameter(name = "lastName", description = "Student last name")
    })
    public ResponseEntity<List<StudentGetDTO>> find(@RequestParam(required = false) Long id,
                                                    @RequestParam(required = false) String enrollment,
                                                    @RequestParam(required = false) String name,
                                                    @RequestParam(required = false) String lastName){

        return ResponseEntity.ok().body(service.find(id, enrollment, name, lastName));
    }

    @GetMapping("/{enrollment}")
    @Operation(summary = "Get student by enrollment",
            parameters = @Parameter(name = "enrollment", description = "enrollment"),
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StudentGetDTO.class))),
                    @ApiResponse(responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StandardError.class))),
            })
    public ResponseEntity<StudentGetDTO> findByEnrollment(@PathVariable String enrollment){
        StudentGetDTO student = service.findByEnrollment(enrollment);

        return ResponseEntity.ok().body(student);
    }

    @PostMapping
    @Operation(summary = "Create student",
            responses = {
                    @ApiResponse(responseCode = "201",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StudentGetDTO.class))),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StandardError.class))),
            })
    public ResponseEntity<StudentGetDTO> insert(@RequestBody @Valid StudentInsertDTO studentInsertDTO){
        StudentGetDTO student = service.insert(studentInsertDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{enrollment}").buildAndExpand(student.getEnrollment()).toUri();

        return ResponseEntity.created(uri).body(student);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student",
            parameters = @Parameter(name = "id", description = "Student id"),
            responses = {
                    @ApiResponse(responseCode = "200",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StudentGetDTO.class))),
                    @ApiResponse(responseCode = "400",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StandardError.class))),
                    @ApiResponse(responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StandardError.class)))
            })
    public ResponseEntity<StudentGetDTO> update(@PathVariable Long id,
                                                @RequestBody @Valid StudentUpdateDTO studentUpdateDTO){
        StudentGetDTO student = service.update(id, studentUpdateDTO);

        return ResponseEntity.ok().body(student);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student by id",
            parameters = @Parameter(name = "id", description = "Student id"),
            responses = {
                    @ApiResponse(responseCode = "204", content = @Content),
                    @ApiResponse(responseCode = "404",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = StandardError.class))),
            })
    public ResponseEntity<StudentGetDTO> deleteById(@PathVariable Long id){
        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}