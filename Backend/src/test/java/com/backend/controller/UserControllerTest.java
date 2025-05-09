package com.backend.controller;

import com.backend.exception.UserException;
import com.backend.modal.User;
import com.backend.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // ✅ disables Spring Security filters
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    // ✅ Successful case: user exists
    @Test
    void testGetUserProfileHandler_ReturnsUserProfile() throws Exception {
        User user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");

        String fakeJwt = "fake-jwt-token";

        Mockito.when(userService.findUserProfileByJwt(fakeJwt)).thenReturn(user);

        mockMvc.perform(get("/api/users/profile")
                .header("Authorization", fakeJwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    // ❌ Failure case: user not found
    @Test
    void testGetUserProfileHandler_UserNotFound() throws Exception {
        String fakeJwt = "invalid-jwt-token";

        Mockito.when(userService.findUserProfileByJwt(fakeJwt))
                .thenThrow(new UserException("User not found"));

        mockMvc.perform(get("/api/users/profile")
                .header("Authorization", fakeJwt)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError()); // You can adjust based on your global exception handler
    }
}
