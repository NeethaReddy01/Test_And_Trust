package com.backend.controller;

import com.backend.modal.Review;
import com.backend.modal.User;
import com.backend.request.ReviewRequest;
import com.backend.service.ReviewService;
import com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewControllerTest {

    @InjectMocks
    private ReviewController reviewController;

    @Mock
    private ReviewService reviewService;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateReviewHandler() throws Exception {
        // Arrange
        String jwt = "dummy-jwt";
        ReviewRequest request = new ReviewRequest();
        request.setProductId(1L);
        request.setReview("Great product!");

        User user = new User();
        Review review = new Review();
        review.setReview("Great product!");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(reviewService.createReview(request, user)).thenReturn(review);

        // Act
        ResponseEntity<Review> response = reviewController.createReviewHandler(request, jwt);

        // Assert
        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Great product!", response.getBody().getReview());
    }

    @Test
    void testGetProductsReviewHandler() {
        // Arrange
        Long productId = 1L;
        Review review = new Review();
        review.setReview("Nice product.");
        when(reviewService.getAllReview(productId)).thenReturn(List.of(review));

        // Act
        ResponseEntity<List<Review>> response = reviewController.getProductsReviewHandler(productId);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Nice product.", response.getBody().get(0).getReview());
    }
}
