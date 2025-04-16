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
    
    //..........
    @Test
    void testCreateReviewHandler_NullRequest() throws UserException {
        String jwt = "valid-jwt";
        User user = new User();

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);

        assertThrows(NullPointerException.class, () -> {
            reviewController.createReviewHandler(null, jwt);
        });

        verify(userService).findUserProfileByJwt(jwt);
    }
    @Test
    void testCreateReviewHandler_EmptyReview() throws UserException, ProductException {
        String jwt = "valid-jwt";
        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setReview(""); // empty review

        User user = new User();
        Review review = new Review();
        review.setReview("");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(reviewService.createReview(req, user)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.createReviewHandler(req, jwt);
        assertEquals(202, response.getStatusCodeValue());
        assertEquals("", response.getBody().getReview());

        verify(reviewService).createReview(req, user);
    }
    @Test
    void testCreateReviewHandler_SpecialCharacters() throws UserException, ProductException {
        String jwt = "valid-jwt";
        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setReview("Awesome product! 😍🔥");

        User user = new User();
        Review review = new Review();
        review.setReview("Awesome product! 😍🔥");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(reviewService.createReview(req, user)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.createReviewHandler(req, jwt);

        assertEquals("Awesome product! 😍🔥", response.getBody().getReview());
    }
//    @Test
//    void testCreateReviewHandler_NullProductId() throws UserException {
//        String jwt = "valid-jwt";
//        ReviewRequest req = new ReviewRequest();
//        req.setProductId(null);
//        req.setReview("Nice!");
//
//        User user = new User();
//        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
//
//        assertThrows(NullPointerException.class, () -> {
//            reviewController.createReviewHandler(req, jwt);
//        });
//    }
    @Test
    void testCreateReviewHandler_ThrowsProductException() throws UserException, ProductException {
        String jwt = "valid-jwt";
        ReviewRequest req = new ReviewRequest();
        req.setProductId(999L);
        req.setReview("Bad product!");

        User user = new User();
        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(reviewService.createReview(req, user)).thenThrow(new ProductException("Product not found"));

        ProductException ex = assertThrows(ProductException.class, () -> {
            reviewController.createReviewHandler(req, jwt);
        });

        assertEquals("Product not found", ex.getMessage());
    }
    @Test
    void testCreateReviewHandler_ThrowsUserException() throws UserException {
        String jwt = "bad-jwt";
        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setReview("Test");

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid token"));

        UserException ex = assertThrows(UserException.class, () -> {
            reviewController.createReviewHandler(req, jwt);
        });

        assertEquals("Invalid token", ex.getMessage());
    }
    @Test
    void testGetProductsReviewHandler_EmptyList() {
        Long productId = 10L;

        when(reviewService.getAllReview(productId)).thenReturn(List.of());

        ResponseEntity<List<Review>> response = reviewController.getProductsReviewHandler(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        verify(reviewService).getAllReview(productId);
    }
    @Test
    void testGetProductsReviewHandler_NullList() {
        Long productId = 2L;

        when(reviewService.getAllReview(productId)).thenReturn(null);

        ResponseEntity<List<Review>> response = reviewController.getProductsReviewHandler(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertNull(response.getBody());

        verify(reviewService).getAllReview(productId);
    }
    @Test
    void testGetProductsReviewHandler_MultipleReviewsFromSameUser() {
        Long productId = 1L;
        User user = new User();
        user.setId(101L);

        Review r1 = new Review();
        r1.setReview("Good");
        r1.setUser(user);

        Review r2 = new Review();
        r2.setReview("Updated review");
        r2.setUser(user);

        List<Review> reviews = Arrays.asList(r1, r2);

        when(reviewService.getAllReview(productId)).thenReturn(reviews);

        ResponseEntity<List<Review>> response = reviewController.getProductsReviewHandler(productId);

        assertEquals(2, response.getBody().size());
        assertEquals("Good", response.getBody().get(0).getReview());
        assertEquals("Updated review", response.getBody().get(1).getReview());
    }
    @Test
    void testCreateReviewHandler_LongText() throws UserException, ProductException {
        String jwt = "valid-jwt";
        String longReview = "Excellent product. ".repeat(100); // very long string

        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setReview(longReview);

        User user = new User();
        Review review = new Review();
        review.setReview(longReview);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(reviewService.createReview(req, user)).thenReturn(review);

        ResponseEntity<Review> response = reviewController.createReviewHandler(req, jwt);

        assertEquals(longReview, response.getBody().getReview());
        assertTrue(response.getBody().getReview().length() > 1000);
    }

    
    
}

