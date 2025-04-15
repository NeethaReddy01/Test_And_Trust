package com.backend.serviceTest;


import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.modal.Review;
import com.backend.modal.User;
import com.backend.repository.ProductRepository;
import com.backend.repository.ReviewRepository;
import com.backend.request.ReviewRequest;
import com.backend.service.ProductService;
import com.backend.service.ReviewServiceImplementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewServiceImplementationTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ReviewServiceImplementation reviewService;

    private ReviewRequest reviewRequest;
    private User user;
    private Product product;
    private Review review;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        reviewRequest = new ReviewRequest();
        reviewRequest.setProductId(1L);
        reviewRequest.setReview("Test review");

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(1L);

        review = new Review();
        review.setId(1L);
        review.setReview("Test review");
        review.setProduct(product);
        review.setUser(user);
        review.setCreatedAt(LocalDateTime.now());
    }

    @Test
    public void testCreateReview() throws ProductException {
        when(productService.findProductById(1L)).thenReturn(product);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);

        Review result = reviewService.createReview(reviewRequest, user);

        assertNotNull(result);
        assertEquals("Test review", result.getReview());
        assertEquals(1L, result.getProduct().getId());
        verify(productService).findProductById(1L);
        verify(reviewRepository).save(any(Review.class));
        verify(productRepository).save(product);
    }

    @Test
    public void testGetAllReview() {
        when(reviewRepository.getAllProductsReview(1L)).thenReturn(Arrays.asList(review));

        List<Review> result = reviewService.getAllReview(1L);

        assertEquals(1, result.size());
        assertEquals("Test review", result.get(0).getReview());
        verify(reviewRepository).getAllProductsReview(1L);
    }
}

