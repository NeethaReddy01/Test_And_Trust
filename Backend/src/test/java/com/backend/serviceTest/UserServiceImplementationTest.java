package com.backend.serviceTest;

import com.backend.config.JwtTokenProvider;
import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.repository.UserRepository;
import com.backend.service.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private UserServiceImplementation userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");
        user.setFirstName("Jane");
    }

    @Test
    public void testFindUserById_Success() throws UserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals("user@example.com", result.getEmail());
    }

    @Test
    public void testFindUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(UserException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("user not found with id 1", ex.getMessage());
    }

    @Test
    public void testFindUserProfileByJwt_Success() throws UserException {
        String jwt = "mock-jwt";
        String email = "user@example.com";

        when(jwtTokenProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(user);

        User result = userService.findUserProfileByJwt(jwt);

        assertNotNull(result);
        assertEquals("user@example.com", result.getEmail());
        verify(jwtTokenProvider).getEmailFromJwtToken(jwt);
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testFindUserProfileByJwt_UserNotFound() {
        String jwt = "mock-jwt";
        String email = "nonexistent@example.com";

        when(jwtTokenProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        Exception ex = assertThrows(UserException.class, () -> {
            userService.findUserProfileByJwt(jwt);
        });

        assertEquals("user not exist with email nonexistent@example.com", ex.getMessage());
    }

    @Test
    public void testFindAllUsers() {
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(user));

        assertEquals(1, userService.findAllUsers().size());
        verify(userRepository).findAllByOrderByCreatedAtDesc();
    }
}

