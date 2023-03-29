package ru.hogwarts.school.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.stream.Collectors;
@Slf4j
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
        log.info("Was invoked method for create student");
        Faculty faculty = facultyRepository.findById(studentDTO.getFacultyId()).get();
        Student student = studentDTO.toStudent();
        student.setFaculty(faculty);
        Student studentCreated = studentRepository.save(student);
        log.info("New student has been created");
        return StudentDTO.fromStudent(studentCreated);
    }
    public StudentDTO getStudentById(Long id) {
        log.info("Was invoked method for getById student: " +id);
        return StudentDTO.fromStudent(studentRepository.findById(id).get());
    }
    public StudentDTO updateStudent(StudentDTO studentDTO) {
        log.info("Was invoked method for update student");
        Faculty faculty = facultyRepository.findById(studentDTO.getFacultyId()).get();
        Student student = studentDTO.toStudent();
        student.setFaculty(faculty);
        Student updatedStudent = studentRepository.save(student);
        log.info("Student has been updated");
        return StudentDTO.fromStudent(updatedStudent);
    }
    public void deleteStudent(Long id) {
        log.info("Was invoked method for delete student: " + id);
        studentRepository.deleteById(id);
        log.info("Student with id: "+id+" has been deleted");
    }
    public Collection<StudentDTO> findByAge(Integer age){
        log.info("Was invoked method for findByAge student: " + age);
        return studentRepository.findAllByAge(age).stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
    public Collection<StudentDTO> findAllStudets(Integer pageNumber, Integer pageSize) {
        log.info("Was invoked method for findAll student");
        PageRequest pageRequest =PageRequest.of(pageNumber-1,pageSize);
        return studentRepository.findAll(pageRequest).getContent().stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
    public Collection<StudentDTO> sortedAgeStudent(Integer minAge,Integer maxAge){
        log.info("Was invoked method for sortedAge student");
        return studentRepository.findByAgeBetween(minAge,maxAge).stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
    public FacultyDTO getFacultyByStudentId(Long id) {
        log.info("Was invoked method for getFacultyByStudent studentId"+id);
        Faculty faculty = facultyRepository.findById(getStudentById(id).getFacultyId()).get();
        return FacultyDTO.fromFaculty(faculty);
    }
    public Student findStudentById(Long id) {
        log.info("Was invoked method for findById student"+id);
        return studentRepository.findById(id).get();
    }
    public Long getNumberOfStudents(){
        log.info("Was invoked method for getNumberOf student");
        return studentRepository.numberOfStudents();
    }
    public Long getSortingStudentsByAverageAge(){
        log.info("Was invoked method for getSortingByAverageAge student");
        return studentRepository.sortingStudentsByAverageAge();
    }
    public Collection<StudentDTO> getSortingStudentByMinAge(){
        log.info("Was invoked method for getSortingByMinAge student");
        return studentRepository.sortingStudentByMinAge().stream().map(StudentDTO::fromStudent).collect(Collectors.toList());
    }
}

