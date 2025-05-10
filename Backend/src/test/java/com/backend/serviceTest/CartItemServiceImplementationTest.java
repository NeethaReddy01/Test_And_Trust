package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.exception.CartItemException;
import com.backend.exception.UserException;
import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.Product;
import com.backend.modal.User;
import com.backend.repository.CartItemRepository;
import com.backend.repository.CartRepository;
import com.backend.service.CartItemServiceImplementation;
import com.backend.service.UserService;

public class CartItemServiceImplementationTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private UserService userService;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartItemServiceImplementation cartItemService;

    private CartItem cartItem;
    private Product product;
    private User user;
    private Cart cart;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create a user
        user = new User();
        user.setId(1L);

        // Create a product
        product = new Product();
        product.setId(1L);
        product.setPrice(100);
        product.setDiscountedPrice(80);

        // Create a cart item
        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setUserId(1L);
        cartItem.setProduct(product);
        cartItem.setQuantity(1);
        cartItem.setPrice(product.getPrice());
        cartItem.setDiscountedPrice(product.getDiscountedPrice());

        // Create a cart
        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
    }

    @Test
    public void testCreateCartItem() {
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(cartItem);

        CartItem createdCartItem = cartItemService.createCartItem(cartItem);

        assertNotNull(createdCartItem);
        assertEquals(cartItem.getProduct().getPrice(), createdCartItem.getPrice());
        assertEquals(cartItem.getProduct().getDiscountedPrice(), createdCartItem.getDiscountedPrice());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
    }

 



    @Test
    public void testIsCartItemExist() {
        when(cartItemRepository.isCartItemExist(any(Cart.class), any(Product.class), anyString(), eq(1L)))
                .thenReturn(cartItem);

        CartItem existingCartItem = cartItemService.isCartItemExist(cart, product, "M", 1L);

        assertNotNull(existingCartItem);
        assertEquals(cartItem.getId(), existingCartItem.getId());
        verify(cartItemRepository, times(1)).isCartItemExist(any(Cart.class), any(Product.class), anyString(), eq(1L));
    }

    @Test
    public void testRemoveCartItemWhenUserIsAuthorized() throws CartItemException, UserException {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));
        when(userService.findUserById(1L)).thenReturn(user);

        cartItemService.removeCartItem(1L, 1L);

        verify(cartItemRepository, times(1)).deleteById(1L);
    }

 

    @Test
    public void testFindCartItemById() throws CartItemException {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.of(cartItem));

        CartItem foundCartItem = cartItemService.findCartItemById(1L);

        assertNotNull(foundCartItem);
        assertEquals(cartItem.getId(), foundCartItem.getId());
        verify(cartItemRepository, times(1)).findById(1L);
    }

    @Test
    public void testFindCartItemByIdWhenNotFound() {
        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        CartItemException exception = assertThrows(CartItemException.class, () -> {
            cartItemService.findCartItemById(1L);
        });

        assertEquals("cartItem not found with id : 1", exception.getMessage());
    }
}
