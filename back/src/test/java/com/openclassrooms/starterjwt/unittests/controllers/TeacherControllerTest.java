package com.openclassrooms.starterjwt.unittests.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private TeacherService teacherService;

    @MockBean
    private TeacherMapper teacherMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        teacher = Teacher.builder()
                .id(1L)
                .firstName("Elena")
                .lastName("Silverberg")
                .build();

        teacherDto = new TeacherDto(
                1L,
                "Silverberg",
                "Elena",
                null,
                null
        );
    }

    @Test
    void testFindById_TeacherExists() throws Exception {
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);
        Mockito.when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        MvcResult result = mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(teacherDto);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testFindById_TeacherDoesNotExist() throws Exception {
        Mockito.when(teacherService.findById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAll_TeachersExist() throws Exception {
        List<Teacher> teachers = Collections.singletonList(teacher);
        List<TeacherDto> teacherDtos = Collections.singletonList(teacherDto);

        Mockito.when(teacherService.findAll()).thenReturn(teachers);
        Mockito.when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        MvcResult result = mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(teacherDtos);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testFindAll_NoTeachersExist() throws Exception {
        Mockito.when(teacherService.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(teacherMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(Collections.emptyList());
        assert responseBody.equals(expectedJson);
    }
}
