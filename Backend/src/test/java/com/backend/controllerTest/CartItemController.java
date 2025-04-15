package com.backend.controllerTest;

import com.backend.exception.CartItemException;
import com.backend.exception.UserException;
import com.backend.modal.CartItem;
import com.backend.modal.User;
import com.backend.service.CartItemService;
import com.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CartItemController.class)
public class CartItemController {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartItemService cartItemService;

    @MockBean
    private UserService userService;

    private ObjectMapper objectMapper;

    private final String jwtToken = "mock-jwt-token";

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void deleteCartItem_Success() throws Exception {
        Long cartItemId = 1L;
        Long userId = 100L;

        User mockUser = new User();
        mockUser.setId(userId);

        when(userService.findUserProfileByJwt(jwtToken)).thenReturn(mockUser);
        doNothing().when(cartItemService).removeCartItem(userId, cartItemId);

        mockMvc.perform(delete("/api/cart_items/{cartItemId}", cartItemId)
                        .header("Authorization", jwtToken))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.message").value("Item Remove From Cart"))
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void deleteCartItem_Unauthorized() throws Exception {
        Long cartItemId = 1L;

        when(userService.findUserProfileByJwt(jwtToken)).thenThrow(new UserException("User not found"));

        mockMvc.perform(delete("/api/cart_items/{cartItemId}", cartItemId)
                        .header("Authorization", jwtToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateCartItem_Success() throws Exception {
        Long cartItemId = 1L;
        Long userId = 100L;

        User mockUser = new User();
        mockUser.setId(userId);

        CartItem requestCartItem = new CartItem();
        requestCartItem.setId(cartItemId);
        requestCartItem.setQuantity(2);

        CartItem updatedCartItem = new CartItem();
        updatedCartItem.setId(cartItemId);
        updatedCartItem.setQuantity(3);

        when(userService.findUserProfileByJwt(jwtToken)).thenReturn(mockUser);
        when(cartItemService.updateCartItem(userId, cartItemId, requestCartItem)).thenReturn(updatedCartItem);

        mockMvc.perform(put("/api/cart_items/{cartItemId}", cartItemId)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCartItem)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(cartItemId))
                .andExpect(jsonPath("$.quantity").value(3));
    }

    @Test
    void updateCartItem_NotFound() throws Exception {
        Long cartItemId = 1L;
        Long userId = 100L;

        User mockUser = new User();
        mockUser.setId(userId);

        CartItem requestCartItem = new CartItem();
        requestCartItem.setId(cartItemId);
        requestCartItem.setQuantity(2);

        when(userService.findUserProfileByJwt(jwtToken)).thenReturn(mockUser);
        when(cartItemService.updateCartItem(userId, cartItemId, requestCartItem))
                .thenThrow(new CartItemException("Cart item not found"));

        mockMvc.perform(put("/api/cart_items/{cartItemId}", cartItemId)
                        .header("Authorization", jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestCartItem)))
                .andExpect(status().isBadRequest());
    }
}
