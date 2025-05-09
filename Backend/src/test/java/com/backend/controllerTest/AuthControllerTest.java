package com.backend.controllerTest;

import com.backend.config.JwtTokenProvider;
import com.backend.controller.AuthController;
import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.repository.UserRepository;
import com.backend.request.LoginRequest;
import com.backend.response.AuthResponse;
import com.backend.service.CartService;
import com.backend.service.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private CustomUserDetails customUserDetails;

    @Mock
    private CartService cartService;

    @InjectMocks
    private AuthController authController;

    private User user;
    private String token;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password123");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setRole("ROLE_CUSTOMER");
        token = "mocked-jwt-token";
    }

    // Test case 1: User registration successful
    @Test
    public void testCreateUserHandler_Success() throws UserException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(token);

        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getStatus());
        assertEquals(token, response.getBody().getJwt());

        verify(cartService).createCart(any(User.class));
        verify(userRepository).save(any(User.class));
    }

    // Test case 2: User registration fails due to email already existing
    @Test
    public void testCreateUserHandler_EmailExists() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);

        UserException exception = assertThrows(UserException.class, () -> {
            authController.createUserHandler(user);
        });

        assertEquals("Email Is Already Used With Another Account", exception.getMessage());
    }

    // Test case 3: Sign-in successful
    @Test
    public void testSignin_Success() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("encodedPassword")
                .roles("CUSTOMER")
                .build();

        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(token);

        ResponseEntity<AuthResponse> response = authController.signin(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertTrue(response.getBody().getStatus());
        assertEquals(token, response.getBody().getJwt());
    }

    // Test case 4: Invalid password during sign-in
    @Test
    public void testSignin_InvalidPassword() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("wrongPassword");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("encodedPassword")
                .roles("CUSTOMER")
                .build();

        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () -> {
            authController.signin(loginRequest);
        });
    }

    // Test case 5: User not found during sign-in
    @Test
    public void testSignin_UserNotFound() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("unknown@example.com");
        loginRequest.setPassword("somePassword");

        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(null);

        assertThrows(BadCredentialsException.class, () -> {
            authController.signin(loginRequest);
        });
    }

    // Test case 6: Password encoding failure during user registration
    @Test
    public void testCreateUserHandler_PasswordEncodingFailure() throws UserException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenThrow(new RuntimeException("Encoding failed"));

        assertThrows(RuntimeException.class, () -> {
            authController.createUserHandler(user);
        });
    }

    // Test case 7: Cart creation failure during user registration
    @Test
    public void testCreateUserHandler_CartCreationFailure() throws UserException {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        doThrow(new RuntimeException("Cart creation failed")).when(cartService).createCart(any(User.class));

        assertThrows(RuntimeException.class, () -> {
            authController.createUserHandler(user);
        });
    }

    // Test case 8: Token generation failure during sign-in
    @Test
    public void testSignin_TokenGenerationFailure() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("encodedPassword")
                .roles("CUSTOMER")
                .build();

        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenThrow(new RuntimeException("Token generation failed"));

        assertThrows(RuntimeException.class, () -> {
            authController.signin(loginRequest);
        });
    }
    //.....
    @Test
    public void testSignin_UserDetailsWithoutAuthorities() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("test@example.com");
        when(userDetails.getPassword()).thenReturn("encodedPassword");
        when(userDetails.getAuthorities()).thenReturn(null);

        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(token);

        ResponseEntity<AuthResponse> response = authController.signin(loginRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getJwt());
    }
    @Test
    public void testCreateUserHandler_NullRole() throws UserException {
        user.setRole(null); // simulate null role

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(token);

        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(token, response.getBody().getJwt());
    }
    @Test
    public void testCreateUserHandler_EmptyFirstNameLastName() throws UserException {
        user.setFirstName("");
        user.setLastName("");

        when(userRepository.findByEmail(user.getEmail())).thenReturn(null);
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn(token);

        ResponseEntity<AuthResponse> response = authController.createUserHandler(user);

        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void testSignin_NullEmail() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(null);
        loginRequest.setPassword("password");
    }

    @Test
    public void testSignin_TokenContainsExpectedPrefix() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password123");

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername("test@example.com")
                .password("encodedPassword")
                .roles("CUSTOMER")
                .build();

        when(customUserDetails.loadUserByUsername(loginRequest.getEmail())).thenReturn(userDetails);
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenProvider.generateToken(any(Authentication.class))).thenReturn("Bearer mocked-jwt-token");

        ResponseEntity<AuthResponse> response = authController.signin(loginRequest);

        assertTrue(response.getBody().getJwt().startsWith("Bearer"));
    }
}
