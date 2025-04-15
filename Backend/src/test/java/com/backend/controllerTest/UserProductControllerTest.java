package com.backend.controllerTest;

import com.backend.controller.UserProductController;
import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserProductControllerTest {

    @InjectMocks
    private UserProductController userProductController;

    @Mock
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    public void setup() {
        // Initializes mocks
        MockitoAnnotations.openMocks(this);

        // Creating a sample product for tests
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setTitle("Sample Product");
        sampleProduct.setBrand("Sample Brand");
        sampleProduct.setDescription("Sample Description");
        sampleProduct.setPrice((int)200.0);  // Price should be double, not int
    }

    @Test
    public void testFindProductByCategoryHandler_Success() {
        // Mock behavior for getting products by category
        String category = "electronics";
        when(productService.getAllProduct(category)).thenReturn(Arrays.asList(sampleProduct));

        // Call the controller method
        ResponseEntity<List<Product>> response = userProductController.findProductByCategoryHandler(category);

        // Verify that the response is correct
        assertEquals(202, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Sample Product", response.getBody().get(0).getTitle());
    }

    @Test
    public void testFindProductByIdHandler_Success() throws ProductException {
        // Mock behavior for getting a product by ID
        Long productId = 1L;
        when(productService.findProductById(productId)).thenReturn(sampleProduct);

        // Call the controller method
        ResponseEntity<Product> response = userProductController.findProductByIdHandler(productId);

        // Verify that the response is correct
        assertEquals(202, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Sample Product", response.getBody().getTitle());
    }

    @Test
    public void testFindProductByIdHandler_ProductNotFound() throws ProductException {
        // Mock behavior to throw exception when product is not found
        Long productId = 999L;
        when(productService.findProductById(productId)).thenThrow(new ProductException("Product not found"));

        // Call the controller method and assert that the exception is thrown
        ProductException exception = assertThrows(ProductException.class, () -> {
            userProductController.findProductByIdHandler(productId);
        });

        // Verify the exception message
        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    public void testSearchProductHandler_Success() {
        // Mock behavior for searching products
        String query = "sample";
        when(productService.searchProduct(query)).thenReturn(Arrays.asList(sampleProduct));

        // Call the controller method
        ResponseEntity<List<Product>> response = userProductController.searchProductHandler(query);

        // Verify that the response is correct
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Sample Product", response.getBody().get(0).getTitle());
    }

    @Test
    public void testSearchProductHandler_EmptyResult() {
        // Mock behavior for searching products with no result
        String query = "notfound";
        when(productService.searchProduct(query)).thenReturn(Arrays.asList());

        // Call the controller method
        ResponseEntity<List<Product>> response = userProductController.searchProductHandler(query);

        // Verify that the response is correct
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }
}
