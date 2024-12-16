package com.openclassrooms.starterjwt.unittests.models;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("william@example.com");
        user.setLastName("Pires");
        user.setFirstName("William");
        user.setPassword("test!1234");
        user.setAdmin(true);
        user.setCreatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
        user.setUpdatedAt(LocalDateTime.of(2024, 12, 15, 17, 52, 21));
    }

    @Test
    void testUserCreation() {
        User newUser = new User("william@example.com", "Pires", "William", "test!1234", true);
        // On set le même Id que user à newUser
        newUser.setId(1L);
        assertNotNull(newUser);
        assertEquals("william@example.com", newUser.getEmail());
        assertEquals("Pires", newUser.getLastName());
        assertEquals("William", newUser.getFirstName());
        assertEquals("test!1234", newUser.getPassword());
        assertTrue(newUser.isAdmin());
        assertEquals(1L, newUser.getId());
    }

    @Test
    void testSettersAndGetters() {
        user.setEmail("elena@example.com");
        user.setLastName("Silverberg");
        user.setFirstName("Elena");
        user.setPassword("test!1234");
        user.setAdmin(false);

        assertNotNull(user.getId());
        assertEquals(1L, user.getId());
        assertEquals("elena@example.com", user.getEmail());
        assertEquals("Silverberg", user.getLastName());
        assertEquals("Elena", user.getFirstName());
        assertEquals("test!1234", user.getPassword());
        assertFalse(user.isAdmin());
    }

    @Test
    void testEqualsAndHashCode() {
        User anotherUser = new User("jowy@example.com", "Atreides", "Jowy", "test!1234", false);
        // On set le même Id que user à newUser
        anotherUser.setId(1L);

        assertEquals(user, anotherUser);
        assertEquals(user.hashCode(), anotherUser.hashCode());

        // On change l'ID d'anotherUser pour s'assurer qu'il n'est pas égal à celui de user
        anotherUser.setId(2L);
        assertNotEquals(user, anotherUser);
        assertNotEquals(user.hashCode(), anotherUser.hashCode());
    }

    @Test
    void testToString() {
        String expectedString = "User(id=1, email=william@example.com, lastName=Pires, firstName=William, password=test!1234, admin=true, createdAt=2024-12-15T17:52:21, updatedAt=2024-12-15T17:52:21)";
        assertEquals(expectedString, user.toString());
    }
}
