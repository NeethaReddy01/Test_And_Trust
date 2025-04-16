package com.backend.controllerTest;

import com.backend.controller.AdminUserController;
import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(MockitoExtension.class)
public class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @InjectMocks
    private AdminUserController adminUserController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminUserController).build();
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        List<User> users = List.of(new User());
        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllUsers_MultipleUsers() throws Exception {
        User user1 = new User();
        user1.setId(1L);
        user1.setFirstName("Alice");

        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Bob");

        List<User> users = List.of(user1, user2);
        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].firstName").value("Alice"))
               .andExpect(jsonPath("$[1].firstName").value("Bob"));
    }

    @Test
    void testGetAllUsers_EmptyList() throws Exception {
        when(userService.findAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$.length()").value(0));
    }

//    @Test
//    void testGetAllUsers_UserExceptionThrown() throws Exception {
//        when(userService.findAllUsers()).thenThrow(new UserException("Failed to fetch users"));
//
//        mockMvc.perform(get("/api/admin/users")
//                        .header("Authorization", "Bearer token"))
//               .andExpect(status().isInternalServerError())
//               .andExpect(jsonPath("$.message").value("Failed to fetch users"));
//    }

    @Test
    void testGetAllUsers_MissingAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/api/admin/users")) // No Authorization header
               .andExpect(status().isBadRequest()); // Or HttpStatus.UNAUTHORIZED if you enforce security
    }
}
