package com.backend.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.modal.Rating;
import com.backend.modal.User;
import com.backend.repository.RatingRepository;
import com.backend.request.RatingRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class RatingServiceImplementationTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RatingServiceImplementation ratingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRating() throws ProductException {
        RatingRequest request = new RatingRequest();
        request.setProductId(1L);
        request.setRating(4);

        Product product = new Product();
        product.setId(1L);
        User user = new User();
        user.setId(10L);

        Rating savedRating = new Rating();
        savedRating.setId(1L);
        savedRating.setRating(4);
        savedRating.setProduct(product);
        savedRating.setUser(user);
        savedRating.setCreatedAt(LocalDateTime.now());

        when(productService.findProductById(1L)).thenReturn(product);
        when(ratingRepository.save(any(Rating.class))).thenReturn(savedRating);

        Rating result = ratingService.createRating(request, user);

        assertNotNull(result);
        assertEquals(4, result.getRating());
        assertEquals(product, result.getProduct());
        assertEquals(user, result.getUser());

        verify(productService, times(1)).findProductById(1L);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testGetProductsRating() {
        Long productId = 2L;
        Rating rating1 = new Rating();
        Rating rating2 = new Rating();

        List<Rating> ratings = Arrays.asList(rating1, rating2);

        when(ratingRepository.getAllProductsRating(productId)).thenReturn(ratings);

        List<Rating> result = ratingService.getProductsRating(productId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(ratingRepository, times(1)).getAllProductsRating(productId);
    }
}
