package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.payload.response.MessageResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private UserDetailsImpl userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        user = new User("william@example.com", "Pires", "William", "password", false);
        user.setId(1L);

        userDetails = new UserDetailsImpl(1L, "william@example.com", "William", "Pires", false, "password");

        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    void testAuthenticateUser_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("william@example.com");
        loginRequest.setPassword("password");

        Mockito.when(authenticationManager.authenticate(Mockito.any())).thenReturn(authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("mockJwtToken");
        Mockito.when(userRepository.findByEmail("william@example.com")).thenReturn(Optional.of(user));

        JwtResponse expectedResponse = new JwtResponse("mockJwtToken", 1L, "william@example.com", "William", "Pires", false);

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("elena@example.com");
        signupRequest.setFirstName("Elena");
        signupRequest.setLastName("Silverberg");
        signupRequest.setPassword("password");

        Mockito.when(userRepository.existsByEmail("elena@example.com")).thenReturn(false);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        MessageResponse expectedResponse = new MessageResponse("User registered successfully!");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);
        assert responseBody.equals(expectedJson);
    }

    @Test
    void testRegisterUser_EmailAlreadyTaken() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail("elena@example.com");
        signupRequest.setFirstName("Elena");
        signupRequest.setLastName("Silverberg");
        signupRequest.setPassword("password");

        Mockito.when(userRepository.existsByEmail("elena@example.com")).thenReturn(true);

        MessageResponse expectedResponse = new MessageResponse("Error: Email is already taken!");

        MvcResult result = mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String expectedJson = objectMapper.writeValueAsString(expectedResponse);
        assert responseBody.equals(expectedJson);
    }
}
