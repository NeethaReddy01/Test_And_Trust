package com.backend.controller;


import com.backend.model.*;
import com.backend.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")  
@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired private ProductService productService;
    
    @PostMapping 
    public ResponseEntity<Product> createProduct(@RequestBody Product product) { 
    	return ResponseEntity.ok(productService.saveProduct(product)); 
    	}
    
    @GetMapping 
    public ResponseEntity<List<Product>> getAllProducts() { 
    	return ResponseEntity.ok(productService.getAllProducts()); 
    	}
    
    @GetMapping("/{id}") 
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productService.getProductById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}") 
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id); return ResponseEntity.noContent().build();
    }
}
