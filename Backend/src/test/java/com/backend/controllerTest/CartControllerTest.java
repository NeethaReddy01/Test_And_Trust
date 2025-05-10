package com.backend.controllerTest;

import com.backend.controller.CartController;
import com.backend.exception.ProductException;
import com.backend.exception.UserException;
import com.backend.modal.Cart;
import com.backend.modal.CartItem;
import com.backend.modal.User;
import com.backend.request.AddItemRequest;
import com.backend.service.CartService;
import com.backend.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @InjectMocks
    private CartController cartController;

    private User mockUser;
    private Cart mockCart;
    private CartItem mockItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setEmail("user@example.com");

        mockCart = new Cart();
        mockCart.setUser(mockUser);

        mockItem = new CartItem();
        mockItem.setId(1L);
    }

    @Test
    public void testFindUserCartHandler_Success() throws UserException {
        String jwt = "valid-token";

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.findUserCart(mockUser.getId())).thenReturn(mockCart);

        ResponseEntity<Cart> response = cartController.findUserCartHandler(jwt);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCart, response.getBody());
    }

    @Test
    public void testAddItemToCart_Success() throws UserException, ProductException {
        String jwt = "valid-token";
        AddItemRequest request = new AddItemRequest();
        request.setProductId(100L);
        request.setQuantity(2);
        request.setSize("M");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.addCartItem(mockUser.getId(), request)).thenReturn(mockItem);

        ResponseEntity<CartItem> response = cartController.addItemToCart(request, jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(mockItem, response.getBody());
    }
   
    
    @Test
    public void testFindUserCartHandler_InvalidJwt() throws UserException {
        String jwt = "invalid-token";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(UserException.class);

        UserException exception = assertThrows(UserException.class, () -> {
            cartController.findUserCartHandler(jwt);
        });

        assertNotNull(exception);
    }
    

    @Test
    public void testAddItemToCart_ProductNotFound() throws UserException, ProductException {
        String jwt = "valid-token";
        AddItemRequest request = new AddItemRequest();
        request.setProductId(9999L);  // Non-existent product ID
        request.setQuantity(2);
        request.setSize("M");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.addCartItem(mockUser.getId(), request)).thenThrow(ProductException.class);

        ProductException exception = assertThrows(ProductException.class, () -> {
            cartController.addItemToCart(request, jwt);
        });

        assertNotNull(exception);
    }

    @Test
    public void testAddItemToCart_CartEmpty() throws UserException, ProductException {
        String jwt = "valid-token";
        AddItemRequest request = new AddItemRequest();
        request.setProductId(100L);
        request.setQuantity(2);
        request.setSize("M");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.addCartItem(mockUser.getId(), request)).thenReturn(mockItem);

        ResponseEntity<CartItem> response = cartController.addItemToCart(request, jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(mockItem, response.getBody());
    }

    @Test
    public void testFindUserCartHandler_ExceptionWhileFetchingUser() throws UserException {
        String jwt = "valid-token";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Error fetching user"));

        UserException exception = assertThrows(UserException.class, () -> {
            cartController.findUserCartHandler(jwt);
        });

        assertEquals("Error fetching user", exception.getMessage());
    }
//......
    @Test
    public void testClearCart_Success() throws Exception {
        String jwt = "valid-token";

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        doNothing().when(cartService).clearCart(mockUser.getId());

        ResponseEntity<?> response = cartController.clearCart(jwt);

        assertEquals(200, response.getStatusCodeValue());
       // assertTrue(((com.backend.response.ApiResponse) response.getBody()).getStatus());
        assertEquals("Cart cleared successfully", ((com.backend.response.ApiResponse) response.getBody()).getMessage());
    }
    @Test
    public void testClearCart_UserException() throws Exception {
        String jwt = "invalid-token";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid token"));

        UserException exception = assertThrows(UserException.class, () -> {
            cartController.clearCart(jwt);
        });

        assertEquals("Invalid token", exception.getMessage());
    }
    @Test
    public void testClearCart_CartItemException() throws Exception {
        String jwt = "valid-token";

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        doThrow(new com.backend.exception.CartItemException("Failed to clear cart"))
            .when(cartService).clearCart(mockUser.getId());

        assertThrows(com.backend.exception.CartItemException.class, () -> {
            cartController.clearCart(jwt);
        });
    }
    @Test
    public void testAddItemToCart_NullFields() throws Exception {
        String jwt = "valid-token";
        AddItemRequest request = new AddItemRequest(); // all fields null

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.addCartItem(mockUser.getId(), request)).thenReturn(mockItem);

        ResponseEntity<CartItem> response = cartController.addItemToCart(request, jwt);

        assertEquals(202, response.getStatusCodeValue());
    }
    @Test
    public void testFindUserCartHandler_WithCartDetails() throws Exception {
        String jwt = "valid-token";

        Cart cart = new Cart();
        cart.setUser(mockUser);
        cart.setTotalItem(2);
        cart.setTotalPrice(1000);
        cart.setTotalDiscountedPrice(800);
        cart.setDiscounte(200);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.findUserCart(mockUser.getId())).thenReturn(cart);

        ResponseEntity<Cart> response = cartController.findUserCartHandler(jwt);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().getTotalItem());
        assertEquals(1000, response.getBody().getTotalPrice());
        assertEquals(800, response.getBody().getTotalDiscountedPrice());
        assertEquals(200, response.getBody().getDiscounte());
    }

    @Test
    public void testAddItemToCart_UserException() throws UserException, ProductException {
        String jwt = "invalid-token";
        AddItemRequest request = new AddItemRequest();
        request.setProductId(101L);
        request.setQuantity(1);
        request.setSize("L");

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Invalid JWT"));

        UserException exception = assertThrows(UserException.class, () -> {
            cartController.addItemToCart(request, jwt);
        });

        assertEquals("Invalid JWT", exception.getMessage());
    }
    @Test
    public void testFindUserCartHandler_EmptyCart() throws UserException {
        String jwt = "valid-token";
        Cart emptyCart = new Cart();
        emptyCart.setUser(mockUser);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.findUserCart(mockUser.getId())).thenReturn(emptyCart);

        ResponseEntity<Cart> response = cartController.findUserCartHandler(jwt);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(0, response.getBody().getCartItems().size());
    }
    @Test
    public void testAddItemToCart_ItemAlreadyExists() throws Exception {
        String jwt = "valid-token";
        AddItemRequest request = new AddItemRequest();
        request.setProductId(100L);
        request.setQuantity(1);
        request.setSize("M");

        CartItem existingItem = new CartItem();
        existingItem.setId(99L);

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.addCartItem(mockUser.getId(), request)).thenReturn(existingItem);

        ResponseEntity<CartItem> response = cartController.addItemToCart(request, jwt);

        assertEquals(202, response.getStatusCodeValue());
        assertEquals(existingItem, response.getBody());
    }
    @Test
    public void testClearCart_AlreadyEmpty() throws Exception {
        String jwt = "valid-token";

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        doNothing().when(cartService).clearCart(mockUser.getId());

        ResponseEntity<?> response = cartController.clearCart(jwt);

        assertEquals(200, response.getStatusCodeValue());
    }
    @Test
    public void testAddItemToCart_InternalError() throws Exception {
        String jwt = "valid-token";
        AddItemRequest request = new AddItemRequest();
        request.setProductId(1L);
        request.setQuantity(1);
        request.setSize("L");

        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
        when(cartService.addCartItem(mockUser.getId(), request)).thenThrow(new NullPointerException("Null occurred"));

        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            cartController.addItemToCart(request, jwt);
        });

        assertEquals("Null occurred", exception.getMessage());
    }

}

