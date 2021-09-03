package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.Plan;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {
    @Autowired
    private ExpenseService expenseService;
    @Autowired
    private UserService userService;
    @Autowired
    private BalanceService balanceService;

    @GetMapping("/new")
    public String newExpenses(Principal principal, @ModelAttribute("expense") Expense expense, Model model) {
        model.addAttribute("user", userService.findUserByUsername(principal.getName()));
        model.addAttribute("categories", userService.findUserByUsername(principal.getName()).getCategories());
        return "addExpense.html";
    }

    @PostMapping(value = "/new")
    public String createNewExpense(Principal principal, @Valid @ModelAttribute("expense") Expense expense, BindingResult result, Model model, RedirectAttributes rAttributes) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user",user);
        if (result.hasErrors()) {
            return "addExpense.html";
        } else {
            for (Plan plan : user.getPlans()) {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd ");
                String today = df.format(Calendar.getInstance().getTime());
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String strDate = dateFormat.format(plan.getStart_datez());
                if (today.compareTo(strDate) > 0) {

                    if (balanceService.getLastBalance(user).getVal() + expense.getAmount() > plan.getLimitz()) {
                        rAttributes.addFlashAttribute("amounterrror", "your expense have  exceeded your plan");
                        return "redirect:/expense/new";
                    } else {
                        expense.setPlan(plan);
                        expenseService.addExpense(user, expense);
                        System.out.println("expense added to plan");
                        return "redirect:/dashboard";
                    }
                }
            }
            rAttributes.addFlashAttribute("error", "there is no plan to add");
            return "redirect:/expense/new";
        }
    }

    @PatchMapping(value = "/{id}")
    public String editExpense(Principal principal, @Valid @ModelAttribute("expense") Expense expense,
                              @PathVariable("id") Long id,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/content";
        }
        expenseService.updateExpense(id,expense);
        return "redirect:/content";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteExpense(Principal principal, @PathVariable("id") Long id) {
        User user = userService.findUserByUsername(principal.getName());
        expenseService.deleteExpense(user, id);
        return "redirect:/content";
    }
}
