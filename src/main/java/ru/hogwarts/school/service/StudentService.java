package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private FacultyRepository facultyRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }
    @Autowired
    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public StudentDTO createStudent(StudentDTO studentDTO){
        Student student = studentDTO.toStudent();
        Student studentCreated = studentRepository.save(student);
        return StudentDTO.fromStudent(studentCreated);
    }
    public StudentDTO getStudentById(Long id) {
        return StudentDTO.fromStudent(studentRepository.findById(id).get());
    }
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        Student student = studentDTO.toStudent();
        Student studentUpdated = studentRepository.save(student);
        return StudentDTO.fromStudent(studentUpdated);
    }
    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
    public Collection<StudentDTO> findByAge(Integer age){
        return studentRepository.findAllByAge(age).stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
    public Collection<StudentDTO> findAllStudets() {
        return studentRepository.findAll().stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
    public Collection<StudentDTO> sortedAgeStudent(Integer minAge,Integer maxAge){
        return studentRepository.findByAgeBetween(minAge,maxAge).stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
    public FacultyDTO getFacultyByStudentId(Long id) {
        Faculty faculty = facultyRepository.findById(getStudentById(id).getFaculty()).get();
        return FacultyDTO.fromFaculty(faculty);
    }
    public Student findStudentById(Long id) {
        return studentRepository.findById(id).get();
    }

}

