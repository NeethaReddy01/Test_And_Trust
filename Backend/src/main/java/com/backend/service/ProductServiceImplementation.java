package com.backend.service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.exception.ProductException;
import com.backend.modal.Category;
//import com.backend.modal.Category;
import com.backend.modal.Product;
import com.backend.repository.CategoryRepository;
//import com.backend.repository.CategoryRepository;
import com.backend.repository.ProductRepository;
import com.backend.request.CreateProductRequest;

@Service
public class ProductServiceImplementation implements ProductService {
  
  private ProductRepository productRepository;
  private UserService userService;
  private CategoryRepository categoryRepository;
  
  public ProductServiceImplementation(ProductRepository productRepository,UserService userService, CategoryRepository categoryRepository) {
    this.productRepository=productRepository;
    this.userService=userService;
    this.categoryRepository=categoryRepository;
  }
  

  @Override
  public Product createProduct(CreateProductRequest req) {
	  Category level1Category = categoryRepository.findByName(req.getLevel1Category());
	    
	    if(level1Category == null) {
	      level1Category = new Category();
	      level1Category.setName(req.getLevel1Category());
	      level1Category.setLevel(1);
	      level1Category = categoryRepository.save(level1Category);
	    }
	    
	    // Find or create level 2 category
	    Category level2Category = categoryRepository.findByNameAndParant(
	        req.getLevel2Category(), req.getLevel1Category());
	    
	    if(level2Category == null) {
	      level2Category = new Category();
	      level2Category.setName(req.getLevel2Category());
	      level2Category.setParentCategory(level1Category);
	      level2Category.setLevel(2);
	      level2Category = categoryRepository.save(level2Category);
	    }
    
//    Category topLevel=categoryRepository.findByName(req.getTopLavelCategory());
//    
//    if(topLevel==null) {
//      
//      Category topLavelCategory=new Category();
//      topLavelCategory.setName(req.getTopLavelCategory());
//      topLavelCategory.setLevel(1);
//      
//      topLevel= categoryRepository.save(topLavelCategory);
//    }
//    
//    Category secondLevel=categoryRepository.
//        findByNameAndParant(req.getSecondLavelCategory(),topLevel.getName());
//    if(secondLevel==null) {
//      
//      Category secondLavelCategory=new Category();
//      secondLavelCategory.setName(req.getSecondLavelCategory());
//      secondLavelCategory.setParentCategory(topLevel);
//      secondLavelCategory.setLevel(2);
//      
//      secondLevel= categoryRepository.save(secondLavelCategory);
//    }
//
//    Category thirdLevel=categoryRepository.findByNameAndParant(req.getThirdLavelCategory(),secondLevel.getName());
//    if(thirdLevel==null) {
//      
//      Category thirdLavelCategory=new Category();
//      thirdLavelCategory.setName(req.getThirdLavelCategory());
//      thirdLavelCategory.setParentCategory(secondLevel);
//      thirdLavelCategory.setLevel(3);
//      
//      thirdLevel=categoryRepository.save(thirdLavelCategory);
//    }
//    
    
    Product product=new Product();
    product.setTitle(req.getTitle());
    product.setColor(req.getColor());
    product.setDescription(req.getDescription());
    product.setDiscountedPrice(req.getDiscountedPrice());
    product.setDiscountPersent(req.getDiscountPersent());
    product.setImageUrl(req.getImageUrl());
    product.setBrand(req.getBrand());
    product.setPrice(req.getPrice());
    product.setSizes(req.getSizes());
    product.setQuantity(req.getQuantity());
    product.setCategory(level2Category); // Set the category to level 2

//    product.setCategory(thirdLevel);
    product.setCreatedAt(LocalDateTime.now());
    
    Product savedProduct= productRepository.save(product);
    
    System.out.println("products - "+product);
    
    return savedProduct;
  }

  @Override
  public String deleteProduct(Long productId) throws ProductException {
    
    Product product=findProductById(productId);
    
    System.out.println("delete product "+product.getId()+" - "+productId);
    product.getSizes();
//    productRepository.save(product);
//    product.getCategory().
    productRepository.delete(product);
    
    return "Product deleted Successfully";
  }

  @Override
  public Product updateProduct(Long productId,Product req) throws ProductException {
    Product product=findProductById(productId);
    
    if(req.getQuantity()!=0) {
      product.setQuantity(req.getQuantity());
    }
    if(req.getDescription()!=null) {
      product.setDescription(req.getDescription());
    }
    
    
      
    
    return productRepository.save(product);
  }

  @Override
  public List<Product> getAllProducts() {
    return productRepository.findAll();
  }
  
  @Override
  public Product findProductById(Long id) throws ProductException {
    Optional<Product> opt=productRepository.findById(id);
    
    if(opt.isPresent()) {
      return opt.get();
    }
    throw new ProductException("product not found with id "+id);
  }

  @Override
  public List<Product> findProductByCategory(String category) {
    
    System.out.println("category --- "+category);
    
    List<Product> products = productRepository.findByCategory(category);
    
    return products;
  }

  @Override
  public List<Product> searchProduct(String query) {
    List<Product> products=productRepository.searchProduct(query);
    return products;
  }



  
  
  @Override
  public List<Product> getAllProduct(String category, List<String>colors) {

//    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    
    List<Product> products = productRepository.filterProducts(category);
    
    
    if (!colors.isEmpty()) {
      products = products.stream()
              .filter(p -> colors.stream().anyMatch(c -> c.equalsIgnoreCase(p.getColor())))
              .collect(Collectors.toList());
    
    
    } 

//    if(stock!=null) {
//
//      if(stock.equals("in_stock")) {
//        products=products.stream().filter(p->p.getQuantity()>0).collect(Collectors.toList());
//      }
//      else if (stock.equals("out_of_stock")) {
//        products=products.stream().filter(p->p.getQuantity()<1).collect(Collectors.toList());        
//      }
        
          
//    }
//    int startIndex = (int) pageable.getOffset();
//    int endIndex = Math.min(startIndex + pageable.getPageSize(), products.size());
//
//    List<Product> pageContent = products.subList(startIndex, endIndex);
//    Page<Product> filteredProducts = new PageImpl<>(pageContent, pageable, products.size());
//      return filteredProducts; // If color list is empty, do nothing and return all products
    return products;
    
  }


  @Override
  public List<Product> recentlyAddedProduct() {
    
    return productRepository.findTop10ByOrderByCreatedAtDesc();
  }

}