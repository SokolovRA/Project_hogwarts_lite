package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/students")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("{id}")
    public ResponseEntity<StudentDTO> getStudentById(@PathVariable Long id) {
        StudentDTO studentDTO = studentService.getStudentById(id);
        if (studentDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentDTO);
    }

    @PostMapping
    public ResponseEntity<StudentDTO> createStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO studentCreated = studentService.createStudent(studentDTO);
        return ResponseEntity.ok(studentCreated);
    }

    @PatchMapping
    public ResponseEntity<StudentDTO> updateStudent(@RequestBody StudentDTO studentDTO) {
        StudentDTO updatedStudent = studentService.updateStudent(studentDTO);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    @GetMapping
    public ResponseEntity<Collection<StudentDTO>> getStudents(@RequestParam(required = false) Integer ageStudent,@RequestParam(required = false) Integer minAge,@RequestParam(required = false) Integer maxAge ){
        if(ageStudent != null) {
          return   ResponseEntity.ok(studentService.findByAge(ageStudent));
        }
        if (minAge!=null && maxAge!=null){
          return   ResponseEntity.ok(studentService.sortedAgeStudent(minAge,maxAge));
        }
        return ResponseEntity.ok(studentService.findAllStudets());
      }
    @GetMapping("{studentId}/faculty")
    public ResponseEntity<FacultyDTO> getFacultyByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentService.getFacultyByStudentId(studentId));
    }
   }
