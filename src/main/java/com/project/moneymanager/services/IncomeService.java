package com.project.moneymanager.services;

import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    private final IncomeRepository incomeRepository;
    private final BalanceService balanceService;

    public IncomeService(@Lazy IncomeRepository incomeRepository,@Lazy BalanceService balanceService) {
        this.incomeRepository = incomeRepository;
        this.balanceService = balanceService;
    }

    public Income addIncome(User u, Income income) {
        income.setUser(u);
        incomeRepository.save(income);
        balanceService.transaction(u);
        return income;

    }

    public void updateIncome(Long id, Income income) {
        Income newIncome = findIncomeById(id);
        newIncome.setDescription(income.getDescription());
        newIncome.setAmount(income.getAmount());
        incomeRepository.save(newIncome);
        balanceService.transaction(newIncome.getUser());
    }

    public Income findIncomeById(Long id) {
        return incomeRepository.findById(id).orElse(null);
    }

    public void deleteIncome(User u, Long id) {
        incomeRepository.deleteById(id);
        balanceService.transaction(u);
    }


    public List<Income> findAllIncomes() {
        return (List<Income>) incomeRepository.findAll();
    }
}
