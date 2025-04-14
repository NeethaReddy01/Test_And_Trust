package com.backend.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.backend.exception.ProductException;
import com.backend.modal.Product;
import com.backend.request.CreateProductRequest;
import com.backend.user.domain.ProductSubCategory;

public interface ProductService {
	
	// only for admin
	public Product createProduct(CreateProductRequest req) throws ProductException;
	
	public String deleteProduct(Long productId) throws ProductException;
	
	public Product updateProduct(Long productId,Product product)throws ProductException;
	
	public List<Product> getAllProducts();
	
	// for user and admin both
	public Product findProductById(Long id) throws ProductException;
	
	public List<Product> findProductByCategory(String category);
	
	public List<Product> searchProduct(String query);
	
//	public List<Product> getAllProduct(List<String>colors,List<String>sizes,int minPrice, int maxPrice,int minDiscount, String category, String sort,int pageNumber, int pageSize);
	public List<Product> getAllProduct(String category, List<String>colors);
	
	public List<Product> recentlyAddedProduct();
	
	

}

