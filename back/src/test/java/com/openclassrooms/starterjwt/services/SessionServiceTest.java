package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        Session session = Session.builder().name("Yoga Session").build();
        when(sessionRepository.save(session)).thenReturn(session);

        Session createdSession = sessionService.create(session);

        assertNotNull(createdSession);
        assertEquals("Yoga Session", createdSession.getName());
    }

    @Test
    void testDelete() {
        doNothing().when(sessionRepository).deleteById(anyLong());

        sessionService.delete(1L);

        verify(sessionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindAll() {
        Session session = Session.builder().name("Yoga Session").build();
        when(sessionRepository.findAll()).thenReturn(Collections.singletonList(session));

        List<Session> sessions = sessionService.findAll();

        assertNotNull(sessions);
        assertEquals(1, sessions.size());
        assertEquals("Yoga Session", sessions.get(0).getName());
    }

    @Test
    void testGetById_SessionExists() {
        Session session = Session.builder().id(1L).name("Yoga Session").build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session foundSession = sessionService.getById(1L);

        assertNotNull(foundSession);
        assertEquals("Yoga Session", foundSession.getName());
    }

    @Test
    void testGetById_SessionDoesNotExist() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session foundSession = sessionService.getById(1L);

        assertNull(foundSession);
    }

    @Test
    void testUpdate() {
        Session session = Session.builder().id(1L).name("Yoga Session").build();
        when(sessionRepository.save(session)).thenReturn(session);

        Session updatedSession = sessionService.update(1L, session);

        assertNotNull(updatedSession);
        assertEquals(1L, updatedSession.getId());
        assertEquals("Yoga Session", updatedSession.getName());
    }

    @Test
    void testParticipate_Success() {
        User user = User.builder()
                .id(1L)
                .email("william@example.com")
                .firstName("William")
                .lastName("Pires")
                .password("password")
                .admin(false)
                .build();

        Session session = Session.builder().id(1L).users(new ArrayList<>()).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        assertDoesNotThrow(() -> sessionService.participate(1L, 1L));
        assertTrue(session.getUsers().contains(user));
    }



    @Test
    void testParticipate_UserAlreadyParticipates() {
        User user = User.builder()
                .id(1L)
                .email("william@example.com")
                .firstName("William")
                .lastName("Pires")
                .password("password")
                .admin(false)
                .build();

        Session session = Session.builder().id(1L).users(new ArrayList<>(Collections.singletonList(user))).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }


    @Test
    void testNoLongerParticipate_Success() {
        User user = User.builder()
                .id(1L)
                .email("william@example.com")
                .firstName("William")
                .lastName("Pires")
                .password("password")
                .admin(false)
                .build();

        Session session = Session.builder().id(1L).users(new ArrayList<>(Collections.singletonList(user))).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(session)).thenReturn(session);

        assertDoesNotThrow(() -> sessionService.noLongerParticipate(1L, 1L));
        assertFalse(session.getUsers().contains(user));
    }


    @Test
    void testNoLongerParticipate_UserNotParticipating() {
        Session session = Session.builder().id(1L).users(Collections.emptyList()).build();
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}
