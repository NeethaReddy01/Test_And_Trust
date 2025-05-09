package com.backend.serviceTest;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.backend.modal.Category;
import com.backend.repository.CategoryRepository;

class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
   // private CategoryService categoryService; // ✅ Make sure you're calling methods on this, not the test class

    private Category category;
    private Category parentCategory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        parentCategory = new Category();
        parentCategory.setId(1L);
        parentCategory.setName("Electronics");

        category = new Category();
        category.setId(2L);
        category.setName("Mobile");
        category.setParentCategory(parentCategory);
        category.setLevel(2);
    }

    @Test
    void testGetCategoryByName_Success() {
        when(categoryRepository.findByName("Mobile")).thenReturn(category);

     //   Category result = categoryService.getCategoryByName("Mobile");

     //   assertNotNull(result);
     //   assertEquals("Mobile", result.getName());
        verify(categoryRepository).findByName("Mobile");
    }

    @Test
    void testGetCategoryByNameAndParent_Success() {
        when(categoryRepository.findByNameAndParant("Mobile", "Electronics")).thenReturn(category);

     //   Category result = categoryService.getCategoryByNameAndParent("Mobile", "Electronics");

      //  assertNotNull(result);
       // assertEquals("Mobile", result.getName());
      //  assertEquals("Electronics", result.getParentCategory().getName());
        verify(categoryRepository).findByNameAndParant("Mobile", "Electronics");
    }

    @Test
    void testCreateCategory_Success() {
        when(categoryRepository.save(category)).thenReturn(category);

      //  Category result = categoryService.createCategory(category);

      //  assertNotNull(result);
     //   assertEquals("Mobile", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testGetCategoryById_Success() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(category));

        //Category result = categoryService.getCategoryById(2L);

      //  assertNotNull(result);
     //   assertEquals(2L, result.getId());
        verify(categoryRepository).findById(2L);
    }
}
