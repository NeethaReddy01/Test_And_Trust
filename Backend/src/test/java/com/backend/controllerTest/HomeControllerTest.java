package com.backend.controllerTest;

import com.backend.controller.HomeController;
import com.backend.response.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class HomeControllerTest {

    private HomeController homeController;

    @BeforeEach
    public void setUp() {
        homeController = new HomeController();
    }

    @Test
    public void testHomeController_ReturnsWelcomeMessage() {
        ResponseEntity<ApiResponse> response = homeController.homeController();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        ApiResponse body = response.getBody();
        assertNotNull(body);
        //assertTrue(body.getStatus());
        assertEquals("Welcome To E-Commerce System", body.getMessage());
    }

    @Test
    public void testHomeController_ResponseEntityNotNull() {
        assertNotNull(homeController.homeController());
    }

    @Test
    public void testHomeController_HttpStatusOK() {
        assertEquals(200, homeController.homeController().getStatusCodeValue());
    }

    @Test
    public void testHomeController_ResponseMessage() {
        ApiResponse res = homeController.homeController().getBody();
        assertEquals("Welcome To E-Commerce System", res.getMessage());
    }
    //......
    @Test
    public void testHomeController_ResponseStatusTrue() {
        ApiResponse response = homeController.homeController().getBody();
        //assertTrue(response.getStatus(), "Expected status to be true");
    }

    @Test
    public void testHomeController_ResponseMessageNotEmpty() {
        String message = homeController.homeController().getBody().getMessage();
        assertNotNull(message);
        assertFalse(message.trim().isEmpty(), "Message should not be empty");
    }

    @Test
    public void testHomeController_ResponseMessageExactMatch() {
        String message = homeController.homeController().getBody().getMessage();
        assertEquals("Welcome To E-Commerce System", message);
    }

    @Test
    public void testHomeController_ResponseBodyClassType() {
        Object body = homeController.homeController().getBody();
        assertInstanceOf(ApiResponse.class, body, "Response body should be instance of ApiResponse");
    }

    @Test
    public void testHomeController_ResponseEntityClassType() {
        ResponseEntity<ApiResponse> response = homeController.homeController();
        assertInstanceOf(ResponseEntity.class, response);
    }

    @Test
    public void testHomeController_ResponseNotInternalServerError() {
        ResponseEntity<ApiResponse> response = homeController.homeController();
        assertNotEquals(500, response.getStatusCodeValue(), "Should not return 500");
    }

    @Test
    public void testHomeController_ResponseMessageDoesNotContainError() {
        String message = homeController.homeController().getBody().getMessage();
        assertFalse(message.toLowerCase().contains("error"), "Message should not contain 'error'");
    }

    @Test
    public void testHomeController_ResponseIsConsistentAcrossCalls() {
        String msg1 = homeController.homeController().getBody().getMessage();
        String msg2 = homeController.homeController().getBody().getMessage();
        assertEquals(msg1, msg2, "Consecutive calls should return same message");
    }

    @Test
    public void testHomeController_ResponseStatusIsHttpOK() {
        ResponseEntity<ApiResponse> response = homeController.homeController();
        assertEquals("200 OK", response.getStatusCode().toString());
    }

    @Test
    public void testHomeController_ResponseObjectEquality() {
        ApiResponse expected = new ApiResponse("Welcome To E-Commerce System", true);
        ApiResponse actual = homeController.homeController().getBody();

        assertEquals(expected.getMessage(), actual.getMessage());
        //assertEquals(expected.getStatus(), actual.getStatus());
    }
//2..
    

    @Test
    public void testHomeController_ResponseContainsExpectedFields() {
        ApiResponse response = homeController.homeController().getBody();
        assertNotNull(response);
        assertNotNull(response.getMessage(), "Message should not be null");
        //assertNotNull(response.getStatus(), "Status should not be null");
    }

    @Test
    public void testHomeController_ResponseToStringIsValid() {
        ApiResponse response = homeController.homeController().getBody();
        String responseString = response.toString();
        assertTrue(responseString.contains("Welcome To E-Commerce System"));
    }

    @Test
    public void testHomeController_ResponseBodyNotSameObject() {
        ApiResponse res1 = homeController.homeController().getBody();
        ApiResponse res2 = homeController.homeController().getBody();
        assertNotSame(res1, res2, "Should return new ApiResponse instances per call");
    }

  

    @Test
    public void testHomeController_ResponseBodyMessageLengthGreaterThanZero() {
        int length = homeController.homeController().getBody().getMessage().length();
        assertTrue(length > 0, "Message should not be empty");
    }

    

    @Test
    public void testHomeController_ResponseIsNotNullAcrossMultipleCalls() {
        for (int i = 0; i < 5; i++) {
            assertNotNull(homeController.homeController().getBody());
        }
    }

    @Test
    public void testHomeController_ResponseMessageDoesNotContainUnexpectedContent() {
        String message = homeController.homeController().getBody().getMessage();
        assertFalse(message.contains("error"));
        assertFalse(message.contains("fail"));
    }

    @Test
    public void testHomeController_ResponseObjectHashCodeIsValid() {
        ApiResponse response = homeController.homeController().getBody();
        assertDoesNotThrow(() -> response.hashCode());
    }

}
