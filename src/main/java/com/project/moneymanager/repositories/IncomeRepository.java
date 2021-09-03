package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface IncomeRepository extends CrudRepository<Income, Long> {
}
