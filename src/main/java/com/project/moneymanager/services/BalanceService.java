package com.project.moneymanager.services;

import com.project.moneymanager.models.Balance;
import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class BalanceService {

    @Autowired
    private BalanceRepository balanceRepository;
    @Autowired
    private IncomeService incomeService;
    @Autowired
    private ExpenseService expenseService;

    public Balance adding(User u, int d){
        int x=0;
        for(Balance b:u.getBalances()){
            if(b==null){
                x=b.getVal();
            }
        }
        Balance bal=new Balance(u,d+x);
        balanceRepository.save(bal);
        return bal;
    }
    public Balance taking(User u,int d){
        int x=0;
        for(Balance b:u.getBalances()){
            if(b==null){
                x=b.getVal();
            }
        }
        Balance bal=new Balance(u,x-d);
        balanceRepository.save(bal);
        return bal;
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
