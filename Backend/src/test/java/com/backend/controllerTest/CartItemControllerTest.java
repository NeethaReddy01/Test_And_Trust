package com.backend.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.backend.controller.CartItemController;
import com.backend.exception.CartItemException;
import com.backend.exception.UserException;
import com.backend.modal.CartItem;
import com.backend.modal.User;
import com.backend.response.ApiResponse;
import com.backend.service.CartItemService;
import com.backend.service.UserService;

public class CartItemControllerTest {

    @Mock
    private CartItemService cartItemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartItemController cartItemController;

    private User testUser;
    private CartItem testCartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUserId(1L);
        testCartItem.setQuantity(2);
    }

    @Test
    public void testDeleteCartItemHandler_Success() throws CartItemException, UserException {
        String jwt = "mock-jwt";
        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        doNothing().when(cartItemService).removeCartItem(testUser.getId(), 1L);

        // Call the correct method name
        ResponseEntity<ApiResponse> response = cartItemController.deleteCartItemHandler(1L, jwt);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals("Item Remove From Cart", response.getBody().getMessage());
        //assertTrue(response.getBody().isSuccess());
    }

    @Test
    public void testUpdateCartItemHandler_Success() throws CartItemException, UserException {
        String jwt = "mock-jwt";
        CartItem updateRequest = new CartItem();
        updateRequest.setQuantity(5);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(testUser);
        when(cartItemService.updateCartItem(testUser.getId(), 1L, updateRequest)).thenReturn(testCartItem);

        // Call the correct method name
        ResponseEntity<CartItem> response = cartItemController.updateCartItemHandler(1L, updateRequest, jwt);

        assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
        assertEquals(testCartItem, response.getBody());
    }
}
