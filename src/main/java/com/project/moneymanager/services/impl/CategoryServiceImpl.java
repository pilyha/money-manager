package com.project.moneymanager.services.impl;

import com.project.moneymanager.models.Category;
import com.project.moneymanager.repositories.CategoryRepository;
import com.project.moneymanager.services.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(Category category) {
        if (category != null) {
            LOGGER_INFO.info("Start create category: " + category.getName());
            checkDuplicate(category);
            categoryRepository.save(category);
            LOGGER_INFO.info("End create category: " + category.getName());
        } else {
            LOGGER_ERROR.error("Category is null!");
        }
    }

    public void updateCategory(Long id, Category category) {
        if (categoryRepository.existsById(id)) {
            LOGGER_WARN.warn("Start update category: " + id);
            Category newCategory = findCategoryById(id);
            checkDuplicate(category);
            newCategory.setName(category.getName());
            categoryRepository.save(newCategory);
            LOGGER_WARN.warn("End update category: " + id);
        } else {
            LOGGER_ERROR.error("Category doesn't exists");
        }
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void deleteCategory(Long id) {
        if (categoryRepository.existsById(id)) {
            LOGGER_WARN.warn("Start delete category: " + id);
            categoryRepository.deleteById(id);
            LOGGER_WARN.warn("End delete category: " + id);
        } else {
            LOGGER_ERROR.error("Category doesn't exists");
        }
    }

    public List<Category> findAllCategory() {
        LOGGER_INFO.info("Read all categories");
        return (List<Category>) categoryRepository.findAll();
    }

    private void checkDuplicate(Category category) {
        for (Category cat : findAllCategory()) {
            if (category.getName().equals(cat.getName())) {
                throw new RuntimeException("Duplicate category");
            }
        }
    }
}
