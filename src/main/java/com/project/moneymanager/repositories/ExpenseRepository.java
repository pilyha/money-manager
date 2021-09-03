package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
}
