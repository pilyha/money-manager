package com.project.moneymanager.services;

import com.project.moneymanager.models.Category;

import java.util.List;

public interface CategoryService {

    void addCategory(Category category);

    void updateCategory(Long id, Category category);

    Category findCategoryById(Long id);

    void deleteCategory(Long id);

    List<Category> findAllCategory();
}
