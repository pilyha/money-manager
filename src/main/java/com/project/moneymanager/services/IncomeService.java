package com.project.moneymanager.services;

import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;

import java.util.List;

public interface IncomeService {

    void addIncome(User user, Income income);

    void updateIncome(Long id, Income income);

    Income findIncomeById(Long id);

    void deleteIncome(User user, Long id);

    List<Income> findAllIncomes();
}
