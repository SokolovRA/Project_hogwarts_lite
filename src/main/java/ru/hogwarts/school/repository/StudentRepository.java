package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface StudentRepository extends JpaRepository<Student,Long> {
    List <Student> findByAge( int ageStudent);
}
