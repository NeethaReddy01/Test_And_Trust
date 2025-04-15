package com.backend.serviceTest;

import com.backend.exception.ProductException;
import com.backend.modal.*;
import com.backend.repository.CartRepository;
import com.backend.request.AddItemRequest;
import com.backend.service.CartItemService;
import com.backend.service.CartServiceImplementation;
import com.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemService cartItemService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartServiceImplementation cartService;

    private User mockUser;
    private Cart mockCart;
    private Product mockProduct;
    private CartItem mockItem;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");

        mockCart = new Cart();
        mockCart.setUser(mockUser);
        mockCart.setCartItems(new HashSet<>());

        mockProduct = new Product();
        mockProduct.setId(101L);
        mockProduct.setPrice(100);
        mockProduct.setDiscountedPrice(80);

        mockItem = new CartItem();
        mockItem.setId(1L);
        mockItem.setProduct(mockProduct);
        mockItem.setSize("M");
        mockItem.setQuantity(2);
        mockItem.setPrice(160);
        mockItem.setDiscountedPrice(160);
        mockItem.setUserId(1L);
    }

    @Test
    public void testCreateCart_Success() {
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        Cart createdCart = cartService.createCart(mockUser);

        assertNotNull(createdCart);
        assertEquals(mockUser, createdCart.getUser());
    }
    @Test
    public void testFindUserCart_Success() {
        mockCart.getCartItems().add(mockItem);

        when(cartRepository.findByUserId(1L)).thenReturn(mockCart);
        when(cartRepository.save(any(Cart.class))).thenReturn(mockCart);

        Cart cart = cartService.findUserCart(1L);

        assertNotNull(cart);
        assertEquals(2, cart.getTotalItem()); 
        assertEquals(160, cart.getTotalDiscountedPrice());
        assertEquals(160, cart.getTotalPrice());
    }



    @Test
    public void testAddCartItem_NewItem() throws ProductException {
      
        AddItemRequest req = new AddItemRequest();
        req.setProductId(101L);
        req.setSize("M");
        req.setQuantity(2);

        CartItem createdCartItem = new CartItem();
        createdCartItem.setId(1L);
        createdCartItem.setProduct(mockProduct);
        createdCartItem.setSize("M");
        createdCartItem.setQuantity(2);
        createdCartItem.setPrice(160);
        createdCartItem.setDiscountedPrice(160);
        createdCartItem.setUserId(1L);

        when(cartRepository.findByUserId(1L)).thenReturn(mockCart);
        when(productService.findProductById(101L)).thenReturn(mockProduct);
        when(cartItemService.isCartItemExist(mockCart, mockProduct, "M", 1L)).thenReturn(null);
        when(cartItemService.createCartItem(any(CartItem.class))).thenReturn(createdCartItem);

        
        CartItem result = cartService.addCartItem(1L, req);

        
        assertNotNull(result);
        assertEquals("M", result.getSize());
        assertEquals(2, result.getQuantity());
        assertEquals(160, result.getPrice());
        assertEquals(160, result.getDiscountedPrice());
        verify(cartItemService).createCartItem(any(CartItem.class));
        verify(cartRepository, never()).save(mockCart); // since addCartItem doesn't persist cart in current logic
    }

}

