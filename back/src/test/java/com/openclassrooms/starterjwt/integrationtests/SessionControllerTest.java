package com.openclassrooms.starterjwt.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

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

    private Session session1;
    private Session session2;
    private Teacher teacher1;
    private Teacher teacher2;
    private User adminUser;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        Optional<User> adminUserOptional = userRepository.findById(1L);
        Optional<Teacher> teacher1Optional = teacherRepository.findById(1L);
        Optional<Teacher> teacher2Optional = teacherRepository.findById(2L);
        Optional<Session> session1Optional = sessionRepository.findById(1L);
        Optional<Session> session2Optional = sessionRepository.findById(2L);

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

        session1 = session1Optional.orElseGet(() -> {
            Session session = new Session();
            session.setName("Session 1");
            session.setDescription("Description 1");
            session.setDate(new Date());
            session.setTeacher(teacher1);
            session.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            session.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            return sessionRepository.save(session);
        });

        session2 = session2Optional.orElseGet(() -> {
            Session session = new Session();
            session.setName("Session 2");
            session.setDescription("Description 2");
            session.setDate(new Date());
            session.setTeacher(teacher2);
            session.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            session.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
            return sessionRepository.save(session);
        });

        // Authentification comme adminUser, génération du token JWT et set du token dans le SecurityContext
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUser.getEmail(), "test!1234"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        jwtToken = jwtUtils.generateJwtToken(authentication);
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    @Test
    void testFindById_SessionExists() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/session/1")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SessionDto responseSession = objectMapper.readValue(responseBody, SessionDto.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(session1.getId(), responseSession.getId());
        assertEquals(session1.getName(), responseSession.getName());
        assertEquals(session1.getDescription(), responseSession.getDescription());
        assertEquals(dateFormat.format(session1.getDate()), dateFormat.format(responseSession.getDate()));
        assertEquals(session1.getTeacher().getId(), responseSession.getTeacher_id());
        assertEquals(dateFormat.format(convertToDate(session1.getCreatedAt())), dateFormat.format(convertToDate(responseSession.getCreatedAt())));
        assertEquals(dateFormat.format(convertToDate(session1.getUpdatedAt())), dateFormat.format(convertToDate(responseSession.getUpdatedAt())));
    }

    @Test
    void testFindById_SessionDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/session/9999").header("Authorization", "Bearer " + jwtToken)).andExpect(status().isNotFound());
    }

    @Test
    void testFindAll_SessionsExist() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/session").header("Authorization", "Bearer " + jwtToken)).andExpect(status().isOk()).andReturn();

        String responseBody = result.getResponse().getContentAsString();
        SessionDto[] responseSessions = objectMapper.readValue(responseBody, SessionDto[].class);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        assertEquals(2, responseSessions.length);
        assertEquals(session1.getId(), responseSessions[0].getId());
        assertEquals(session1.getName(), responseSessions[0].getName());
        assertEquals(session1.getDescription(), responseSessions[0].getDescription());
        assertEquals(dateFormat.format(session1.getDate()), dateFormat.format(responseSessions[0].getDate()));
        assertEquals(session1.getTeacher().getId(), responseSessions[0].getTeacher_id());
        assertEquals(dateFormat.format(convertToDate(session1.getCreatedAt())), dateFormat.format(convertToDate(responseSessions[0].getCreatedAt())));
        assertEquals(dateFormat.format(convertToDate(session1.getUpdatedAt())), dateFormat.format(convertToDate(responseSessions[0].getUpdatedAt())));

        assertEquals(session2.getId(), responseSessions[1].getId());
        assertEquals(session2.getName(), responseSessions[1].getName());
        assertEquals(session2.getDescription(), responseSessions[1].getDescription());
        assertEquals(dateFormat.format(session2.getDate()), dateFormat.format(responseSessions[1].getDate()));
        assertEquals(session2.getTeacher().getId(), responseSessions[1].getTeacher_id());
        assertEquals(dateFormat.format(convertToDate(session2.getCreatedAt())), dateFormat.format(convertToDate(responseSessions[1].getCreatedAt())));
        assertEquals(dateFormat.format(convertToDate(session2.getUpdatedAt())), dateFormat.format(convertToDate(responseSessions[1].getUpdatedAt())));
    }
}