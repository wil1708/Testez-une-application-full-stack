package com.openclassrooms.starterjwt.integrationtests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private User adminUser;
    private User elenaUser;
    private String adminJwtToken;
    private String elenaJwtToken;

    @BeforeEach
    void setUp() {
        Optional<User> adminUserOptional = userRepository.findById(1L);
        Optional<User> elenaUserOptional = userRepository.findByEmail("elena@example.com");

        adminUser = adminUserOptional.orElseThrow(() -> new IllegalStateException("Admin user not found in database"));

        if (elenaUserOptional.isPresent()) {
            elenaUser = elenaUserOptional.get();
        } else {
            elenaUser = User.builder()
                    .email("elena@example.com")
                    .firstName("Elena")
                    .lastName("Silverberg")
                    .password("$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq")
                    .admin(false)
                    .build();
            elenaUser = userRepository.save(elenaUser);
        }

        // Authentification comme adminUser, génération du token JWT et set du token dans le SecurityContext
        Authentication adminAuth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminUser.getEmail(), "test!1234"));
        SecurityContextHolder.getContext().setAuthentication(adminAuth);
        adminJwtToken = jwtUtils.generateJwtToken(adminAuth);

        // Authentification comme elenaUser et génération du token JWT
        Authentication elenaAuth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(elenaUser.getEmail(), "test!1234"));
        elenaJwtToken = jwtUtils.generateJwtToken(elenaAuth);
    }

    @Test
    void testFindById_UserExists() throws Exception {
        MvcResult result = mockMvc.perform(get("/api/user/" + adminUser.getId())
                        .header("Authorization", "Bearer " + adminJwtToken))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        User responseUser = objectMapper.readValue(responseBody, User.class);

        assertEquals(adminUser.getId(), responseUser.getId());
        assertEquals(adminUser.getEmail(), responseUser.getEmail());
        assertEquals(adminUser.getFirstName(), responseUser.getFirstName());
        assertEquals(adminUser.getLastName(), responseUser.getLastName());
        assertEquals(adminUser.isAdmin(), responseUser.isAdmin());
    }

    @Test
    void testFindById_UserDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/user/9999")
                        .header("Authorization", "Bearer " + adminJwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        mockMvc.perform(delete("/api/user/" + elenaUser.getId())
                        .header("Authorization", "Bearer " + elenaJwtToken))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser_Unauthorized() throws Exception {
        // On essaie de supprimer l'utilisateur admin avec le token d'un utilisateur non-admin elena
        mockMvc.perform(delete("/api/user/" + adminUser.getId())
                        .header("Authorization", "Bearer " + elenaJwtToken))
                .andExpect(status().isUnauthorized());
    }

}
