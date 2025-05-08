package com.backend.serviceTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.util.HashSet;

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
import com.backend.repository.CartRepository;
import com.backend.repository.UserRepository;
import com.backend.request.AddItemRequest;
import com.backend.service.CartItemService;
import com.backend.service.CartServiceImplementation;
import com.backend.service.ProductService;

public class CartServiceTest {

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
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        cart = new Cart();
        cart.setId(1L);
        cart.setUser(user);
        cart.setCartItems(new HashSet<>());

        product = new Product();
        product.setId(1L);
        product.setDiscountedPrice(100);

        cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setPrice(200);
        cartItem.setQuantity(2);
        cartItem.setDiscountedPrice(100);
    }

    @Test
    public void testCreateCart_Success() {
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.createCart(user);
        assertNotNull(createdCart);
        assertEquals(user, createdCart.getUser());
    }

    @Test
    public void testAddCartItem_Success() throws Exception {
        AddItemRequest req = new AddItemRequest();
        req.setProductId(1L);
        req.setQuantity(2);
        req.setSize("M");

        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(productService.findProductById(1L)).thenReturn(product);
        when(cartItemService.isCartItemExist(cart, product, "M", 1L)).thenReturn(null);
        when(cartItemService.createCartItem(any(CartItem.class))).thenReturn(cartItem);

        CartItem addedItem = cartService.addCartItem(1L, req);
        assertNotNull(addedItem);
        assertEquals(2, addedItem.getQuantity());
        assertEquals(product, addedItem.getProduct());
    }

    @Test
    public void testFindUserCart_Success() {
        cart.getCartItems().add(cartItem);

        when(cartRepository.findByUserId(1L)).thenReturn(cart);
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart foundCart = cartService.findUserCart(1L);
        assertNotNull(foundCart);
        assertEquals(user, foundCart.getUser());
        assertEquals(1, foundCart.getCartItems().size());
    }

    @Test
    public void testClearCart_Success() throws Exception {
        cart.getCartItems().add(cartItem);
        when(cartRepository.findByUserId(1L)).thenReturn(cart);

        cartService.clearCart(1L);

        verify(cartItemService).removeCartItem(1L, cartItem.getId());
        verify(cartRepository).save(cart);
        assertEquals(0, cart.getCartItems().size());
    }
}
