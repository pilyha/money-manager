package com.project.moneymanager.services;

import com.project.moneymanager.models.Category;
import com.project.moneymanager.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category addCategory(Category b) {
        return categoryRepository.save(b);
    }


    public Category findCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        return optionalCategory.orElse(null);
    }

    public void updateCategory(Long id, Category category) {
        Category newCategory = findCategory(id);
        newCategory.setName(category.getName());
        categoryRepository.save(newCategory);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> findAllCategory() {
        return (List<Category>) categoryRepository.findAll();
    }
}
