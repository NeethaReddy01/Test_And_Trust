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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartItemControllerTest {

    @Mock
    private CartItemService cartItemService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartItemController cartItemController;

    private User user;
    private CartItem cartItem;
    private User testUser;
    private CartItem testCartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUserId(1L);
        cartItem.setQuantity(2);
    }

  

    @Test
    public void testDeleteCartItemHandler_InvalidUser() throws CartItemException, UserException {
        when(userService.findUserProfileByJwt("jwt")).thenThrow(new UserException("Invalid User"));

        assertThrows(UserException.class, () -> {
            cartItemController.deleteCartItemHandler(1L, "jwt");
        });
    }

    @Test
    public void testDeleteCartItemHandler_CartItemNotFound() throws CartItemException, UserException {
        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        doThrow(new CartItemException("Item not found")).when(cartItemService).removeCartItem(1L, 1L);

        assertThrows(CartItemException.class, () -> {
            cartItemController.deleteCartItemHandler(1L, "jwt");
        });

        testUser = new User();
        testUser.setId(1L);
        testUser.setEmail("test@example.com");

        testCartItem = new CartItem();
        testCartItem.setId(1L);
        testCartItem.setUserId(1L);
        testCartItem.setQuantity(2);
    }

    @Test
    public void testUpdateCartItemHandler_Success() throws CartItemException, UserException {
        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        when(cartItemService.updateCartItem(1L, 1L, cartItem)).thenReturn(cartItem);

        ResponseEntity<CartItem> response = cartItemController.updateCartItemHandler(1L, cartItem, "jwt");

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(cartItem, response.getBody());
    }

    @Test
    public void testUpdateCartItemHandler_InvalidUser() throws CartItemException, UserException {
        when(userService.findUserProfileByJwt("jwt")).thenThrow(new UserException("Invalid JWT"));

        assertThrows(UserException.class, () -> {
            cartItemController.updateCartItemHandler(1L, cartItem, "jwt");
        });
    }

    @Test
    public void testUpdateCartItemHandler_NullQuantity() throws CartItemException, UserException {
        cartItem.setQuantity(0);
        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        when(cartItemService.updateCartItem(1L, 1L, cartItem)).thenReturn(cartItem);

        ResponseEntity<CartItem> response = cartItemController.updateCartItemHandler(1L, cartItem, "jwt");
        assertEquals(202, response.getStatusCodeValue());
        assertEquals(cartItem, response.getBody());
    }

    @Test
    public void testUpdateCartItemHandler_MismatchedUserId() throws CartItemException, UserException {
        user.setId(1L);
        cartItem.setUserId(2L); // mismatch

        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        when(cartItemService.updateCartItem(1L, 1L, cartItem))
            .thenThrow(new CartItemException("You can't update another user's cart item"));

        assertThrows(CartItemException.class, () -> {
            cartItemController.updateCartItemHandler(1L, cartItem, "jwt");
        });
    }

    @Test
    public void testDeleteCartItemHandler_UserMismatch() throws CartItemException, UserException {
        user.setId(1L);

        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        doThrow(new UserException("Mismatch")).when(cartItemService).removeCartItem(1L, 1L);

        assertThrows(UserException.class, () -> {
            cartItemController.deleteCartItemHandler(1L, "jwt");
        });
    }

    @Test
    public void testUpdateCartItemHandler_NegativeQuantity() throws CartItemException, UserException {
        cartItem.setQuantity(-5);

        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        when(cartItemService.updateCartItem(1L, 1L, cartItem)).thenReturn(cartItem);

        ResponseEntity<CartItem> response = cartItemController.updateCartItemHandler(1L, cartItem, "jwt");

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(-5, response.getBody().getQuantity());
    }

    @Test
    public void testUpdateCartItemHandler_ZeroQuantity() throws CartItemException, UserException {
        cartItem.setQuantity(0);
        when(userService.findUserProfileByJwt("jwt")).thenReturn(user);
        when(cartItemService.updateCartItem(1L, 1L, cartItem)).thenReturn(cartItem);

        ResponseEntity<CartItem> response = cartItemController.updateCartItemHandler(1L, cartItem, "jwt");
        assertEquals(202, response.getStatusCodeValue());
    }
}
