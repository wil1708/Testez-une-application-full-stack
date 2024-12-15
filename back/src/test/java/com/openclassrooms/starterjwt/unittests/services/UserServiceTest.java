package com.openclassrooms.starterjwt.unittests.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDelete() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.delete(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById_UserExists() {
        User user = User.builder()
                .id(1L)
                .email("william@example.com")
                .firstName("William")
                .lastName("Pires")
                .password("password")
                .admin(false)
                .build();

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User foundUser = userService.findById(1L);

        assertNotNull(foundUser);
        assertEquals("william@example.com", foundUser.getEmail());
        assertEquals("William", foundUser.getFirstName());
        assertEquals("Pires", foundUser.getLastName());
    }

    @Test
    void testFindById_UserDoesNotExist() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User foundUser = userService.findById(1L);

        assertNull(foundUser);
    }
}
