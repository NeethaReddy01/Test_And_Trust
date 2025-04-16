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
    //.............
    
    @Test
    public void testFindUserCartHandler_InvalidJwt() throws UserException {
        String jwt = "invalid-token";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(UserException.class);

        UserException exception = assertThrows(UserException.class, () -> {
            cartController.findUserCartHandler(jwt);
        });

        assertNotNull(exception);
    }
    
//    @Test
//    public void testFindUserCartHandler_UserNotFound() throws UserException {
//        String jwt = "valid-token";
//
//        when(userService.findUserProfileByJwt(jwt)).thenReturn(null);
//
//        UserException exception = assertThrows(UserException.class, () -> {
//            cartController.findUserCartHandler(jwt);
//        });
//
//        assertEquals("User not found", exception.getMessage());
//    }
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
//    @Test
//    public void testAddItemToCart_InvalidRequestData() throws UserException, ProductException {
//        String jwt = "valid-token";
//        AddItemRequest request = new AddItemRequest();
//        request.setProductId(100L);
//        request.setQuantity(-1);  // Invalid quantity
//        request.setSize("M");
//
//        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
//
//        // Handle invalid input scenario
//        ProductException exception = assertThrows(ProductException.class, () -> {
//            cartController.addItemToCart(request, jwt);
//        });
//
//        assertNotNull(exception);
//    }
//   @Test
//    public void testAddItemToCart_UpdateExistingItem() throws UserException, ProductException {
//        String jwt = "valid-token";
//        AddItemRequest request = new AddItemRequest();
//        request.setProductId(100L);
//        request.setQuantity(2);
//        request.setSize("M");
//
//        CartItem existingItem = new CartItem();
//        existingItem.setQuantity(1);  // Existing item with 1 quantity
//        existingItem.setProduct(mockItem.getProduct());
//
//        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
//        when(cartService.addCartItem(mockUser.getId(), request)).thenReturn(existingItem);
//
//        ResponseEntity<CartItem> response = cartController.addItemToCart(request, jwt);
//
//        assertEquals(202, response.getStatusCodeValue());
//        assertEquals(existingItem, response.getBody());
//        assertEquals(3, existingItem.getQuantity());  // Updated quantity
//    }
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
//    @Test
//    public void testFindUserCartHandler_NoCart() throws UserException {
//        String jwt = "valid-token";
//
//        when(userService.findUserProfileByJwt(jwt)).thenReturn(mockUser);
//        when(cartService.findUserCart(mockUser.getId())).thenReturn(new Cart());
//
//        ResponseEntity<Cart> response = cartController.findUserCartHandler(jwt);
//
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//        assertEquals(0, response.getBody().getCartItems().size());
//    }
    @Test
    public void testFindUserCartHandler_ExceptionWhileFetchingUser() throws UserException {
        String jwt = "valid-token";

        when(userService.findUserProfileByJwt(jwt)).thenThrow(new UserException("Error fetching user"));

        UserException exception = assertThrows(UserException.class, () -> {
            cartController.findUserCartHandler(jwt);
        });

        assertEquals("Error fetching user", exception.getMessage());
    }


}

