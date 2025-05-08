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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.exception.CartItemException;
import com.backend.exception.ProductException;
import com.backend.exception.UserException;
import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.Product;
import com.backend.modal.User;
import com.backend.repository.CartItemRepository;
import com.backend.repository.CartRepository;
import com.backend.repository.UserRepository;
import com.backend.request.AddItemRequest;
import com.backend.service.CartItemService;
import com.backend.service.CartServiceImplementation;
import com.backend.service.ProductService;

public class CartServiceImplementationTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImplementation cartService;

    private User user;
    private Cart cart;
    private Product product;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup test user and cart
        user = new User();
        user.setId(1L);

        cart = new Cart();
        cart.setUser(user);
        //cart.setCartItems(new ArrayList<>());
        cart.setId(1L);

        product = new Product();
        product.setId(1L);
        product.setDiscountedPrice(100);
    }

    @Test
    public void testCreateCart() {
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.createCart(user);

        assertNotNull(createdCart);
        assertEquals(user, createdCart.getUser());
        verify(cartRepository, times(1)).save(any(Cart.class));
    }

    @Test
    public void testFindUserCart() {
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setPrice(100);
        cartItem.setDiscountedPrice(80);
        cartItem.setQuantity(1);
        cartItems.add(cartItem);
       // cart.setCartItems(cartItems);
        
        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        Cart foundCart = cartService.findUserCart(1L);

        assertNotNull(foundCart);
        assertEquals(100, foundCart.getTotalPrice());
        assertEquals(80, foundCart.getTotalDiscountedPrice());
        assertEquals(20, foundCart.getDiscounte());
        assertEquals(1, foundCart.getTotalItem());
        verify(cartRepository, times(1)).findByUserId(1L);
    }

    @Test
    public void testAddCartItem() throws ProductException {
        AddItemRequest request = new AddItemRequest();
        request.setProductId(1L);
        request.setQuantity(2);
        request.setSize("M");

        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(productService.findProductById(1L)).thenReturn(product);
        when(cartItemService.isCartItemExist(any(Cart.class), any(Product.class), anyString(), eq(1L)))
                .thenReturn(null);
        when(cartItemService.createCartItem(any(CartItem.class))).thenReturn(new CartItem());

        CartItem cartItem = cartService.addCartItem(1L, request);

        assertNotNull(cartItem);
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(productService, times(1)).findProductById(1L);
        verify(cartItemService, times(1)).createCartItem(any(CartItem.class));
    }

    @Test
    public void testClearCart() throws UserException, CartItemException {
        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cart.setCartItems((Set<CartItem>) List.of(cartItem));

        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        cartService.clearCart(1L);

        assertEquals(0, cart.getCartItems().size());
        assertEquals(0, cart.getTotalPrice());
        assertEquals(0, cart.getTotalItem());
        assertEquals(0, cart.getTotalDiscountedPrice());
        assertEquals(0, cart.getDiscounte());
        verify(cartRepository, times(1)).save(any(Cart.class));
        verify(cartItemService, times(1)).removeCartItem(eq(1L), eq(1L));
    }

    @Test
    public void testClearCartWhenCartNotFound() throws UserException, CartItemException {
        when(cartRepository.findByUserId(1L)).thenReturn(null);

        UserException exception = assertThrows(UserException.class, () -> {
            cartService.clearCart(1L);
        });

        assertEquals("Cart not found for user id: 1", exception.getMessage());
    }

    @Test
    public void testAddCartItemWhenItemAlreadyExists() throws ProductException {
        AddItemRequest request = new AddItemRequest();
        request.setProductId(1L);
        request.setQuantity(2);
        request.setSize("M");

        CartItem existingCartItem = new CartItem();
        existingCartItem.setQuantity(2);
        existingCartItem.setPrice(200);
        existingCartItem.setDiscountedPrice(180);

        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(productService.findProductById(1L)).thenReturn(product);
        when(cartItemService.isCartItemExist(cart, product, "M", 1L)).thenReturn(existingCartItem);

        CartItem cartItem = cartService.addCartItem(1L, request);

        assertNotNull(cartItem);
        assertEquals(2, cartItem.getQuantity());
        verify(cartRepository, times(1)).findByUserId(1L);
        verify(productService, times(1)).findProductById(1L);
        verify(cartItemService, times(1)).isCartItemExist(cart, product, "M", 1L);
    }
}
