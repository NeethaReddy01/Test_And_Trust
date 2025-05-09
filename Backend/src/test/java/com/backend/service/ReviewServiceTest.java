package com.backend.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.backend.modal.Review;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ReviewServiceTest {

    @Mock
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReview() {
        Long productId = 2L;

        Review r1 = new Review();
        Review r2 = new Review();
        List<Review> reviews = Arrays.asList(r1, r2);

        when(reviewService.getAllReview(productId)).thenReturn(reviews);

        List<Review> result = reviewService.getAllReview(productId);

        assertEquals(2, result.size());
        verify(reviewService, times(1)).getAllReview(productId);
    }
}
