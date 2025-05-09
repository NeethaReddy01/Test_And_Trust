package com.backend.service;

import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.modal.Rating;
import com.backend.modal.User;
import com.backend.request.RatingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingServicesTest {

    @Mock
    private RatingServices ratingServices;

    private RatingRequest ratingRequest;
    private User sampleUser;
    private Rating sampleRating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        Product product = new Product();
        product.setId(1L);

        sampleUser = new User();
        sampleUser.setId(1L);
        sampleUser.setEmail("user@example.com");

        ratingRequest = new RatingRequest();
        ratingRequest.setProductId(1L);
        ratingRequest.setRating(4.5);
        ratingRequest.setReview("Great product!");

        sampleRating = new Rating();
        sampleRating.setProduct(product);
        sampleRating.setRating(4.5);
        sampleRating.setReview("Great product!");
        sampleRating.setUser(sampleUser);
    }

    @Test
    void testCreateRating() throws ProductException {
        when(ratingServices.createRating(ratingRequest, sampleUser)).thenReturn(sampleRating);

        Rating result = ratingServices.createRating(ratingRequest, sampleUser);

        assertNotNull(result);
        assertEquals(1L, result.getProduct().getId());
        assertEquals(4.5, result.getRating());
        assertEquals("Great product!", result.getReview());
        assertEquals(sampleUser, result.getUser());
    }

    @Test
    void testGetProductsRating() {
        List<Rating> ratings = Arrays.asList(sampleRating);
        when(ratingServices.getProductsRating(1L)).thenReturn(ratings);

        List<Rating> result = ratingServices.getProductsRating(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(4.5, result.get(0).getRating());
    }

    @Test
    void testCreateRating_ThrowsProductException() throws ProductException {
        when(ratingServices.createRating(ratingRequest, sampleUser))
                .thenThrow(new ProductException("Product not found"));

        assertThrows(ProductException.class, () -> {
            ratingServices.createRating(ratingRequest, sampleUser);
        });
    }
}
