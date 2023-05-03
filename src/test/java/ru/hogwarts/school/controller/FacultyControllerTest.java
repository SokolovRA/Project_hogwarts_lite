package ru.hogwarts.school.controller;


import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.hogwarts.school.config.DockerConfig;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class FacultyControllerTest extends DockerConfig {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;

    private final Faculty faculty = new Faculty();
    private final JSONObject jsonObject = new JSONObject();

    @BeforeEach
    void setUp() throws JSONException {
        faculty.setName("Slytherin");
        faculty.setColor("green");
        facultyRepository.save(faculty);

        Student student1 = new Student();
        student1.setName("Draco");
        student1.setAge(18);
        student1.setFaculty(faculty);
        studentRepository.save(student1);

        Student student2 = new Student();
        student2.setName("Goil");
        student2.setAge(17);
        student2.setFaculty(faculty);
        studentRepository.save(student2);

        List<Student> studentList = new ArrayList<>();
        studentList.add(student1);
        studentList.add(student2);
        faculty.setStudentsList(studentList);



    }

    @Test
    void testСreateFaculty() throws Exception{
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        mockMvc.perform(post("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Ravenclaw"))
                .andExpect(jsonPath("$.color").value("blue"));

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].name").value("Ravenclaw"))
                .andExpect(jsonPath("$[1].color").value("blue"));

    }
    @Test
    void testUpdateFaculty() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        jsonObject.put("name", "Griffindor");
        jsonObject.put("color", "red");

        mockMvc.perform(put("/faculties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Griffindor"))
                .andExpect(jsonPath("$.color").value("red"));

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[1].name").value("Griffindor"))
                .andExpect(jsonPath("$[1].color").value("red"));
    }

    @Test
    void testUpdateFacultyWhenFacultyNotFoundReturnsBadRequest() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        studentRepository.deleteAll();
        faculty.setStudentsList(Collections.emptyList());
        facultyRepository.delete(faculty);

        mockMvc.perform(put("/faculties"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void testDeleteFaculty() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        studentRepository.deleteAll();
        mockMvc.perform(delete("/faculties/" + faculty.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetFaculty() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        mockMvc.perform(get("/faculties/" + faculty.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("green"));
    }

    @Test
    void testGetFacultiesGetsListOfFacultiesFromDatabase() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        mockMvc.perform(get("/faculties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetFacultiesByColor() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        String color = "green";
        mockMvc.perform(get("/faculties?colorFaculty=" + color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
    }

    @Test
    void testGetFacultiesByName() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        mockMvc.perform(get("/faculties?nameFacylty=" + faculty.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(faculty.getName()))
                .andExpect(jsonPath("$[0].color").value(faculty.getColor()));
    }

    @Test
    void getAllStudentsByFacultyId() throws Exception {
        jsonObject.put("name","Ravenclaw");
        jsonObject.put("color","blue");

        mockMvc.perform(get("/faculties/" + faculty.getId() + "/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(faculty.getStudentsList().size()));
    }
}
