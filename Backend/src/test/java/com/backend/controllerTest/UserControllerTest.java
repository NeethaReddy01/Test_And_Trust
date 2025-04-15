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
}
