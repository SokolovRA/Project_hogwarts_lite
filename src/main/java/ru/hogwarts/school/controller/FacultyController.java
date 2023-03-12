package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.dto.FacultyDTO;
import ru.hogwarts.school.dto.StudentDTO;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping("{id}")
    public ResponseEntity<FacultyDTO> getFacultyById(@PathVariable Long id) {
        FacultyDTO facultyDTO = facultyService.getFacultyById(id);
        if (facultyDTO == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyDTO);
    }

    @PostMapping
    public ResponseEntity<FacultyDTO> createFaculty(@RequestBody FacultyDTO facultyDTO) {
        FacultyDTO facultyCreated = facultyService.createFaculty(facultyDTO);
        return ResponseEntity.ok(facultyCreated);
    }

    @PutMapping
    public ResponseEntity<FacultyDTO> updateFaculty(@RequestBody FacultyDTO facultyDTO) {
        FacultyDTO facultyUpdated = facultyService.updateFaculty(facultyDTO);
        if (facultyUpdated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(facultyUpdated);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Collection<FacultyDTO>> getFaculties(@RequestParam(required = false) String colorFaculty, @RequestParam(required = false) String nameFacylty) {
        if (colorFaculty != null && !colorFaculty.isBlank()) {
           return  ResponseEntity.ok(facultyService.findByColor(colorFaculty));
        }
        if (nameFacylty != null && !nameFacylty.isBlank()) {
           return ResponseEntity.ok(facultyService.findAllByNameFaculties(nameFacylty));
        }
        return ResponseEntity.ok(facultyService.getAllFaculties());
    }
    @GetMapping("{facultyId}/students")
    public ResponseEntity<Collection<StudentDTO>> getAllStudentsByFacultyId (@PathVariable Long facultyId) {
        return ResponseEntity.ok(facultyService.getAllStudentsByFacultyId(facultyId));
    }
}


