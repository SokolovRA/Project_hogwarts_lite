package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable long id) {
        Student student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> updateupdateStudent(@RequestBody Student student) {
        Student foundStudent = studentService.updateStudent(student);
        if (foundStudent == null){
            return ResponseEntity.notFound().build();
    }
      return ResponseEntity.ok(foundStudent);
}
    @DeleteMapping("{id}")
    public Student deleteStudent(@PathVariable long id){
        return studentService.deleteStudent(id);
    }
    @GetMapping
    public ResponseEntity<Collection<Student>> filterAgeStudent(@RequestParam int ageStudent){
        return ResponseEntity.ok(studentService.getAllStudent().stream().filter(student -> student.getAge()==ageStudent).collect(Collectors.toList()));

    }
}
