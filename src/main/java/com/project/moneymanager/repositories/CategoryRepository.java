package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Category;
import com.project.moneymanager.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
