package com.backend.controllerTest;

import com.backend.controller.ReviewController;
import com.backend.exception.ProductException;
import com.backend.exception.UserException;
import com.backend.modal.Review;
import com.backend.modal.User;
import com.backend.request.ReviewRequest;
import com.backend.service.ReviewService;
import com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
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
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReviewHandler() throws UserException, ProductException {
        String jwt = "mock-jwt";
        ReviewRequest request = new ReviewRequest();
        request.setProductId(1L);
        request.setReview("Amazing product!");

        User user = new User();
        user.setId(1L);

        Review review = new Review();
        review.setId(1L);
        review.setReview("Amazing product!");
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(reviewService.createReview(request, user)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.createReviewHandler(request, jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals("Amazing product!", response.getBody().getReview());
        verify(userService).findUserProfileByJwt(jwt);
        verify(reviewService).createReview(request, user);
    }

    @Test
    public void testGetProductsReviewHandler() {
        Long productId = 1L;
        Review r1 = new Review();
        r1.setReview("Great!");
        Review r2 = new Review();
        r2.setReview("Not bad.");

        when(reviewService.getAllReview(productId)).thenReturn(Arrays.asList(r1, r2));

        ResponseEntity<List<Review>> response = reviewController.getProductsReviewHandler(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Great!", response.getBody().get(0).getReview());
        verify(reviewService).getAllReview(productId);
    }
}

