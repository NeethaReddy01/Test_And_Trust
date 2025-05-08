package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.Product;
import com.backend.modal.User;
import com.backend.repository.CartItemRepository;
import com.backend.service.CartItemServiceImplementation;
import com.backend.service.UserService;

class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartItemServiceImplementation cartItemService;

    private User user;
    private Product product;
    private Cart cart;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        product = new Product();
        product.setPrice(100);
        product.setDiscountedPrice(80);

        cart = new Cart();

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setProduct(product);
        cartItem.setSize("M");
        cartItem.setUserId(1L);
        cartItem.setCart(cart);
        cartItem.setQuantity(1);
    }

    @Test
    void testCreateCartItem_Success() {
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartItemService.createCartItem(cartItem);

        assertEquals(cartItem, result);
        assertEquals(100, result.getPrice());
        assertEquals(80, result.getDiscountedPrice());
        verify(cartItemRepository, times(1)).save(cartItem);
    }

    @Test
    void testUpdateCartItem_Success() throws Exception {
        CartItem updatedItem = new CartItem();
        updatedItem.setQuantity(3);

        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(1L)).thenReturn(user);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem result = cartItemService.updateCartItem(1L, 1L, updatedItem);

        assertEquals(3, result.getQuantity());
        assertEquals(300, result.getPrice());
        assertEquals(240, result.getDiscountedPrice());
        verify(cartItemRepository).save(cartItem);
    }

    @Test
    void testIsCartItemExist_Success() {
        when(cartItemRepository.isCartItemExist(cart, product, "M", 1L)).thenReturn(cartItem);

        CartItem result = cartItemService.isCartItemExist(cart, product, "M", 1L);

        assertEquals(cartItem, result);
        verify(cartItemRepository).isCartItemExist(cart, product, "M", 1L);
    }

    @Test
    void testRemoveCartItem_Success() throws Exception {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(1L)).thenReturn(user);

        cartItemService.removeCartItem(1L, 1L);

        verify(cartItemRepository).deleteById(1L);
    }

    @Test
    void testFindCartItemById_Success() throws Exception {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        CartItem result = cartItemService.findCartItemById(1L);

        assertEquals(cartItem, result);
        verify(cartItemRepository).findById(1L);
    }
}
