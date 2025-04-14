package com.backend.controllerTest;

import com.backend.controller.RatingController;
import com.backend.exception.ProductException;
import com.backend.exception.UserException;
import com.backend.modal.Rating;
import com.backend.modal.User;
import com.backend.request.RatingRequest;
import com.backend.service.RatingServices;
import com.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RatingControllerTest {

    @InjectMocks
    private RatingController ratingController; // ✅ FIXED HERE

    @Mock
    private UserService userService;

    @Mock
    private RatingServices ratingServices;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRatingHandler() throws UserException, ProductException {
        String jwt = "valid-jwt";
        RatingRequest request = new RatingRequest();
        request.setProductId(1L);
        request.setRating(4);

        User user = new User();
        user.setId(1L);

        Rating expectedRating = new Rating();
        expectedRating.setRating(4);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(user);
        when(ratingServices.createRating(request, user)).thenReturn(expectedRating);

        ResponseEntity<Rating> response = ratingController.createRatingHandler(request, jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(expectedRating.getRating(), response.getBody().getRating());

        verify(userService).findUserProfileByJwt(jwt);
        verify(ratingServices).createRating(request, user);
    }

    @Test
    void testGetProductsReviewHandler() {
        Long productId = 1L;
        Rating r1 = new Rating();
        r1.setRating(5);
        Rating r2 = new Rating();
        r2.setRating(3);

        List<Rating> ratings = Arrays.asList(r1, r2);

        when(ratingServices.getProductsRating(productId)).thenReturn(ratings);

        ResponseEntity<List<Rating>> response = ratingController.getProductsReviewHandler(productId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals(5, response.getBody().get(0).getRating());

        verify(ratingServices).getProductsRating(productId);
    }
}
