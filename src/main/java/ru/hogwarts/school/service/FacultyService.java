package ru.hogwarts.school.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
@Service
public class FacultyService {
   private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public FacultyDTO createFaculty(FacultyDTO facultyDTO){
        log.info("Was invoked method for create faculty");
        Faculty faculty = facultyDTO.toFaculty();
        Faculty facultyCreated = facultyRepository.save(faculty);
        log.info("New faculty has been created");
        return FacultyDTO.fromFaculty(facultyCreated);
    }
    public FacultyDTO getFacultyById(Long id) {
        log.info("Was invoked method for getById faculty: " +id);
        return FacultyDTO.fromFaculty(facultyRepository.findById(id).get());
    }
    public FacultyDTO updateFaculty(FacultyDTO facultyDTO) {
        log.info("Was invoked method for update faculty");
        Faculty faculty = facultyDTO.toFaculty();
        Faculty facultyUpdated = facultyRepository.save(faculty);
        log.info("Faculty has been updated");
        return FacultyDTO.fromFaculty(facultyUpdated);
    }
    public void deleteFaculty(Long id) {
        log.info("Was invoked method for delete faculty: " + id);
        facultyRepository.deleteById(id);
        log.info("Faculty with id: "+id+" has been deleted");
    }
    public Collection<FacultyDTO> findByColor(String color) {
        log.info("Was invoked method for findByColor faculty: " + color);
        return facultyRepository.findByColor(color).stream().map(FacultyDTO::fromFaculty).collect(Collectors.toList());
    }
    public Collection <FacultyDTO> getAllFaculties() {
        log.info("Was invoked method for getAll faculty");
        return facultyRepository.findAll().stream().map(FacultyDTO::fromFaculty).collect(Collectors.toList());
    }
    public  Collection<FacultyDTO> findAllByNameFaculties(String nameFaculty){
        log.info("Was invoked method for findAllByName faculty: "+  nameFaculty);
        return facultyRepository.findAllByNameIgnoreCase(nameFaculty).stream().map(FacultyDTO::fromFaculty).collect(Collectors.toList());
    }
    public Collection<StudentDTO> getAllStudentsByFacultyId(Long id) {
        log.info("Was invoked method for getAllStudentsByFacultyId faculty: "+  id);
        List<Student> students = facultyRepository.findById(id).get().getStudentsList();
        List<StudentDTO> studentsDTO = new ArrayList<>();
        for(Student student : students) {
            StudentDTO studentDTO = StudentDTO.fromStudent(student);
            studentsDTO.add(studentDTO);
        }
        return studentsDTO;
    }
}
