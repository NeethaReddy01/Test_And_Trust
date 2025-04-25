package com.backend.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.backend.modal.Product;
import com.backend.user.domain.ProductSubCategory;


public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query("SELECT p From Product p Where LOWER(p.category.name)=:category")
	public List<Product> findByCategory(@Param("category") String category);
	
	@Query("SELECT p From Product p where LOWER(p.title) Like %:query% OR LOWER(p.description) Like %:query% OR LOWER(p.brand) LIKE %:query% OR LOWER(p.category.name) LIKE %:query%")
	public List<Product> searchProduct(@Param("query")String query);
	
	@Query("SELECT p From Product p where (LOWER(p.title) Like %:query% OR LOWER(p.description) Like %:query% OR LOWER(p.brand) LIKE %:query% OR LOWER(p.category.name) LIKE %:query%) ORDER BY p.discountedPrice ASC")
	public List<Product> searchProductOrderByPriceAsc(@Param("query") String query);

	@Query("SELECT p From Product p where (LOWER(p.title) Like %:query% OR LOWER(p.description) Like %:query% OR LOWER(p.brand) LIKE %:query% OR LOWER(p.category.name) LIKE %:query%) ORDER BY p.discountedPrice DESC")
	public List<Product> searchProductOrderByPriceDesc(@Param("query") String query);

	@Query("SELECT p From Product p where (LOWER(p.title) Like %:query% OR LOWER(p.description) Like %:query% OR LOWER(p.brand) LIKE %:query% OR LOWER(p.category.name) LIKE %:query%) ORDER BY p.createdAt DESC")
	public List<Product> searchProductOrderByNewest(@Param("query") String query);
	


	
	@Query("SELECT p FROM Product p " +
	        "WHERE (p.category.name = :category OR :category = '') "+"ORDER BY "  +"p.createdAt DESC")
//			
//	        "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
//		    "AND (:minDiscount IS NULL OR p.discountPersent >= :minDiscount) " +
//		    
//		    "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
//		    "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC, "+
	
		    
	List<Product> filterProducts(
	        @Param("category") String category
//			@Param("minPrice") Integer minPrice,
//			@Param("maxPrice") Integer maxPrice,
//			@Param("minDiscount") Integer minDiscount,
//			@Param("sort") String sort
			);
	
	public List<Product> findTop10ByOrderByCreatedAtDesc();
	
	List<Product> findByCategoryNameOrderByPriceAsc(String category);
	List<Product> findByCategoryNameOrderByPriceDesc(String category);
	List<Product> findByCategoryNameOrderByCreatedAtDesc(String category);

	// Or a more dynamic method
	@Query("SELECT p FROM Product p " +
	        "WHERE (p.category.name = :category OR :category = '') " +
	        "ORDER BY CASE " +
	        "WHEN :sort = 'price_low' THEN p.price " +
	        "END ASC, " +
	        "CASE " +
	        "WHEN :sort = 'price_high' THEN p.price " +
	        "END DESC, " +
	        "CASE " +
	        "WHEN :sort = 'newest' THEN p.createdAt " +
	        "END DESC")
	List<Product> filterProductsWithSort(
	        @Param("category") String category,
	        @Param("sort") String sort);
}