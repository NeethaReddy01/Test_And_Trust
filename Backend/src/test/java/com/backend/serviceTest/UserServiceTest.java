package com.backend.serviceTest;

import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.repository.UserRepository;
import com.backend.service.UserServiceImplementation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

   

    @InjectMocks
    private UserServiceImplementation userService;

    private User testUser;

    @BeforeEach
    public void setup() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setFirstName("Alice");
        testUser.setLastName("Smith");
        testUser.setEmail("alice@example.com");
        testUser.setPassword("password123");
        testUser.setMobile("9876543210");
        testUser.setRole("USER");
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void testFindUserById_Success() throws UserException {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        User foundUser = userService.findUserById(1L);

        assertNotNull(foundUser);
        assertEquals("Alice", foundUser.getFirstName());
    }

    @Test
    public void testFindUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserException.class, () -> userService.findUserById(1L));
    }


 

    @Test
    public void testFindUserProfileByJwt_NullEmail() {
        //when(jwtService.extractUsername("token")).thenReturn(null);
        //assertThrows(UserException.class, () -> userService.findUserProfileByJwt("token"));
    }

    @Test
    public void testFindAllUsers_ReturnsList() {
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(testUser));
        List<User> users = userService.findAllUsers();

        assertEquals(1, users.size());
        assertEquals("Alice", users.get(0).getFirstName());
    }

    @Test
    public void testFindAllUsers_ReturnsEmptyList() {
        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());
        List<User> users = userService.findAllUsers();

        assertTrue(users.isEmpty());
    }
    @Test
    public void testFindUserById_NullId() {
        //assertThrows(UserException.class, () -> userService.findUserById(null));
    }
    @Test
    public void testFindUserProfileByJwt_MalformedJwt() {
        String malformedToken = "invalid-jwt-token";
        //assertThrows(UserException.class, () -> userService.findUserProfileByJwt(malformedToken));
    }

  
 
  
    @Test
    public void testFindAllUsers_MultipleUsers() {
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Bob");
        user2.setEmail("bob@example.com");

        when(userRepository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(testUser, user2));

        List<User> users = userService.findAllUsers();
        assertEquals(2, users.size());
    }
 

}



