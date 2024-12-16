package com.openclassrooms.starterjwt.unittests.mappers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessionMapperTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @InjectMocks
    private SessionMapper sessionMapper = Mappers.getMapper(SessionMapper.class);

    private Teacher teacher;
    private User user;
    private Session session;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Pires");
        teacher.setFirstName("William");

        user = new User();
        user.setId(1L);
        user.setEmail("william@example.com");
        user.setLastName("Pires");
        user.setFirstName("William");
        user.setPassword("test!1234");
        user.setAdmin(true);

        session = new Session();
        session.setId(1L);
        session.setName("Yoga Class");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session.");
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));
        session.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
        session.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
    }

    @Test
    void testSessionToSessionDto() {
        SessionDto sessionDto = sessionMapper.toDto(session);

        assertNotNull(sessionDto);
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDate(), sessionDto.getDate());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(session.getTeacher().getId(), sessionDto.getTeacher_id());
        assertEquals(session.getUsers().size(), sessionDto.getUsers().size());
        assertEquals(session.getUsers().get(0).getId(), sessionDto.getUsers().get(0));
        assertEquals(session.getCreatedAt(), sessionDto.getCreatedAt());
        assertEquals(session.getUpdatedAt(), sessionDto.getUpdatedAt());
    }

    @Test
    void testSessionDtoToSession() {
        SessionDto sessionDto = new SessionDto(
                1L,
                "Yoga Class",
                new Date(),
                1L,
                "A relaxing yoga session.",
                Collections.singletonList(1L),
                LocalDateTime.of(2024, 12, 15, 17, 52, 21),
                LocalDateTime.of(2024, 12, 15, 17, 52, 21)
        );

        when(teacherService.findById(anyLong())).thenReturn(teacher);
        when(userService.findById(anyLong())).thenReturn(user);

        Session session = sessionMapper.toEntity(sessionDto);

        assertNotNull(session);
        assertEquals(sessionDto.getId(), session.getId());
        assertEquals(sessionDto.getName(), session.getName());
        assertEquals(sessionDto.getDate(), session.getDate());
        assertEquals(sessionDto.getDescription(), session.getDescription());
        assertEquals(sessionDto.getTeacher_id(), session.getTeacher().getId());
        assertEquals(sessionDto.getUsers().size(), session.getUsers().size());
        assertEquals(sessionDto.getUsers().get(0), session.getUsers().get(0).getId());
        assertEquals(sessionDto.getCreatedAt(), session.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), session.getUpdatedAt());
    }
}
