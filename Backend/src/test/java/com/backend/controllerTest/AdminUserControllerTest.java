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


    @Test
    void testGetAllUsers_MissingAuthorizationHeader() throws Exception {
        mockMvc.perform(get("/api/admin/users")) // No Authorization header
               .andExpect(status().isBadRequest()); // Or HttpStatus.UNAUTHORIZED if you enforce security
    }
    
    @Test
    void testGetAllUsers_InvalidMethod() throws Exception {
        mockMvc.perform(post("/api/admin/users") // should be GET
                .header("Authorization", "Bearer token"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void testGetAllUsers_CheckFieldsInResponse() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("test@example.com");

        when(userService.findAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Test"))
                .andExpect(jsonPath("$[0].lastName").value("User"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"));
    }
    //........
    @Test
    void testGetAllUsers_NullListReturned() throws Exception {
        when(userService.findAllUsers()).thenReturn(null);

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(content().string("")); // Will return empty body
    }
    @Test
    void testGetAllUsers_UserWithNullFields() throws Exception {
        User user = new User(); // all fields null
        when(userService.findAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].id").doesNotExist());
    }
    @Test
    void testGetAllUsers_LongAuthorizationHeader() throws Exception {
        String longToken = "Bearer " + "a".repeat(5000);
        when(userService.findAllUsers()).thenReturn(List.of(new User()));

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", longToken))
                .andExpect(status().isAccepted());
    }
    @Test
    void testGetAllUsers_SpecialCharactersInResponse() throws Exception {
        User user = new User();
        user.setId(99L);
        user.setFirstName("Tęšt!@#");
        user.setLastName("Üšęř");
        user.setEmail("specialchars@example.com");

        when(userService.findAllUsers()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "Bearer token"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$[0].firstName").value("Tęšt!@#"))
                .andExpect(jsonPath("$[0].lastName").value("Üšęř"))
                .andExpect(jsonPath("$[0].email").value("specialchars@example.com"));
    }
    @Test
    void testGetAllUsers_AuthHeaderWithExtraSpaces() throws Exception {
        mockMvc.perform(get("/api/admin/users")
                .header("Authorization", "  Bearer   token   "))
                .andExpect(status().isAccepted());
    }


}
