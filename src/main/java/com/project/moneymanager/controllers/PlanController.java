package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Plan;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.PlanService;
import com.project.moneymanager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/plans")
public class PlanController {

    private final PlanService planService;
    private final UserService userService;

    public PlanController(PlanService planService, UserService userService) {
        this.planService = planService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newPlan(Principal principal, @ModelAttribute("plan") Plan plan, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);

        return "addPlan.html";
    }

    @PostMapping(value = "/new")
    public String createPlan(Principal principal, @Valid @ModelAttribute("plan") Plan plan, BindingResult result, Model model) {
        User user = userService.findUserByUsername(principal.getName());
        model.addAttribute("user", user);
        if (result.hasErrors()) {
            return "addPlan.html";
        } else {
            plan.setUser(user);
            planService.addPlan(plan);
            return "redirect:/dashboard";
        }
    }

    @PatchMapping(value = "/{id}")
    public String editPlan(Principal principal, @Valid @ModelAttribute("plan") Plan plan,
                           @PathVariable("id") Long id,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/content";
        }
        planService.updatePlan(id, plan);
        return "redirect:/content";
    }

    @DeleteMapping(value = "/{id}")
    public String deletePlan(Principal principal, @PathVariable("id") Long id) {
        planService.deletePlan(id);
        return "redirect:/content";
    }
}
