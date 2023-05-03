package ru.hogwarts.school.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.hogwarts.school.config.DockerConfig;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Testcontainers
public class StudentControllerTest extends DockerConfig {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private StudentRepository studentRepository;

    private final Faculty faculty = new Faculty();
    private final JSONObject jsonObject = new JSONObject();
    private final Student student = new Student();

    @BeforeEach
    void setUp() throws JSONException {
        faculty.setName("Slytherin");
        faculty.setColor("green");
        facultyRepository.save(faculty);

        student.setName("Germiona");
        student.setAge(19);
        student.setFaculty(faculty);
        studentRepository.save(student);


    }

    @Test
    void testCreateStudent() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(post("/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonObject.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Sedrik"))
                .andExpect(jsonPath("$.age").value(18))
                .andExpect(jsonPath("$.facultyId").value(faculty.getId()));

        mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[1].name").value("Sedrik"))
                .andExpect(jsonPath("$[1].age").value(18))
                .andExpect(jsonPath("$[1].facultyId").value(faculty.getId()));
    }

    @Test
    void testUpdateStudent() throws Exception {
        jsonObject.put("id",1);
        jsonObject.put("name", "Ron");
        jsonObject.put("age", 22);
        jsonObject.put("facultyId", faculty.getId());

            jsonObject.put("id",2);
            mockMvc.perform(put("/students")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(jsonObject.toString()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").isNotEmpty())
                    .andExpect(jsonPath("$.id").isNumber())
                    .andExpect(jsonPath("$.name").value("Ron"))
                    .andExpect(jsonPath("$.age").value(22))
                    .andExpect(jsonPath("$.facultyId").value(faculty.getId()));

        }

    @Test
    void testUpdateStudentWhenStudentNotFoundReturnsBadRequest() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        studentRepository.delete(student);
        mockMvc.perform(put("/students"))
                .andExpect(status().isBadRequest());
    }
    @Test
    void testDeleteStudent() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(delete("/students/" + student.getId()))
                .andExpect(status().isOk());
        mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void testGetStudent() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(get("/students/" + student.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Germiona"))
                .andExpect(jsonPath("$.age").value(19))
                .andExpect(jsonPath("$.facultyId").value(faculty.getId()));
    }
    @Test
    void testGetStudentsGetsListOfStudents() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(get("/students?pageNumber=1&pageSize=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }
    @Test
    void testGetStudentsReturnsStudentsByMinAge() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(get("/students/sorted/studentByMinAge"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value(student.getName()))
                .andExpect(jsonPath("$[0].age").value(student.getAge()));
    }
    @Test
    void testGetFacultyByStudentIdReturnsFaculty() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(get("/students/" + student.getId() + "/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Slytherin"))
                .andExpect(jsonPath("$.color").value("green"))
                .andExpect(jsonPath("$.id").value(faculty.getId()));
    }

    @Test
    void testGetTotalStudentCountReturnsNumberOfStudents() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        when(Mockito.mock(StudentRepository.class).numberOfStudents()).thenReturn(1L);
        mockMvc.perform(get("/students/numberOfStudents"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("1"));
    }

    @Test
    void testGetAverageAgeReturnsStudentsByAverageAge() throws Exception {
        jsonObject.put("name", "Sedrik");
        jsonObject.put("age", 18);
        jsonObject.put("facultyId", faculty.getId());

        mockMvc.perform(get("/students/sorted/studentsByAverageAge"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string(studentRepository.sortingStudentsByAverageAge().toString()));
    }
}

