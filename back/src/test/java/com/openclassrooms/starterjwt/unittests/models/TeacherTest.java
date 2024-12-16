package com.openclassrooms.starterjwt.unittests.models;

import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    private Teacher teacher;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setLastName("Pires");
        teacher.setFirstName("William");
        teacher.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
        teacher.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
    }

    @Test
    void testTeacherCreation() {
        Teacher newTeacher = new Teacher();
        newTeacher.setId(1L);
        newTeacher.setLastName("Pires");
        newTeacher.setFirstName("William");
        newTeacher.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
        newTeacher.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));

        assertNotNull(newTeacher);
        assertEquals(1L, newTeacher.getId());
        assertEquals("Pires", newTeacher.getLastName());
        assertEquals("William", newTeacher.getFirstName());
        assertEquals(LocalDateTime.of(2024, 12, 15, 17, 52, 21), newTeacher.getCreatedAt());
        assertEquals(LocalDateTime.of(2024, 12, 15, 17, 52, 21), newTeacher.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        teacher.setId(2L);
        teacher.setLastName("Pires");
        teacher.setFirstName("William");

        assertNotNull(teacher.getId());
        assertEquals(2L, teacher.getId());
        assertEquals("Pires", teacher.getLastName());
        assertEquals("William", teacher.getFirstName());
    }

    @Test
    void testEqualsAndHashCode() {
        Teacher anotherTeacher = new Teacher();
        // On set le même Id que pour celui de teacher
        anotherTeacher.setId(1L);

        // On s'assure que les deux teachers ont le même id
        teacher.setId(1L);
        teacher.setLastName("Pires");
        teacher.setFirstName("William");


        assertEquals(teacher, anotherTeacher);
        assertEquals(teacher.hashCode(), anotherTeacher.hashCode());

        // On s'assure que les deux teachers n'ont pas le même id
        anotherTeacher.setId(2L);
        assertNotEquals(teacher, anotherTeacher);
        assertNotEquals(teacher.hashCode(), anotherTeacher.hashCode());
    }

    @Test
    void testToString() {
        teacher.setId(1L);
        String expectedString = "Teacher(id=1, lastName=Pires, firstName=William, createdAt=2024-12-15T17:52:21, updatedAt=2024-12-15T17:52:21)";
        assertEquals(expectedString, teacher.toString());
    }
}
