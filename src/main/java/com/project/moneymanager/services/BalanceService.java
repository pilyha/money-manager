package com.project.moneymanager.services;

import com.project.moneymanager.models.Balance;
import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final IncomeService incomeService;
    private final ExpenseService expenseService;

    public BalanceService(@Lazy BalanceRepository balanceRepository,@Lazy IncomeService incomeService,@Lazy ExpenseService expenseService) {
        this.balanceRepository = balanceRepository;
        this.incomeService = incomeService;
        this.expenseService = expenseService;
    }

    public Balance getLastBalance(User u){
        Balance bal=null;
        for(Balance b:u.getBalances()){
            bal=b;
        }
        if(bal==null){
            bal=new Balance(u,0);
            balanceRepository.save(bal);
        }

        return  bal;
    }

    public Balance transaction(User u){
        int total = 0;
        for (Income i : incomeService.findAllIncomes().stream()
                .filter(income -> income.getUser().equals(u))
                .collect(Collectors.toList())) {
            total += i.getAmount();
        }
        for (Expense i : expenseService.findAllExpenses().stream()
                .filter(income -> income.getUser().equals(u))
                .collect(Collectors.toList())) {
            total -= i.getAmount();
        }
        Balance bal = new Balance(u, total);
        balanceRepository.save(bal);
        return bal;
    }
}
