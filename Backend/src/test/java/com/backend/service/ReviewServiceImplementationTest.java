package com.backend.service;

import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.modal.Review;
import com.backend.modal.User;
import com.backend.repository.ProductRepository;
import com.backend.repository.ReviewRepository;
import com.backend.request.ReviewRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceImplementationTest {

    private ReviewRepository reviewRepository;
    private ProductService productService;
    private ProductRepository productRepository;

    private ReviewServiceImplementation reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);
        productService = mock(ProductService.class);
        productRepository = mock(ProductRepository.class);
        reviewService = new ReviewServiceImplementation(reviewRepository, productService, productRepository);
    }

    @Test
    void testCreateReview() throws ProductException {
        // Prepare data
        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setReview("Great product!");

        User user = new User();
        Product product = new Product();
        Review savedReview = new Review();
        savedReview.setReview("Great product!");

        // Mock behavior
        when(productService.findProductById(1L)).thenReturn(product);
        when(reviewRepository.save(any(Review.class))).thenReturn(savedReview);

        // Act
        Review result = reviewService.createReview(req, user);

        // Assert
        assertNotNull(result);
        assertEquals("Great product!", result.getReview());
        verify(productRepository, times(1)).save(product);
        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void testGetAllReview() {
        Long productId = 2L;

        Review r1 = new Review();
        Review r2 = new Review();
        List<Review> reviews = Arrays.asList(r1, r2);

        when(reviewRepository.getAllProductsReview(productId)).thenReturn(reviews);

        List<Review> result = reviewService.getAllReview(productId);

        assertEquals(2, result.size());
        verify(reviewRepository, times(1)).getAllProductsReview(productId);
    }
}
