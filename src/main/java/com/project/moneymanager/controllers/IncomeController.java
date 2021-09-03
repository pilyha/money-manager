package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.IncomeService;
import com.project.moneymanager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/incomes")
public class IncomeController {

    private final IncomeService incomeService;
    private final UserService userService;

    public IncomeController(IncomeService incomeService,UserService userService) {
        this.incomeService = incomeService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String displayIncomeCreation(Principal principal, @ModelAttribute("income") Income income, Model model) {
        model.addAttribute("user", userService.findUserByUsername(principal.getName()));
        return "addIncome.html";
    }

    @PostMapping(value = "/new")
    public String createNewIncome(Principal principal, @Valid @ModelAttribute("income") Income income, BindingResult result, Model model, RedirectAttributes limitError) {
        User user = userService.findUserByUsername(principal.getName());
        if (result.hasErrors()) {
            return "addIncome.html";
        } else {
            incomeService.addIncome(user, income);
            return "redirect:/dashboard";
        }
    }

    @PatchMapping(value = "/{id}")
    public String editIncome(Principal principal, @Valid @ModelAttribute("income") Income income,
                             @PathVariable("id") Long id,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/content";
        }

        incomeService.updateIncome(id,income);
        return "redirect:/content";
    }

    @DeleteMapping(value = "/delete/{id}")
    public String deleteIncome(Principal principal, @PathVariable("id") Long id) {
        User user = userService.findUserByUsername(principal.getName());
        incomeService.deleteIncome(user, id);
        return "redirect:/content";
    }
}
