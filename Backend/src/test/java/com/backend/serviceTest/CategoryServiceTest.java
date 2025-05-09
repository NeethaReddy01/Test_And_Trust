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
}
