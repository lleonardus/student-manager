package com.lleonardus.studentmanager.repository;

import com.lleonardus.studentmanager.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends
        JpaRepository<Student, Long>, JpaSpecificationExecutor<Student> {
    Optional<Student> findByEnrollment(String enrollment);
}
