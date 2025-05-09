package com.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(UserProductController.class)
@Import(UserProductControllerTest.TestSecurityConfig.class)
public class UserProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleProduct = new Product();
        sampleProduct.setId(1L);
        sampleProduct.setTitle("Sample Product");
        sampleProduct.setBrand("TestBrand");
    }

    @Test
    void testFindProductByIdHandler() throws Exception {
        when(productService.findProductById(1L)).thenReturn(sampleProduct);

        mockMvc.perform(get("/api/products/id/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Sample Product"));
    }

    @Test
    void testFindProductByIdHandler_ProductNotFound() throws Exception {
        when(productService.findProductById(99L)).thenThrow(new ProductException("Product not found"));

        mockMvc.perform(get("/api/products/id/99")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testSearchProductHandler() throws Exception {
        List<Product> products = Arrays.asList(sampleProduct);
        when(productService.searchProduct("Sample", "newest")).thenReturn(products);

        mockMvc.perform(get("/api/products/search")
                        .param("q", "Sample")
                        .param("sort", "newest")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Sample Product"));
    }

    @Test
    void testFindProductByCategoryHandler() throws Exception {
        List<Product> products = Arrays.asList(sampleProduct);
        when(productService.findProductByCategory("skincare")).thenReturn(products);

        mockMvc.perform(get("/api/products")
                        .param("category", "skincare")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].brand").value("TestBrand"));
    }

    // Disable security for testing
    @Configuration
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                .authorizeHttpRequests()
                .anyRequest().permitAll();
            return http.build();
        }
    }
}
