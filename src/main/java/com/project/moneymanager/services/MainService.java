package com.project.moneymanager.services;

import com.project.moneymanager.models.*;
import com.project.moneymanager.repositories.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MainService {
    private final PlanRepository planRepository;
    private final RoleRepository roleRepository;
    private final IncomeRepository incomeRepository;
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final BalanceRepository balanceRepository;
    private final NoteRepository noteRepository;

    public MainService(PlanRepository planRepository, RoleRepository roleRepository, IncomeRepository incomeRepository, ExpenseRepository expenseRepository, CategoryRepository categoryRepository, BalanceRepository balanceRepository, NoteRepository noteRepository) {
        this.planRepository = planRepository;
        this.roleRepository = roleRepository;
        this.incomeRepository = incomeRepository;
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.balanceRepository = balanceRepository;
        this.noteRepository = noteRepository;
    }

    public void planToExpense(Expense e, Plan p) {
        e.setPlan(p);
    }

    public Income findIncome(Long id) {
        Optional<Income> income = incomeRepository.findById(id);
        return income.orElse(null);
    }

    public Plan findPlanByDate(User user, Date d) {
        for (Plan plan : user.getPlans()) {
            Date a = plan.getStart_datez();
            Date b = plan.getEnd_datez();
            // the date in question
            if (a.compareTo(d) * d.compareTo(b) > 0) {
                return plan;
            }
        }
        return null;
    }

    public LocalDate getLocalDate() {
        return LocalDate.now();
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public List<Role> findAllRoll() {
        return (List<Role>) roleRepository.findAll();
    }



}