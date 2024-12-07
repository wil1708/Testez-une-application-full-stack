package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
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
import java.util.Date;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private SessionService sessionService;

    @MockBean
    private SessionMapper sessionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        session = Session.builder()
                .id(1L)
                .name("Yoga matin")
                .date(new Date())
                .description("Session de yoga relaxante le matin")
                .build();

        sessionDto = new SessionDto(
                1L,
                "Yoga matin",
                new Date(),
                1L,
                "Session de yoga relaxante le mati",
                null,
                null,
                null
        );
    }

    @Test
    void testFindById_SessionExists() throws Exception {
        Mockito.when(sessionService.getById(1L)).thenReturn(session);
        Mockito.when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        MvcResult result = mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(sessionDto);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testFindById_SessionDoesNotExist() throws Exception {
        Mockito.when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testFindAll_SessionsExist() throws Exception {
        List<Session> sessions = Collections.singletonList(session);
        List<SessionDto> sessionDtos = Collections.singletonList(sessionDto);

        Mockito.when(sessionService.findAll()).thenReturn(sessions);
        Mockito.when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        MvcResult result = mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(sessionDtos);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testFindAll_NoSessionsExist() throws Exception {
        Mockito.when(sessionService.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(sessionMapper.toDto(Collections.emptyList())).thenReturn(Collections.emptyList());

        MvcResult result = mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(Collections.emptyList());
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testCreate_Session() throws Exception {
        Mockito.when(sessionService.create(Mockito.any(Session.class))).thenReturn(session);
        Mockito.when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        Mockito.when(sessionMapper.toEntity(sessionDto)).thenReturn(session);

        MvcResult result = mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(sessionDto);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testUpdate_Session() throws Exception {
        Mockito.when(sessionService.update(Mockito.eq(1L), Mockito.any(Session.class))).thenReturn(session);
        Mockito.when(sessionMapper.toDto(session)).thenReturn(sessionDto);
        Mockito.when(sessionMapper.toEntity(sessionDto)).thenReturn(session);

        MvcResult result = mockMvc.perform(put("/api/session/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(sessionDto);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testDelete_Session() throws Exception {
        Mockito.when(sessionService.getById(1L)).thenReturn(session);

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testParticipate() throws Exception {
        Mockito.doNothing().when(sessionService).participate(1L, 1L);

        mockMvc.perform(post("/api/session/1/participate/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testNoLongerParticipate() throws Exception {
        Mockito.doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        mockMvc.perform(delete("/api/session/1/participate/1"))
                .andExpect(status().isOk());
    }
}
