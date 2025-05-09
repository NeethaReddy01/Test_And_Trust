package com.backend.controller;

import com.backend.modal.Rating;
import com.backend.modal.User;
import com.backend.request.RatingRequest;
import com.backend.service.RatingServices;
import com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RatingControllerTest {

    @InjectMocks
    private RatingController ratingController;

    @Mock
    private RatingServices ratingServices;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRatingHandler() throws Exception {
        // Arrange
        String jwt = "dummy-jwt-token";
        RatingRequest request = new RatingRequest();
        request.setProductId(1L);
        request.setRating(4.5);

        User user = new User();
        Rating rating = new Rating();
        rating.setRating(4.5);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(ratingServices.createRating(request, user)).thenReturn(rating);

        // Act
        ResponseEntity<Rating> response = ratingController.createRatingHandler(request, jwt);

        // Assert
        assertEquals(202, response.getStatusCodeValue());
        assertEquals(4.5, response.getBody().getRating());
    }

    @Test
    void testGetProductsReviewHandler() {
        // Arrange
        Long productId = 1L;
        Rating rating = new Rating();
        rating.setRating(5.0);
        when(ratingServices.getProductsRating(productId)).thenReturn(List.of(rating));

        // Act
        ResponseEntity<List<Rating>> response = ratingController.getProductsReviewHandler(productId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(5.0, response.getBody().get(0).getRating());
    }
}
