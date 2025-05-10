package com.backend.serviceTest;

import com.backend.config.JwtTokenProvider;
import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.repository.UserRepository;
import com.backend.service.UserServiceImplementation;

import io.jsonwebtoken.lang.Collections;

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
    public void testFindUserById_Success11() throws UserException {
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
    //........
    @Test
    public void testFindUserById_Success1() throws UserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals("user@example.com", result.getEmail());
    }

    // Test: Find user by ID - Not Found
    @Test
    public void testFindUserById_NotFound1() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(UserException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("user not found with id 1", ex.getMessage());
    }

    // Test: Find user profile by JWT - Success
    @Test
    public void testFindUserProfileByJwt_Success1() throws UserException {
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

    // Test: Find user profile by JWT - User not found
    @Test
    public void testFindUserProfileByJwt_UserNotFound1() {
        String jwt = "mock-jwt";
        String email = "nonexistent@example.com";

        when(jwtTokenProvider.getEmailFromJwtToken(jwt)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(null);

        Exception ex = assertThrows(UserException.class, () -> {
            userService.findUserProfileByJwt(jwt);
        });

        assertEquals("user not exist with email nonexistent@example.com", ex.getMessage());
    }


  

    // Test: Find all users - Empty list
    @Test
    public void testFindAllUsers_EmptyList() {
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList());

        assertTrue(userService.findAllUsers().isEmpty());
        verify(userRepository).findAllByOrderByCreatedAtDesc();
    }

    // Test: Find all users - Multiple users
    @Test
    public void testFindAllUsers_MultipleUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(user, user2));

        assertEquals(2, userService.findAllUsers().size());
        verify(userRepository).findAllByOrderByCreatedAtDesc();
    }
    //............................
    @Test
    public void testFindUserById_Success() throws UserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.findUserById(1L);

        assertEquals("user@example.com", result.getEmail());
    }

    // Test: Find user by ID - Not Found
    @Test
    public void testFindUserById_NotFound11() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(UserException.class, () -> {
            userService.findUserById(1L);
        });

        assertEquals("user not found with id 1", ex.getMessage());
    }

    // Test: Find user by ID - Null ID
  

    // Test: Find user profile by JWT - Success
    @Test
    public void testFindUserProfileByJwt_Success11() throws UserException {
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

    // Test: Find user profile by JWT - User not found
    @Test
    public void testFindUserProfileByJwt_UserNotFound11() {
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
    public void testFindAllUsers_EmptyList1() {
        //when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());

        assertTrue(userService.findAllUsers().isEmpty());
        verify(userRepository).findAllByOrderByCreatedAtDesc();
    }

    // Test: Find all users - Multiple users
    @Test
    public void testFindAllUsers_MultipleUsers1() {
        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user2@example.com");

        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Arrays.asList(user, user2));

        assertEquals(2, userService.findAllUsers().size());
        verify(userRepository).findAllByOrderByCreatedAtDesc();
    }

  

    // Test: Database error in find user by ID

}

