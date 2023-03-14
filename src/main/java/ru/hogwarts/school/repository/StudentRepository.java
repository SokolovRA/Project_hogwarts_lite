package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

public interface StudentRepository extends JpaRepository<Student,Long> {
    Collection<Student> findAllByAge(Integer ageStudent);

    Collection<Student> findByAgeBetween(Integer minAge, Integer maxAge);

    @Query(value = "SELECT count(id) FROM Student ")
    Long numberOfStudents();

    @Query(value = "SELECT AVG(age) FROM Student ")
    Long sortingStudentsByAverageAge();

    @Query(value = "SELECT * FROM Student WHERE id>0 ORDER BY age LIMIT 5",nativeQuery = true)
    Collection<Student> sortingStudentByMinAge();
}
