package com.backend.serviceTest;

import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.modal.Rating;
import com.backend.modal.User;
import com.backend.repository.RatingRepository;
import com.backend.request.RatingRequest;
import com.backend.service.ProductService;
import com.backend.service.RatingServiceImplementation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RatingServiceImplementationTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private RatingServiceImplementation ratingService; // ✅ FIXED HERE

    private RatingRequest ratingRequest;
    private User user;
    private Product product;
    private Rating rating;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        ratingRequest = new RatingRequest();
        ratingRequest.setProductId(101L);
        ratingRequest.setRating(4.5);

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setId(101L);

        rating = new Rating();
        rating.setId(1L);
        rating.setProduct(product);
        rating.setUser(user);
        rating.setRating(4.5);
    }

    @Test
    public void testCreateRating() throws ProductException {
        when(productService.findProductById(101L)).thenReturn(product);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.createRating(ratingRequest, user);

        assertNotNull(result);
        assertEquals(4.5, result.getRating());
        assertEquals(101L, result.getProduct().getId());
        verify(productService).findProductById(101L);
        verify(ratingRepository).save(any(Rating.class));
    }

    @Test
    public void testGetProductsRating() {
        when(ratingRepository.getAllProductsRating(101L)).thenReturn(Arrays.asList(rating));

        List<Rating> result = ratingService.getProductsRating(101L);

        assertEquals(1, result.size());
        assertEquals(4.5, result.get(0).getRating());
        verify(ratingRepository).getAllProductsRating(101L);
    }
}
