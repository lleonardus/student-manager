package com.lleonardus.studentmanager.specification;

import com.lleonardus.studentmanager.model.Student;
import jakarta.persistence.criteria.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
public class StudentSpec {
    private Long id;
    private String enrollment;
    private String name;
    private String lastName;

    public Specification<Student> toSpec(){
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(id != null){
                Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
                predicates.add(predicate);
            }
            if (enrollment != null){
                Predicate predicate = criteriaBuilder.equal(root.get("enrollment"), enrollment);
                predicates.add(predicate);
            }
            if(name != null){
                Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(root.get("name")),
                        name.toUpperCase());
                predicates.add(predicate);
            }
            if(lastName != null){
                Predicate predicate = criteriaBuilder.equal(criteriaBuilder.upper(root.get("lastName")),
                        lastName.toUpperCase());
                predicates.add(predicate);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}