package com.openclassrooms.starterjwt.unittests.models;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private Session session;
    private Teacher teacher;
    private User user;

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
    void testSessionCreation() {
        Session newSession = new Session();
        newSession.setId(1L);
        newSession.setName("Yoga Class");
        newSession.setDate(new Date());
        newSession.setDescription("A relaxing yoga session.");
        newSession.setTeacher(teacher);
        newSession.setUsers(Collections.singletonList(user));
        newSession.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
        newSession.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));

        assertNotNull(newSession);
        assertEquals(1L, newSession.getId());
        assertEquals("Yoga Class", newSession.getName());
        assertNotNull(newSession.getDate());
        assertEquals("A relaxing yoga session.", newSession.getDescription());
        assertEquals(teacher, newSession.getTeacher());
        assertEquals(1, newSession.getUsers().size());
        assertEquals(user, newSession.getUsers().get(0));
        assertEquals(LocalDateTime.of(2024, 12, 15, 17, 52, 21), newSession.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 12, 15, 17, 52, 21), newSession.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        session.setId(2L);
        session.setName("Pilates Class");
        session.setDate(new Date());
        session.setDescription("An invigorating pilates session.");
        session.setTeacher(teacher);
        session.setUsers(Collections.singletonList(user));

        assertNotNull(session.getId());
        assertEquals(2L, session.getId());
        assertEquals("Pilates Class", session.getName());
        assertNotNull(session.getDate());
        assertEquals("An invigorating pilates session.", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(1, session.getUsers().size());
        assertEquals(user, session.getUsers().get(0));
    }

    @Test
    void testEqualsAndHashCode() {
        Session anotherSession = new Session();
        anotherSession.setId(1L);

        assertEquals(session, anotherSession);
        assertEquals(session.hashCode(), anotherSession.hashCode());

        anotherSession.setId(2L);
        assertNotEquals(session, anotherSession);
        assertNotEquals(session.hashCode(), anotherSession.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "Session(id=1, name=Yoga Class, date=" + session.getDate() + ", description=A relaxing yoga session., teacher=Teacher(id=1, lastName=Pires, firstName=William, createdAt=null, updatedAt=null), users=[User(id=1, email=william@example.com, lastName=Pires, firstName=William, password=test!1234, admin=true, createdAt=null, updatedAt=null)], createdAt=2024-12-15T17:52:21, updatedAt=2024-12-15T17:52:21)";
        assertEquals(expectedString, session.toString());
    }
}
