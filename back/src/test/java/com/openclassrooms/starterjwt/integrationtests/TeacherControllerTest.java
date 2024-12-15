package com.openclassrooms.starterjwt.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher teacher1;
    private Teacher teacher2;
    private User adminUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        Optional<User> adminUserOptional = userRepository.findById(1L);
        Optional<Teacher> teacher1Optional = teacherRepository.findById(1L);
        Optional<Teacher> teacher2Optional = teacherRepository.findById(2L);

        adminUser = adminUserOptional.orElseThrow(() -> new IllegalStateException("Admin user not found in database"));

        teacher1 = teacher1Optional.orElseGet(() -> {
            Teacher teacher = new Teacher();
            teacher.setFirstName("Margot");
            teacher.setLastName("DELAHAYE");
            teacher.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            teacher.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            return teacherRepository.save(teacher);
        });

        teacher2 = teacher2Optional.orElseGet(() -> {
            Teacher teacher = new Teacher();
            teacher.setFirstName("Hélène");
            teacher.setLastName("THIERCELIN");
            teacher.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            teacher.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            return teacherRepository.save(teacher);
        });

        // Authentification comme adminUser, génération du token JWT et set du token dans le SecurityContext
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUser.getEmail(), "test!1234"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    @Test
    void testFindById_TeacherExists() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/teacher/1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Teacher responseTeacher = objectMapper.readValue(responseBody, Teacher.class);

        assertEquals(teacher1.getId(), responseTeacher.getId());
        assertEquals(teacher1.getFirstName(), responseTeacher.getFirstName());
        assertEquals(teacher1.getLastName(), responseTeacher.getLastName());
        assertEquals(teacher1.getCreatedAt(), responseTeacher.getCreatedAt());
        assertEquals(teacher1.getUpdatedAt(), responseTeacher.getUpdatedAt());
    }

    @Test
    void testFindById_TeacherDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/teacher/9999")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAll_TeachersExist() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/teacher")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Teacher[] responseTeachers = objectMapper.readValue(responseBody, Teacher[].class);

        assertEquals(2, responseTeachers.length);
        assertEquals(teacher1.getId(), responseTeachers[0].getId());
        assertEquals(teacher1.getFirstName(), responseTeachers[0].getFirstName());
        assertEquals(teacher1.getLastName(), responseTeachers[0].getLastName());
        assertEquals(teacher1.getCreatedAt(), responseTeachers[0].getCreatedAt());
        assertEquals(teacher1.getUpdatedAt(), responseTeachers[0].getUpdatedAt());

        assertEquals(teacher2.getId(), responseTeachers[1].getId());
        assertEquals(teacher1.getFirstName(), responseTeachers[0].getFirstName());
        assertEquals(teacher2.getLastName(), responseTeachers[1].getLastName());
        assertEquals(teacher2.getCreatedAt(), responseTeachers[1].getCreatedAt());
        assertEquals(teacher2.getUpdatedAt(), responseTeachers[1].getUpdatedAt());
    }
}
