package com.project.moneymanager.controllers;

import com.project.moneymanager.models.*;
import com.project.moneymanager.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.List;

@Controller
public class MainController {

    private final UserService userService;
    private final PlanService planService;
    private final IncomeService incomeService;
    private final BalanceService balanceService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    @Autowired
    public MainController(UserService userService, PlanService planService, IncomeService incomeService, BalanceService balanceService, ExpenseService expenseService, CategoryService categoryService) {
        this.userService = userService;
        this.planService = planService;
        this.incomeService = incomeService;
        this.balanceService = balanceService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model, @ModelAttribute("note") Note note, @ModelAttribute("category") Category category, BindingResult result) {
        User user = userService.findByUsername(principal.getName());
        Balance balance;
        if (user.getBalances().size() < 1) {
            balance = new Balance(user, 0);
        } else {
            balance = balanceService.getLastBalance(user);
        }

        Integer[] values = new Integer[user.getBalances().size()];
        Integer[] dates = new Integer[user.getBalances().size()];
        int j = 0;
        for (Balance bal : user.getBalances()) {
            values[j] = bal.getVal();
            dates[j] = j;
            j++;
        }

        String[] categories = new String[user.getCategories().size()];
        int[] persent = new int[user.getCategories().size()];
        int i = 0;
        for (Category cat : user.getCategories()) {
            categories[i] = cat.getName();
            for (int tt : persent) {
                persent[i] = 0;
            }
            for (Expense exp : cat.getExpenses()) {
                persent[i] += exp.getAmount();
            }
            i++;
        }

        model.addAttribute("balances", user.getBalances());
        model.addAttribute("currentUser", user);
        model.addAttribute("incomes", user.getIncomes());
        model.addAttribute("plans", user.getPlans());
        model.addAttribute("notes", user.getNotes());
        model.addAttribute("balance", balance.getVal());

        model.addAttribute("dates", dates);
        model.addAttribute("values", values);

        model.addAttribute("categories", categories);
        model.addAttribute("persent", persent);

        return "dashboard.html";
    }


    @GetMapping("/history")
    public String history(Principal principal, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        List<Plan> plans = userService.findByUsername(principal.getName()).getPlans();
        List<Income> incomes = userService.findByUsername(principal.getName()).getIncomes();
        List<Expense> expenses = null;
        for (Plan plan : plans) {
            if (expenses == null) {
                expenses = plan.getExpenses();
            } else {
                expenses.addAll(plan.getExpenses());
            }
        }
        model.addAttribute("incomes", incomes);
        model.addAttribute("expenses", expenses);
        return "history.html";
    }

    @GetMapping("content/{aa}/{id}")
    public String edit(Principal principal,
                       @PathVariable("aa") String action,
                       @PathVariable("id") Long id,
                       @ModelAttribute("income") Income income,
                       @ModelAttribute("plan") Plan plan,
                       @ModelAttribute("expense") Expense expense,
                       @ModelAttribute("category") Category category,
                       Model model) {
        User user = userService.findByUsername(principal.getName());
        List<Income> incomes = user.getIncomes();
        List<Plan> plans = user.getPlans();
        List<Expense> expenses = null;
        for (Plan plan1 : plans) {
            if (expenses == null) {
                expenses = plan1.getExpenses();
            } else {
                expenses.addAll(plan1.getExpenses());
            }
        }
        int option = 0;
        if (action.equals("incomes")) {
            model.addAttribute("income", incomeService.findIncomeById(id));

        } else if (action.equals("plans")) {
            option = 1;
            model.addAttribute("plan", planService.findPlanById(id));

        } else if (action.equals("expenses")) {
            option = 2;
            model.addAttribute("expense", expenseService.findExpenseById(id));
        } else if (action.equals("categories")) {
            option = 3;
            model.addAttribute("category", categoryService.findCategoryById(id));
        }

        model.addAttribute("user", user)
                .addAttribute("option", option)
                .addAttribute("username", principal.getName())
                .addAttribute("incomes", incomes)
                .addAttribute("plans", plans)
                .addAttribute("expenses", expenses)
                .addAttribute("categories", categoryService.findAllCategory());
        return "content.html";
    }

    @GetMapping("/content")
    public String edit(Principal principal,
                       @ModelAttribute("income") Income income,
                       @ModelAttribute("plan") Plan plan,
                       @ModelAttribute("expense") Expense expense,
                       @ModelAttribute("category") Category category,
                       Model model) {

        User user = userService.findByUsername(principal.getName());
        List<Income> incomes = user.getIncomes();
        List<Plan> plans = user.getPlans();
        List<Expense> expenses = null;
        for (Plan plan1 : plans) {
            if (expenses == null) {
                expenses = plan1.getExpenses();
            } else {
                expenses.addAll(plan1.getExpenses());
            }
        }
        model.addAttribute("user", user)
                .addAttribute("username", principal.getName())
                .addAttribute("incomes", incomes)
                .addAttribute("plans", plans)
                .addAttribute("expenses", expenses)
                .addAttribute("categories", categoryService.findAllCategory());
        return "content.html";

    }
}
