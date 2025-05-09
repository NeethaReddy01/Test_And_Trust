package com.backend.controllerTest;

import com.backend.controller.UserController;
import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
    }

    @Test
    public void testGetUserProfileHandler() throws UserException {
        String jwt = "mock-jwt";

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserProfileHandler(jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("test@example.com", response.getBody().getEmail());
        verify(userService).findUserProfileByJwt(jwt);
    }

    @Test
    public void testGetUserProfileHandlerThrowsException() throws UserException {
        String jwt = "invalid-jwt";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid token"));

        UserException thrown = assertThrows(UserException.class, () -> {
            userController.getUserProfileHandler(jwt);
        });

        assertEquals("Invalid token", thrown.getMessage());
    }
    
    //............
    @Test
    public void testFindUserById_UserExists() throws UserException {
        Long userId = 1L;
        when(userService.findUserById(userId)).thenReturn(mockUser);

        User result = userService.findUserById(userId);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userService).findUserById(userId);
    }
    @Test
    public void testFindUserById_UserNotFound() throws UserException {
        Long userId = 2L;
        when(userService.findUserById(userId)).thenThrow(new UserException("user not found with id 2"));

        UserException ex = assertThrows(UserException.class, () -> userService.findUserById(userId));
        assertEquals("user not found with id 2", ex.getMessage());
    }
    @Test
    public void testGetUserProfileHandlerWithNullJwt() throws UserException {
        String jwt = null;

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("JWT is null"));

        UserException ex = assertThrows(UserException.class, () -> userController.getUserProfileHandler(jwt));
        assertEquals("JWT is null", ex.getMessage());
    }
    @Test
    public void testGetUserProfileHandlerWithEmptyJwt() throws UserException {
        String jwt = "";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("JWT is empty"));

        UserException ex = assertThrows(UserException.class, () -> userController.getUserProfileHandler(jwt));
        assertEquals("JWT is empty", ex.getMessage());
    }
    @Test
    public void testUserProfileResponseFields() throws UserException {
        String jwt = "mock-jwt";
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserProfileHandler(jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("John", response.getBody().getFirstName());
        assertEquals("Doe", response.getBody().getLastName());
    }
    @Test
    public void testGetUserProfileHandlerCalledOnce() throws UserException {
        String jwt = "mock-jwt";

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        userController.getUserProfileHandler(jwt);
        userController.getUserProfileHandler(jwt);

        verify(userService, times(2)).findUserProfileByJwt(jwt);
    }
    @Test
    public void testUserProfileResponseStatusCodeAccepted() throws UserException {
        String jwt = "mock-jwt";
        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);

        ResponseEntity<User> response = userController.getUserProfileHandler(jwt);

        assertEquals(202, response.getStatusCodeValue());
    }


}
