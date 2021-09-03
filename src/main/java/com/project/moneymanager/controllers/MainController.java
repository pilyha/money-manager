package com.project.moneymanager.controllers;

import com.project.moneymanager.models.*;
import com.project.moneymanager.services.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class MainController {
    private final MainService mainService;
    private final UserService userService;
    private final PlanService planService;
    private final IncomeService incomeService;
    private final BalanceService balanceService;
    private final ExpenseService expenseService;
    private final CategoryService categoryService;

    public MainController(MainService mainService, UserService userService, PlanService planService, IncomeService incomeService, BalanceService balanceService, ExpenseService expenseService, CategoryService categoryService) {
        this.mainService = mainService;
        this.userService = userService;
        this.planService = planService;
        this.incomeService = incomeService;
        this.balanceService = balanceService;
        this.expenseService = expenseService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard")
    public String homepage(Principal principal, Model model, @ModelAttribute("note") Note note, @ModelAttribute("category") Category category, BindingResult result) {
        User user = userService.findUserByUsername(principal.getName());
        Balance balance = null;
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

        return "home.html";
    }

    @PostMapping(value = "/addNote")
    public String newNote(Principal principal, @Valid @ModelAttribute("note") Note note, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/dashboard";
        }
        User user = userService.findUserByUsername(principal.getName());
        mainService.createNote(user, note);
        return "redirect:/dashboard";
    }

    @GetMapping("/history")
    public String history(Principal principal, Model model) {
        model.addAttribute("user", userService.findUserByUsername(principal.getName()));
        List<Plan> plans = userService.findUserByUsername(principal.getName()).getPlans();
        List<Income> incomes = userService.findUserByUsername(principal.getName()).getIncomes();
        List<Expense> expenses = null;
        for (Plan plan : plans) {
            if (expenses == null) {
                expenses = plan.getExpenses();
            } else {
                expenses.addAll(plan.getExpenses());
            }
        }
        model.addAttribute("username", principal.getName());
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
                       @ModelAttribute("cate") Category cate,
                       BindingResult result,
                       Model model, RedirectAttributes rAttributes) {
        User user = userService.findUserByUsername(principal.getName());
        List<Income> incomes = user.getIncomes();
        List<Plan> plans = user.getPlans();
        List<Expense> expenses = null;
        for (Plan pland : plans) {
            if (expenses == null) {
                expenses = pland.getExpenses();
            } else {
                expenses.addAll(pland.getExpenses());
            }
        }
        if (incomes == null) {
            rAttributes.addFlashAttribute("errori", "this field is empty");
        }
        if (plans == null) {
            rAttributes.addFlashAttribute("errorp", "this field is empty");
        }
        if (expenses == null) {
            rAttributes.addFlashAttribute("errore", "this field is empty");
        }
        int option = 0;
        if (action.equals("incomes")) {
            model.addAttribute("income", incomeService.findIncomeById(id));

        } else if (action.equals("plans")) {
            option = 1;
            model.addAttribute("plan", planService.findPlanByID(id));

        } else if (action.equals("expenses")) {
            option = 2;
            model.addAttribute("expense", expenseService.findExpenseById(id));
        } else if (action.equals("categories")) {
            option = 3;
            model.addAttribute("category", categoryService.findCategoryById(id));
        }

        model.addAttribute("user", user);
        model.addAttribute("option", option);
        model.addAttribute("username", principal.getName());
        model.addAttribute("incomes", incomes);
        model.addAttribute("plans", plans);
        model.addAttribute("expenses", expenses);
        model.addAttribute("categories", categoryService.findAllCategory());
        return "edit.html";
    }

    @GetMapping("/content")
    public String edit(Principal principal,
                       @ModelAttribute("income") Income income,
                       @ModelAttribute("plan") Plan plan,
                       @ModelAttribute("expense") Expense expense,
                       @ModelAttribute("cate") Category cate,
                       BindingResult result,
                       Model model, RedirectAttributes rAttributes) {

            User user = userService.findUserByUsername(principal.getName());
            List<Income> incomes = user.getIncomes();
            List<Plan> plans = user.getPlans();
            List<Expense> expenses = null;
            for (Plan pland : plans) {
                if (expenses == null) {
                    expenses = pland.getExpenses();
                } else {
                    expenses.addAll(pland.getExpenses());
                }
            }
            if (incomes == null) {
                rAttributes.addFlashAttribute("errori", "this field is empty");
            }
            if (plans == null) {
                rAttributes.addFlashAttribute("errorp", "this field is empty");
            }
            if (expenses == null) {
                rAttributes.addFlashAttribute("errore", "this field is empty");
            }
            int option = 5;
        model.addAttribute("user", user);
        model.addAttribute("option", option);
        model.addAttribute("username", principal.getName());
        model.addAttribute("incomes", incomes);
        model.addAttribute("plans", plans);
        model.addAttribute("expenses", expenses);
        model.addAttribute("categories", categoryService.findAllCategory());
        return "edit.html";

    }
}

