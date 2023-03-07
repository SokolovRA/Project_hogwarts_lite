package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;

@Service
public class FacultyService {
    private final HashMap<Long, Faculty> faculties = new HashMap<>();
    private long lastId = 0;

    public Faculty createFaculty(Faculty faculty){
        faculty.setId(++lastId);
        faculties.put(lastId,faculty);
        return faculty;
    }
    public Faculty getFacultyById(long id) {
        return faculties.get(id);
    }
    public Faculty updateFaculty(Faculty faculty) {
        if(faculties.containsKey(faculty.getId())) {
            faculties.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }
    public Faculty deleteFaculty(long id) {
        return faculties.remove(id);
    }
    public Collection<Faculty> getAllFaculty(){
        return faculties.values();
    }
}
