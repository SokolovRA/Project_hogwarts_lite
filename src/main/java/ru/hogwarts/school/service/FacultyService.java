package ru.hogwarts.school.service;

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

@Service
public class FacultyService {
   private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public FacultyDTO createFaculty(FacultyDTO facultyDTO){
        Faculty faculty = facultyDTO.toFaculty();
        Faculty facultyCreated = facultyRepository.save(faculty);
        return FacultyDTO.fromFaculty(facultyCreated);
    }
    public FacultyDTO getFacultyById(Long id) {
        return FacultyDTO.fromFaculty(facultyRepository.findById(id).get());
    }
    public FacultyDTO updateFaculty(FacultyDTO facultyDTO) {
        Faculty faculty = facultyDTO.toFaculty();
        Faculty facultyUpdated = facultyRepository.save(faculty);
        return FacultyDTO.fromFaculty(facultyUpdated);
    }
    public void deleteFaculty(Long id) {
         facultyRepository.deleteById(id);
    }
    public Collection<FacultyDTO> findByColor(String color) {
        return facultyRepository.findByColor(color).stream().map(FacultyDTO::fromFaculty).collect(Collectors.toList());
    }
    public Collection <FacultyDTO> getAllFaculties() {
        return facultyRepository.findAll().stream().map(FacultyDTO::fromFaculty).collect(Collectors.toList());
    }
    public  Collection<FacultyDTO> findAllByNameFaculties(String nameFaculty){
        return facultyRepository.findAllByNameIgnoreCase(nameFaculty).stream().map(FacultyDTO::fromFaculty).collect(Collectors.toList());
    }
    public Collection<StudentDTO> getAllStudentsByFacultyId(Long id) {
        List<Student> students = facultyRepository.findById(id).get().getStudentsList();
        List<StudentDTO> studentsDTO = new ArrayList<>();
        for(Student student : students) {
            StudentDTO studentDTO = StudentDTO.fromStudent(student);
            studentsDTO.add(studentDTO);
        }
        return studentsDTO;
    }
}
