//package com.backend.serviceTest;
//
//public class ProductServiceImplementationTest {
//
//}
package com.backend.serviceTest;

import com.backend.exception.ProductException;
import com.backend.modal.Category;
import com.backend.modal.Product;
import com.backend.repository.CategoryRepository;
import com.backend.repository.ProductRepository;
import com.backend.request.CreateProductRequest;
import com.backend.service.ProductServiceImplementation;
import com.backend.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplementationTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ProductServiceImplementation productService;

    private Product product;

    @BeforeEach
    void setup() {
        product = new Product();
        product.setId(1L);
        product.setTitle("Test Product");
        product.setDescription("Description");
        product.setColor("Red");
        product.setPrice(1000);
        product.setDiscountedPrice(800);
        product.setDiscountPersent(20);
        product.setBrand("BrandX");
        product.setSizes("M,L");
        product.setQuantity(10);
        product.setImageUrl("image.jpg");
        product.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void testCreateProduct() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(level1, level2);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product created = productService.createProduct(req);

        assertEquals("Shirt", created.getTitle());
        assertEquals("Shirts", created.getCategory().getName());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testDeleteProductSuccess() throws ProductException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1L);

        assertEquals("Product deleted Successfully", result);
        verify(productRepository).delete(product);
    }

    @Test
    void testDeleteProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.deleteProduct(1L));
    }

    @Test
    void testUpdateProductSuccess() throws ProductException {
        Product update = new Product();
        update.setQuantity(20);
        update.setDescription("Updated description");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, update);

        assertEquals(20, result.getQuantity());
        assertEquals("Updated description", result.getDescription());
    }

    @Test
    void testFindProductByIdFound() throws ProductException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.findProductById(1L);

        assertEquals(1L, result.getId());
    }

    @Test
    void testFindProductByIdNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.findProductById(1L));
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product, new Product()));

        List<Product> result = productService.getAllProducts();

        assertEquals(2, result.size());
    }

    @Test
    void testFindProductByCategory() {
        when(productRepository.findByCategory("men")).thenReturn(List.of(product));

        List<Product> result = productService.findProductByCategory("men");

        assertEquals(1, result.size());
    }

    @Test
    void testSearchProduct_PriceLow() {
        when(productRepository.searchProductOrderByPriceAsc("shirt")).thenReturn(List.of(product));

        List<Product> result = productService.searchProduct("shirt", "price_low");

        assertEquals(1, result.size());
    }

    @Test
    void testSearchProduct_PriceHigh() {
        when(productRepository.searchProductOrderByPriceDesc("shirt")).thenReturn(List.of(product));

        List<Product> result = productService.searchProduct("shirt", "price_high");

        assertEquals(1, result.size());
    }

    @Test
    void testSearchProduct_Newest() {
        when(productRepository.searchProductOrderByNewest("shirt")).thenReturn(List.of(product));

        List<Product> result = productService.searchProduct("shirt", "newest");

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_WithSort_PriceHigh() {
        when(productRepository.findByCategoryNameOrderByPriceDesc("men")).thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct("men", "price_high");

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_WithoutSort() {
        when(productRepository.filterProducts("men")).thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct("men", "");

        assertEquals(1, result.size());
    }

    @Test
    void testRecentlyAddedProduct() {
        when(productRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(List.of(product));

        List<Product> result = productService.recentlyAddedProduct();

        assertEquals(1, result.size());
    }
    //......
    @Test
    void testUpdateProduct_NoUpdatesProvided_ShouldReturnUnchangedProduct() throws ProductException {
        Product req = new Product(); // no changes set

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, req);

        assertEquals(product.getQuantity(), result.getQuantity());
        assertEquals(product.getDescription(), result.getDescription());
        verify(productRepository).save(product);
    }

    @Test
    void testSearchProduct_InvalidSortType_ShouldDefaultToNewest() {
        when(productRepository.searchProductOrderByNewest("item")).thenReturn(List.of(product));

        List<Product> result = productService.searchProduct("item", "invalid_sort");

        assertEquals(1, result.size());
        verify(productRepository).searchProductOrderByNewest("item");
    }

    @Test
    void testGetAllProduct_InvalidSortType_ShouldFallbackToDefault() {
        when(productRepository.filterProducts("men")).thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct("men", "unknown");

        assertEquals(1, result.size());
        verify(productRepository).filterProducts("men");
    }

    @Test
    void testCreateProduct_WithExistingCategories_ShouldNotCreateNewCategories() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setColor("Black");
        req.setDescription("Running shoes");
        req.setDiscountedPrice(500);
        req.setDiscountPersent(10);
        req.setImageUrl("img.jpg");
        req.setBrand("Nike");
        req.setPrice(700);
        req.setSizes("10");
        req.setQuantity(5);
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Running");

        Category level1 = new Category();
        level1.setName("Footwear");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Running");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Footwear")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Running", "Footwear")).thenReturn(level2);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.createProduct(req);

        assertEquals("Shoes", result.getTitle());
        verify(categoryRepository, never()).save(level1); // should not create level1
        verify(categoryRepository, never()).save(level2); // should not create level2
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testDeleteProduct_VerifySizeFieldAccess() throws ProductException {
        product.setSizes("M,L");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        String result = productService.deleteProduct(1L);

        assertEquals("Product deleted Successfully", result);
        verify(productRepository).delete(product);
    }

    @Test
    void testFindProductByCategory_EmptyList() {
        when(productRepository.findByCategory("unknown")).thenReturn(Collections.emptyList());

        List<Product> result = productService.findProductByCategory("unknown");

        assertTrue(result.isEmpty());
    }

    @Test
    void testRecentlyAddedProduct_EmptyList() {
        when(productRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());

        List<Product> result = productService.recentlyAddedProduct();

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateProduct_NullCategoryHandling() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Test");
        req.setColor("Blue");
        req.setDescription("Desc");
        req.setDiscountedPrice(400);
        req.setDiscountPersent(10);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setPrice(500);
        req.setSizes("S,M");
        req.setQuantity(5);
        req.setLevel1Category(null);
        req.setLevel2Category(null);

        when(categoryRepository.findByName(null)).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product created = productService.createProduct(req);

        assertNotNull(created);
    }
    @Test
    void testUpdateProduct_InvalidId_ThrowsException() {
        Product update = new Product();
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class,
                () -> productService.updateProduct(99L, update));

        assertEquals("product not found with id 99", exception.getMessage());
    }

    @Test
    void testDeleteProduct_NullId_ThrowsException() {
        assertThrows(NullPointerException.class, () -> productService.deleteProduct(null));
    }

    @Test
    void testCreateProduct_WithEmptySizesAndImageUrl() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Watch");
        req.setColor("Black");
        req.setDescription("Smartwatch");
        req.setPrice(2000);
        req.setDiscountedPrice(1500);
        req.setDiscountPersent(25);
        req.setBrand("Fossil");
        req.setSizes(""); // empty sizes
        req.setImageUrl(""); // empty image URL
        req.setQuantity(2);
        req.setLevel1Category("Accessories");
        req.setLevel2Category("Watches");

        Category cat1 = new Category();
        cat1.setName("Accessories");
        Category cat2 = new Category();
        cat2.setName("Watches");
        cat2.setParentCategory(cat1);

        when(categoryRepository.findByName("Accessories")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(cat1, cat2);
        when(categoryRepository.findByNameAndParant("Watches", "Accessories")).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.createProduct(req);

        assertEquals("Watch", result.getTitle());
        assertEquals("", result.getSizes());
    }

    @Test
    void testGetAllProducts_EmptyList() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        List<Product> products = productService.getAllProducts();

        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    void testSearchProduct_EmptyQuery() {
        when(productRepository.searchProductOrderByNewest("")).thenReturn(List.of(product));

        List<Product> results = productService.searchProduct("", "newest");

        assertEquals(1, results.size());
        verify(productRepository).searchProductOrderByNewest("");
    }

    @Test
    void testGetAllProduct_NullSort() {
        when(productRepository.filterProducts("men")).thenReturn(List.of(product));

        List<Product> products = productService.getAllProduct("men", null);

        assertEquals(1, products.size());
    }

    @Test
    void testGetAllProduct_EmptyCategoryAndSort() {
        when(productRepository.filterProducts("")).thenReturn(List.of(product));

        List<Product> products = productService.getAllProduct("", "");

        assertEquals(1, products.size());
    }

    @Test
    void testFindProductById_NegativeId() {
        when(productRepository.findById(-1L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.findProductById(-1L));
    }

    @Test
    void testCreateProductHandlesMultipleSavesOfCategories() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Bag");
        req.setColor("Brown");
        req.setDescription("Leather bag");
        req.setPrice(1200);
        req.setDiscountedPrice(900);
        req.setDiscountPersent(25);
        req.setBrand("LV");
        req.setSizes("OneSize");
        req.setQuantity(4);
        req.setImageUrl("img.jpg");
        req.setLevel1Category("Accessories");
        req.setLevel2Category("Bags");

        when(categoryRepository.findByName("Accessories")).thenReturn(null);
        when(categoryRepository.findByNameAndParant("Bags", "Accessories")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.createProduct(req);

        assertEquals("Bag", result.getTitle());
        verify(categoryRepository, times(2)).save(any(Category.class)); // 2 category saves
    }

    @Test
    void testCreateProduct_MissingTitle() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle(null);  // Missing title
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testCreateProduct_InvalidPrice() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(-1000);  // Invalid price
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testGetAllProduct_InvalidCategory() {
        when(productRepository.filterProducts("unknown_category")).thenReturn(Collections.emptyList());

        List<Product> result = productService.getAllProduct("unknown_category", "price_low");

        assertTrue(result.isEmpty());
        verify(productRepository).filterProducts("unknown_category");
    }
    @Test
    void testUpdateProduct_NoChanges() throws ProductException {
        Product update = new Product();
        update.setQuantity(product.getQuantity());  // No changes to quantity
        update.setDescription(product.getDescription());  // No changes to description

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, update);

        assertEquals(product.getQuantity(), result.getQuantity());
        assertEquals(product.getDescription(), result.getDescription());
        verify(productRepository).save(product);
    }
    @Test
    void testCreateProduct_InvalidCategoryLevel() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);
        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setLevel(3); // Invalid category level

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);

        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testSearchProduct_EmptyList() {
        when(productRepository.searchProductOrderByNewest("nonexistent")).thenReturn(Collections.emptyList());

        List<Product> result = productService.searchProduct("nonexistent", "newest");

        assertTrue(result.isEmpty());
        verify(productRepository).searchProductOrderByNewest("nonexistent");
    }
    @Test
    void testDeleteProduct_InvalidId() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.deleteProduct(999L));
    }
    @Test
    void testUpdateProduct_NullFields() throws ProductException {
        Product update = new Product();
        update.setDescription(null);  // Null description

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, update);

        assertNull(result.getDescription());
        verify(productRepository).save(product);
    }
    @Test
    void testCreateProduct_InvalidImageUrl() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setColor("Black");
        req.setDescription("Running shoes");
        req.setDiscountedPrice(500);
        req.setDiscountPersent(10);
        req.setImageUrl("invalid_url"); // Invalid URL
        req.setBrand("Nike");
        req.setPrice(700);
        req.setSizes("10");
        req.setQuantity(5);
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Running");

        Category level1 = new Category();
        level1.setName("Footwear");
        level1.setLevel(1);
        Category level2 = new Category();
        level2.setName("Running");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Footwear")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Running", "Footwear")).thenReturn(level2);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.createProduct(req);

        assertEquals("Shoes", result.getTitle());
        assertEquals("invalid_url", result.getImageUrl());
    }

    @Test
    void testCreateProduct_WithNullFields() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle(null);  // Null title
        req.setDescription(null);  // Null description
        req.setColor(null);  // Null color
        req.setPrice(1000);  // Valid price
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl(null);  // Null image URL
        req.setBrand("BrandX");
        req.setSizes(null);  // Null sizes
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        // Create category objects
        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.createProduct(req);

        assertNotNull(result);
        assertNull(result.getTitle());  // Title should be null
        assertNull(result.getDescription());  // Description should be null
        assertNull(result.getColor());  // Color should be null
        assertNull(result.getImageUrl());  // Image URL should be null
        assertNull(result.getSizes());  // Sizes should be null
    }
    @Test
    void testUpdateProduct_WithNullValues() throws ProductException {
        Product update = new Product();
        update.setTitle(null);  // Null title
        update.setDescription(null);  // Null description
        update.setColor(null);  // Null color
        update.setPrice(1500);  // Valid price
        update.setDiscountedPrice(1200);
        update.setDiscountPersent(20);
        update.setImageUrl(null);  // Null image URL
        update.setBrand("NewBrand");
        update.setSizes(null);  // Null sizes
        update.setQuantity(10);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, update);

        assertEquals(1500, result.getPrice());  // Price should be updated
        assertNull(result.getTitle());  // Title should remain null
        assertNull(result.getDescription());  // Description should remain null
        assertNull(result.getImageUrl());  // Image URL should remain null
        assertNull(result.getSizes());  // Sizes should remain null
    }
    @Test
    void testCreateProduct_InvalidDiscountPercentage() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(120);  // Invalid discount percentage (greater than 100)
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        // Create category objects
        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testCreateProduct_InvalidQuantity() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(-5);  // Invalid quantity
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);

        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testSearchProduct_WithSpecialCharacters() {
        when(productRepository.searchProductOrderByNewest("shirt@#")).thenReturn(List.of(product));

        List<Product> result = productService.searchProduct("shirt@#", "newest");

        assertEquals(1, result.size());
        verify(productRepository).searchProductOrderByNewest("shirt@#");
    }
    @Test
    void testCreateProduct_WithCategoryParentChildMismatch() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setLevel(1);  // Parent and child mismatch

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);

        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testUpdateProduct_NewCategory() throws ProductException {
        Category newCategory = new Category();
        newCategory.setName("Electronics");
        newCategory.setLevel(1);

        product.setCategory(newCategory);  // Assigning new category to product

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, product);

        assertEquals("Electronics", result.getCategory().getName());  // Category should be updated
    }
    @Test
    void testUpdateProduct_NullProductReturned() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.updateProduct(1L, product));
    }
    @Test
    void testDeleteProduct_NonExistingProduct() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ProductException.class, () -> productService.deleteProduct(1L));
    }
    @Test
    void testDeleteProduct_OtherException() {
        when(productRepository.findById(1L)).thenThrow(new RuntimeException("Database error"));

        assertThrows(RuntimeException.class, () -> productService.deleteProduct(1L));
    }
    @Test
    void testCreateProduct_WithPriceGreaterThanDiscountedPrice() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(1100);  // Discounted price greater than price
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);

        // Expecting ProductException as discounted price cannot be greater than original price
        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testCreateProduct_InvalidCategory() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("NonExistingCategory");  // Invalid category
        req.setLevel2Category("Shirts");

        when(categoryRepository.findByName("NonExistingCategory")).thenReturn(null);

        // Should throw ProductException due to non-existing category
        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testUpdateProduct_WithInvalidProductId() {
        Product update = new Product();
        update.setTitle("New Shirt");
        update.setDescription("Updated Cotton shirt");
        update.setColor("Red");
        update.setPrice(1500);
        update.setDiscountedPrice(1200);
        update.setDiscountPersent(20);
        update.setImageUrl("new_url");
        update.setBrand("NewBrand");
        update.setSizes("M,L");
        update.setQuantity(10);

        when(productRepository.findById(999L)).thenReturn(Optional.empty());  // Non-existing product ID

        // Should throw ProductException as the product doesn't exist
        assertThrows(ProductException.class, () -> productService.updateProduct(999L, update));
    }
    @Test
    void testSearchProduct_EmptySearchTerm() {
        // Return all products when no search term is provided
        when(productRepository.searchProductOrderByNewest("")).thenReturn(List.of(product, product));

        List<Product> result = productService.searchProduct("", "newest");

        assertEquals(2, result.size());
        verify(productRepository).searchProductOrderByNewest("");
    }
    @Test
    void testSearchProduct_NoMatchingResults() {
        when(productRepository.searchProductOrderByNewest("NonExistingProduct")).thenReturn(List.of());

        List<Product> result = productService.searchProduct("NonExistingProduct", "newest");

        assertTrue(result.isEmpty());  // Expecting an empty list
    }
    @Test
    void testCreateProduct_SaveException() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);
        when(productRepository.save(any(Product.class))).thenThrow(new RuntimeException("Database error"));

        // Expecting exception due to error during product save
        assertThrows(RuntimeException.class, () -> productService.createProduct(req));
    }
   
    @Test
    void testDeleteProduct_InvalidId1() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());  // Non-existing product

        assertThrows(ProductException.class, () -> productService.deleteProduct(999L));
    }
    @Test
    void testCreateProduct_WithIncorrectCategoryLevel() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M,L");
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(2);  // Invalid level for level1

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);

        // Expecting ProductException due to incorrect category levels
        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }
    @Test
    void testCreateProduct_WithInvalidSizeFormat() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shirt");
        req.setDescription("Cotton shirt");
        req.setColor("Blue");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setImageUrl("url");
        req.setBrand("BrandX");
        req.setSizes("M, XXS, XL");  // Invalid size format
        req.setQuantity(5);
        req.setLevel1Category("Men");
        req.setLevel2Category("Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(level1);
        when(categoryRepository.findByNameAndParant("Shirts", "Men")).thenReturn(level2);

        // Should throw ProductException due to invalid size format
        assertThrows(ProductException.class, () -> productService.createProduct(req));
    }

}

