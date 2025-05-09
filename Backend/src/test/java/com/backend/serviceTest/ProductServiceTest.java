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
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

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
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setTitle("T-Shirt");
        product.setDescription("Cotton");
        product.setPrice(1000);
        product.setDiscountedPrice(800);
        product.setDiscountPersent(20);
        product.setQuantity(10);
        product.setBrand("Puma");
        product.setColor("Red");
        product.setSizes("M,L");
        product.setImageUrl("img.jpg");
        product.setCreatedAt(LocalDateTime.now());

        Category category = new Category();
        category.setName("Men");
        category.setLevel(1);
        product.setCategory(category);
    }

    @Test
    void testCreateProductWithNewCategories() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("T-Shirt");
        req.setDescription("Soft cotton");
        req.setBrand("Nike");
        req.setColor("Blue");
        req.setPrice(900);
        req.setDiscountedPrice(750);
        req.setDiscountPersent(17);
        req.setQuantity(15);
        req.setSizes("M,L");
        req.setImageUrl("img.jpg");
        req.setLevel1Category("Men");
        req.setLevel2Category("T-Shirts");

        Category level1 = new Category();
        level1.setName("Men");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("T-Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Men")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(level1).thenReturn(level2);
        when(categoryRepository.findByNameAndParant("T-Shirts", "Men")).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product saved = productService.createProduct(req);
        assertEquals("T-Shirt", saved.getTitle());
        verify(productRepository).save(any(Product.class));
    }

    @Test
    void testFindProductById_Success() throws ProductException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        Product result = productService.findProductById(1L);
        assertEquals("T-Shirt", result.getTitle());
    }

    @Test
    void testFindProductById_NotFound() {
        when(productRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ProductException.class, () -> productService.findProductById(2L));
    }

    @Test
    void testDeleteProduct_Success() throws ProductException {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        String msg = productService.deleteProduct(1L);
        assertEquals("Product deleted Successfully", msg);
        verify(productRepository).delete(product);
    }

    @Test
    void testUpdateProduct_OnlyDescriptionChanged() throws ProductException {
        Product update = new Product();
        update.setDescription("New description");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product updated = productService.updateProduct(1L, update);
        assertEquals("New description", updated.getDescription());
    }

    @Test
    void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(List.of(product));
        List<Product> products = productService.getAllProducts();
        assertEquals(1, products.size());
    }

    @Test
    void testFindProductByCategory() {
        when(productRepository.findByCategory("men")).thenReturn(List.of(product));
        List<Product> result = productService.findProductByCategory("men");
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProduct_SortByPriceLow() {
        when(productRepository.searchProductOrderByPriceAsc("shirt")).thenReturn(List.of(product));
        List<Product> result = productService.searchProduct("shirt", "price_low");
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProduct_SortByPriceHigh() {
        when(productRepository.searchProductOrderByPriceDesc("shirt")).thenReturn(List.of(product));
        List<Product> result = productService.searchProduct("shirt", "price_high");
        assertEquals(1, result.size());
    }

    @Test
    void testSearchProduct_DefaultSortNewest() {
        when(productRepository.searchProductOrderByNewest("shirt")).thenReturn(List.of(product));
        List<Product> result = productService.searchProduct("shirt", "newest");
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_PriceLow() {
        when(productRepository.findByCategoryNameOrderByPriceAsc("Men")).thenReturn(List.of(product));
        List<Product> result = productService.getAllProduct("Men", "price_low");
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_EmptySort_UsesDefault() {
        when(productRepository.filterProducts("Men")).thenReturn(List.of(product));
        List<Product> result = productService.getAllProduct("Men", "");
        assertEquals(1, result.size());
    }

    @Test
    void testRecentlyAddedProduct() {
        when(productRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(List.of(product));
        List<Product> result = productService.recentlyAddedProduct();
        assertEquals(1, result.size());
    }
    //...
    @Test
    void testUpdateProduct_QuantityUpdatedOnly() throws ProductException {
        Product update = new Product();
        update.setQuantity(50);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product updated = productService.updateProduct(1L, update);
        assertEquals(50, updated.getQuantity());
        assertEquals(product.getDescription(), updated.getDescription()); // unchanged
    }

    @Test
    void testUpdateProduct_NoChangesMade() throws ProductException {
        Product update = new Product(); // no fields set

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        Product updated = productService.updateProduct(1L, update);
        assertEquals(product.getQuantity(), updated.getQuantity());
        assertEquals(product.getDescription(), updated.getDescription());
    }

    @Test
    void testDeleteProduct_ProductNotFound_ThrowsException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductException ex = assertThrows(ProductException.class, () -> productService.deleteProduct(99L));
        assertTrue(ex.getMessage().contains("product not found"));
    }

    @Test
    void testFindProductByCategory_CaseInsensitive() {
        when(productRepository.findByCategory("men")).thenReturn(List.of(product));


    }

    @Test
    void testSearchProduct_SortNull_ShouldDefaultToNewest() {
        when(productRepository.searchProductOrderByNewest("shirt")).thenReturn(List.of(product));


    }

    @Test
    void testSearchProduct_SortEmpty_ShouldDefaultToNewest() {
        when(productRepository.searchProductOrderByNewest("shirt")).thenReturn(List.of(product));

        List<Product> result = productService.searchProduct("shirt", "");
        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_SortPriceHigh() {
        when(productRepository.findByCategoryNameOrderByPriceDesc("Men")).thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct("Men", "price_high");

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_SortNewest() {
        when(productRepository.findByCategoryNameOrderByCreatedAtDesc("Men")).thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct("Men", "newest");

        assertEquals(1, result.size());
    }

    @Test
    void testGetAllProduct_UnknownSort_ShouldFallbackToDefault() {
        when(productRepository.filterProducts("Men")).thenReturn(List.of(product));

        List<Product> result = productService.getAllProduct("Men", "unknown_sort");

        assertEquals(1, result.size());
    }

    @Test
    void testCreateProductHandlesDuplicateSaveCalls() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setColor("White");
        req.setDescription("Sneakers");
        req.setDiscountedPrice(500);
        req.setDiscountPersent(10);
        req.setImageUrl("url");
        req.setBrand("Adidas");
        req.setPrice(700);
        req.setSizes("10");
        req.setQuantity(5);
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Sneakers");

        Category level1 = new Category();
        level1.setName("Footwear");
        Category level2 = new Category();
        level2.setName("Sneakers");
        level2.setParentCategory(level1);

        when(categoryRepository.findByName("Footwear")).thenReturn(null);
        when(categoryRepository.findByNameAndParant("Sneakers", "Footwear")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.createProduct(req);

        assertEquals("Shoes", result.getTitle());
        verify(categoryRepository, times(2)).save(any(Category.class)); // two saves
    }

    @Test
    void testRecentlyAddedProduct_ReturnsEmptyList() {
        when(productRepository.findTop10ByOrderByCreatedAtDesc()).thenReturn(Collections.emptyList());

        List<Product> result = productService.recentlyAddedProduct();

        assertTrue(result.isEmpty());
    }

    @Test
    void testFindProductById_ZeroId_ShouldThrowException() {
        when(productRepository.findById(0L)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> productService.findProductById(0L));
        assertTrue(exception.getMessage().contains("product not found"));
    }

    @Test
    void testCreateProduct_MissingTitle() {
        CreateProductRequest req = new CreateProductRequest();
        req.setDescription("Cotton T-shirt");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setQuantity(10);
        req.setBrand("Nike");
        req.setColor("Red");
        req.setSizes("M,L");
        req.setImageUrl("image.jpg");
        req.setLevel1Category("Men");
        req.setLevel2Category("T-Shirts");

        when(categoryRepository.findByName("Men")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductException ex = assertThrows(ProductException.class, () -> productService.createProduct(req));
        assertEquals("Product title cannot be null or empty", ex.getMessage());
    }
    @Test
    void testFindProductByCategory_NoProductsFound() {
        when(productRepository.findByCategory("men")).thenReturn(Collections.emptyList());

        List<Product> result = productService.findProductByCategory("men");
        assertTrue(result.isEmpty());
    }
    @Test
    void testFindProductByCategory_CaseInsensitive1() {
        when(productRepository.findByCategory("MEN")).thenReturn(List.of(product));

        List<Product> result = productService.findProductByCategory("MEN");
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        Product update = new Product();
        update.setDescription("Updated description");

        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        ProductException exception = assertThrows(ProductException.class, () -> productService.updateProduct(99L, update));
        assertTrue(exception.getMessage().contains("Product not found"));
    }
    @Test
    void testDeleteProduct_NullId() {
        assertThrows(ProductException.class, () -> productService.deleteProduct(null));
    }
    @Test
    void testSearchProduct_EmptySearchTerm() {
        when(productRepository.searchProductOrderByNewest("")).thenReturn(Collections.emptyList());

        List<Product> result = productService.searchProduct("", "newest");
        assertTrue(result.isEmpty());
    }
    @Test
    void testGetAllProducts_LargeList() {
        List<Product> productList = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            productList.add(product);
        }

        when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getAllProducts();
        assertEquals(1000, result.size());
    }
    @Test
    void testCreateProduct_InvalidPrice() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setDescription("Running Shoes");
        req.setPrice(-500);
        req.setDiscountedPrice(400);
        req.setDiscountPersent(10);
        req.setQuantity(10);
        req.setBrand("Nike");
        req.setColor("Black");
        req.setSizes("10,11");
        req.setImageUrl("image.jpg");
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Sneakers");

        when(categoryRepository.findByName("Footwear")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductException ex = assertThrows(ProductException.class, () -> productService.createProduct(req));
        assertEquals("Price cannot be negative", ex.getMessage());
    }
    @Test
    void testCreateProduct_InvalidDiscountedPrice() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("T-shirt");
        req.setDescription("Soft Cotton");
        req.setPrice(1000);
        req.setDiscountedPrice(1200); // Discounted price greater than regular price
        req.setDiscountPersent(10);
        req.setQuantity(5);
        req.setBrand("Adidas");
        req.setColor("Blue");
        req.setSizes("M,L");
        req.setImageUrl("img.jpg");
        req.setLevel1Category("Men");
        req.setLevel2Category("T-shirts");

        when(categoryRepository.findByName("Men")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductException ex = assertThrows(ProductException.class, () -> productService.createProduct(req));
        assertEquals("Discounted price cannot be greater than original price", ex.getMessage());
    }
    @Test
    void testCreateProduct_CategoryAlreadyExists() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setDescription("Running Shoes");
        req.setPrice(1500);
        req.setDiscountedPrice(1200);
        req.setDiscountPersent(20);
        req.setQuantity(5);
        req.setBrand("Adidas");
        req.setColor("Black");
        req.setSizes("10,11");
        req.setImageUrl("img.jpg");
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Sneakers");

        Category existingCategory = new Category();
        existingCategory.setName("Footwear");

        when(categoryRepository.findByName("Footwear")).thenReturn(existingCategory);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product savedProduct = productService.createProduct(req);
        assertEquals("Shoes", savedProduct.getTitle());
        verify(categoryRepository, never()).save(any(Category.class)); // Category save should not be called
    }
    @Test
    void testSearchProduct_NullCategory() {
        when(productRepository.searchProductOrderByNewest(null)).thenReturn(Collections.emptyList());

        List<Product> result = productService.searchProduct(null, "newest");
        assertTrue(result.isEmpty());
    }
    @Test
    void testCreateProduct_MissingBrand() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setDescription("Running Shoes");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setQuantity(10);
        req.setColor("Black");
        req.setSizes("10,11");
        req.setImageUrl("image.jpg");
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Sneakers");

        when(categoryRepository.findByName("Footwear")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductException ex = assertThrows(ProductException.class, () -> productService.createProduct(req));
        assertEquals("Brand cannot be null or empty", ex.getMessage());
    }
    @Test
    void testCreateProduct_NegativeQuantity() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("Shoes");
        req.setDescription("Running Shoes");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setQuantity(-10); // Invalid quantity
        req.setBrand("Nike");
        req.setColor("Red");
        req.setSizes("10,11");
        req.setImageUrl("image.jpg");
        req.setLevel1Category("Footwear");
        req.setLevel2Category("Sneakers");

        when(categoryRepository.findByName("Footwear")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        ProductException ex = assertThrows(ProductException.class, () -> productService.createProduct(req));
        assertEquals("Quantity cannot be negative", ex.getMessage());
    }
    @Test
    void testUpdateProduct_InvalidCategory() throws ProductException {
        Product update = new Product();
        update.setCategory(new Category()); // New category which is not saved

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(categoryRepository.findByName("NonExistentCategory")).thenReturn(null); // Category does not exist

        ProductException exception = assertThrows(ProductException.class, () -> productService.updateProduct(1L, update));
        assertTrue(exception.getMessage().contains("Category not found"));
    }
    @Test
    void testUpdateProduct_InvalidDiscountedPrice() throws ProductException {
        Product update = new Product();
        update.setDiscountedPrice(1500); // Invalid price

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductException ex = assertThrows(ProductException.class, () -> productService.updateProduct(1L, update));
        assertEquals("Discounted price cannot be greater than original price", ex.getMessage());
    }
    @Test
    void testUpdateProduct_InvalidDiscountPercentage() throws ProductException {
        Product update = new Product();
        update.setDiscountPersent(120); // Invalid discount percentage > 100%

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        ProductException ex = assertThrows(ProductException.class, () -> productService.updateProduct(1L, update));
        assertEquals("Discount percentage cannot exceed 100%", ex.getMessage());
    }
    @Test
    void testFindProductByCategory_NonExistent() {
        when(productRepository.findByCategory("NonExistentCategory")).thenReturn(Collections.emptyList());
        List<Product> result = productService.findProductByCategory("NonExistentCategory");
        assertTrue(result.isEmpty());
    }
    @Test
    void testCreateProduct_WithValidCategoryChain() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("T-shirt");
        req.setDescription("Soft Cotton T-shirt");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setQuantity(15);
        req.setBrand("Nike");
        req.setColor("Red");
        req.setSizes("M,L");
        req.setImageUrl("img.jpg");
        req.setLevel1Category("Apparel");
        req.setLevel2Category("T-Shirts");

        Category level1 = new Category();
        level1.setName("Apparel");
        level1.setLevel(1);

        Category level2 = new Category();
        level2.setName("T-Shirts");
        level2.setParentCategory(level1);
        level2.setLevel(2);

        when(categoryRepository.findByName("Apparel")).thenReturn(null);
        when(categoryRepository.findByNameAndParant("T-Shirts", "Apparel")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(level1).thenReturn(level2);
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product saved = productService.createProduct(req);
        assertEquals("T-shirt", saved.getTitle());
        verify(categoryRepository, times(2)).save(any(Category.class)); // Two save calls for categories
    }
    @Test
    void testDeleteProduct_WithNonExistentId() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        ProductException exception = assertThrows(ProductException.class, () -> productService.deleteProduct(999L));
        assertEquals("Product not found", exception.getMessage());
    }
    @Test
    void testCreateProduct_LargeImageUrl() {
        CreateProductRequest req = new CreateProductRequest();
        req.setTitle("T-shirt");
        req.setDescription("Soft Cotton T-shirt");
        req.setPrice(1000);
        req.setDiscountedPrice(800);
        req.setDiscountPersent(20);
        req.setQuantity(15);
        req.setBrand("Nike");
        req.setColor("Red");
        req.setSizes("M,L");
        req.setImageUrl("https://www.example.com/images/" + "a".repeat(300)); // Large URL
        req.setLevel1Category("Apparel");
        req.setLevel2Category("T-Shirts");

        when(categoryRepository.findByName("Apparel")).thenReturn(null);
        when(categoryRepository.findByNameAndParant("T-Shirts", "Apparel")).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category());
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product savedProduct = productService.createProduct(req);
        assertNotNull(savedProduct);
    }
    @Test
    void testGetAllProducts_NoProducts() {
        when(productRepository.findAll()).thenReturn(Collections.emptyList());
        List<Product> result = productService.getAllProducts();
        assertTrue(result.isEmpty());
    }
    @Test
    void testFindProductByInvalidId() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());
        ProductException exception = assertThrows(ProductException.class, () -> productService.findProductById(999L));
        assertTrue(exception.getMessage().contains("product not found"));
    }



}
