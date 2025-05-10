package com.backend.controllerTest;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import com.backend.service.ProductService;
import com.backend.modal.Product;
import com.backend.exception.ProductException;
import com.backend.controller.AdminProductController;
import com.backend.request.CreateProductRequest;
import com.backend.response.ApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.http.MediaType;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class AdminProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private AdminProductController adminProductController;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminProductController).build();
    }


    @Test
    void testCreateProduct() throws Exception {
        // Create request with all required fields
        CreateProductRequest request = new CreateProductRequest();
        request.setTitle("Product1");
        request.setPrice(100);
        request.setLevel1Category("Category1");
        request.setLevel2Category("SubCategory1");
        
        // Create a product with ID for the mock response
        Product product = new Product();
        product.setId(1L); // Set an ID so the test passes
        product.setTitle("Product1");
        product.setPrice(100);
        
        // Use Mockito's any() matcher since the actual object might not match exactly
        when(productService.createProduct(any(CreateProductRequest.class))).thenReturn(product);

        mockMvc.perform(post("/api/admin/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Product1\", \"price\": 100, \"level1Category\": \"Category1\", \"level2Category\": \"SubCategory1\"}"))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").exists());
    }


    @Test
    void testDeleteProduct() throws Exception {
        Long productId = 1L;
        String message = "Product deleted successfully";
        when(productService.deleteProduct(productId)).thenReturn(message);

        mockMvc.perform(delete("/api/admin/products/{productId}/delete", productId))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = List.of(new Product());
        when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/api/admin/products/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }
   
    @Test
    void testGetAllProducts_EmptyList() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/products/all"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void testGetRecentlyAddedProducts() throws Exception {
        when(productService.recentlyAddedProduct()).thenReturn(List.of(new Product()));

        mockMvc.perform(get("/api/admin/products/recent"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$").isArray());
    }
    @Test
    void testUpdateProduct() throws Exception {
        Long productId = 1L;
        Product request = new Product();
        request.setTitle("Updated Product");

        Product updatedProduct = new Product();
        updatedProduct.setId(productId);
        updatedProduct.setTitle("Updated Product");

        when(productService.updateProduct(eq(productId), any(Product.class))).thenReturn(updatedProduct);

        mockMvc.perform(put("/api/admin/products/{productId}/update", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Updated Product\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Product"));
    }
 
    @Test
    void testGetRecentlyAddedProducts_Empty() throws Exception {
        when(productService.recentlyAddedProduct()).thenReturn(List.of());

        mockMvc.perform(get("/api/admin/products/recent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    @Test
    void testInvalidMethodNotAllowed() throws Exception {
        mockMvc.perform(put("/api/admin/products/all")) // Only GET is allowed
               .andExpect(status().isMethodNotAllowed());
    }
    @Test
    void testCreateProduct_MissingFields() throws Exception {
        mockMvc.perform(post("/api/admin/products/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"price\": 100}")) // Missing title/category
                .andExpect(status().isAccepted()); // or BadRequest if validation exists
    }
}

