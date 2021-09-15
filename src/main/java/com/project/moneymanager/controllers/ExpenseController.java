package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.BalanceService;
import com.project.moneymanager.services.ExpenseService;
import com.project.moneymanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;

    @Autowired
    public ExpenseController(ExpenseService expenseService, UserService userService) {
        this.expenseService = expenseService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String displayExpenseCreation(Principal principal, @ModelAttribute("expense") Expense expense, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        model.addAttribute("categories", userService.findByUsername(principal.getName()).getCategories());
        return "addExpense.html";
    }

    @PostMapping(value = "/new")
    public String createExpense(Principal principal, @Valid @ModelAttribute("expense") Expense expense, BindingResult result, Model model, RedirectAttributes rAttributes) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "addExpense.html";
        } else {
            try {
                expenseService.addExpense(user, expense);
                return "redirect:/dashboard";
            } catch (NullPointerException e) {
                rAttributes.addFlashAttribute("error", "There is no plan to add");
                return "redirect:/expenses/new";
            } catch (RuntimeException e) {
                rAttributes.addFlashAttribute("amountError", "Expense have exceeded the plan");
                return "redirect:/expenses/new";
            }
        }
    }

    @PatchMapping(value = "/{id}")
    public String editExpense(@Valid @ModelAttribute("expense") Expense expense,
                              @PathVariable("id") Long id,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/content";
        }
        expenseService.updateExpense(id, expense);
        return "redirect:/content";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteExpense(Principal principal, @PathVariable("id") Long id) {
        User user = userService.findByUsername(principal.getName());
        expenseService.deleteExpense(user, id);
        return "redirect:/content";
    }
}
