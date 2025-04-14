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

@ExtendWith(MockitoExtension.class)
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
        CreateProductRequest request = new CreateProductRequest();
        Product product = new Product();
        when(productService.createProduct(request)).thenReturn(product);

        mockMvc.perform(post("/api/admin/products/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Product1\", \"price\": 100}"))
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
    void testUpdateProduct() throws Exception {
        Long productId = 1L;
        Product product = new Product();
        when(productService.updateProduct(productId, product)).thenReturn(product);

        mockMvc.perform(put("/api/admin/products/{productId}/update", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated Product\", \"price\": 150}"))
               .andExpect(status().isOk());
    }

    @Test
    void testCreateMultipleProducts() throws Exception {
        CreateProductRequest[] requests = {new CreateProductRequest()};
        mockMvc.perform(post("/api/admin/products/creates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("[{\"name\": \"Product1\", \"price\": 100}, {\"name\": \"Product2\", \"price\": 200}]"))
               .andExpect(status().isAccepted())
               .andExpect(jsonPath("$.message").value("products created successfully"));
    }
}

