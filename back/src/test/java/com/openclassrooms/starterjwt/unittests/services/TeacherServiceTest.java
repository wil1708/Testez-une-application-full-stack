package com.openclassrooms.starterjwt.unittests.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        Teacher teacher1 = Teacher.builder().id(1L).firstName("William").lastName("Pires").build();
        Teacher teacher2 = Teacher.builder().id(2L).firstName("Elena").lastName("Silverberg").build();

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(teacher1, teacher2));

        List<Teacher> teachers = teacherService.findAll();

        assertNotNull(teachers);
        assertEquals(2, teachers.size());
        assertEquals("William", teachers.get(0).getFirstName());
        assertEquals("Elena", teachers.get(1).getFirstName());
    }

    @Test
    void testFindById_TeacherExists() {
        Teacher teacher = Teacher.builder().id(1L).firstName("William").lastName("Pires").build();

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher foundTeacher = teacherService.findById(1L);

        assertNotNull(foundTeacher);
        assertEquals("William", foundTeacher.getFirstName());
        assertEquals("Pires", foundTeacher.getLastName());
    }

    @Test
    void testFindById_TeacherDoesNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Teacher foundTeacher = teacherService.findById(1L);

        assertNull(foundTeacher);
    }
}
