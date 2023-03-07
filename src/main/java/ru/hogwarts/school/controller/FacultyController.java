package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/faculties")
public class FacultyController {
    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }
    @GetMapping("{id}")
    public ResponseEntity<Faculty>  getFacultyById(@PathVariable long id){
        Faculty faculty = facultyService.getFacultyById(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }
    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty){
        return facultyService.createFaculty(faculty);
    }
    @PutMapping
    public  ResponseEntity<Faculty> updateupdateFaculty(@RequestBody Faculty faculty){
        Faculty foundFaculty = facultyService.updateFaculty(faculty);
        if (foundFaculty == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundFaculty);
    }
    @DeleteMapping("{id}")
    public Faculty deleteFaculty(@PathVariable long id){
        return facultyService.deleteFaculty(id);
    }
    @GetMapping
    public ResponseEntity<Collection<Faculty>> filterAgeFaculty(@RequestParam String colorFaculty){
        return ResponseEntity.ok(facultyService.getAllFaculty().stream().filter(faculty -> faculty.getColor().equals(colorFaculty)).collect(Collectors.toList()));

    }
}
