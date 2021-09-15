package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.IncomeService;
import com.project.moneymanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final UserService userService;

    @Autowired
    public IncomeController(IncomeService incomeService, UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String displayIncomeCreation(Principal principal, @ModelAttribute("income") Income income, Model model) {
        model.addAttribute("user", userService.findByUsername(principal.getName()));
        return "addIncome.html";
    }

    @PostMapping(value = "/new")
    public String createIncome(Principal principal, @Valid @ModelAttribute("income") Income income, BindingResult result) {
        User user = userService.findByUsername(principal.getName());
        if (result.hasErrors()) {
            return "addIncome.html";
        } else {
            incomeService.addIncome(user, income);
            return "redirect:/dashboard";
        }
    }

    @PatchMapping(value = "/{id}")
    public String editIncome(@Valid @ModelAttribute("income") Income income, @PathVariable("id") Long id, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/content";
        } else {
            incomeService.updateIncome(id, income);
            return "redirect:/content";
        }
    }

    @DeleteMapping(value = "/{id}")
    public String deleteIncome(Principal principal, @PathVariable("id") Long id) {
        User user = userService.findByUsername(principal.getName());
        incomeService.deleteIncome(user, id);
        return "redirect:/content";
    }
}
