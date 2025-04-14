package com.backend.controllerTest;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.backend.service.UserService;
import com.backend.modal.User;
import com.backend.exception.UserException;
import com.backend.controller.AdminUserController;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;

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
    void testGetAllUsers() throws Exception {
        List<User> users = List.of(new User());
        when(userService.findAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/admin/users")
                        .header("Authorization", "Bearer token"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$").isArray());
    }
}
