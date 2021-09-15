package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Category;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.CategoryService;
import com.project.moneymanager.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @PostMapping(value = "/new")
    public String createCategory(Principal principal, @Valid @ModelAttribute("category") Category category, BindingResult result,
                                 RedirectAttributes rAttributes) {
        User user = userService.findByUsername(principal.getName());
        if (result.hasErrors()) {
            return "redirect:/dashboard";
        }
        category.setUser(user);
        try {
            categoryService.addCategory(category);
        } catch (RuntimeException e) {
            rAttributes.addFlashAttribute("error", "Name already exists");
            return "redirect:/dashboard";
        }
        return "redirect:/dashboard";
    }

    @PatchMapping(value = "/{id}")
    public String editCategory(@Valid @ModelAttribute("category") Category category,
                               @PathVariable("id") Long id,
                               BindingResult result, RedirectAttributes rAttributes) {
        if (result.hasErrors()) {
            return "redirect:/content";
        }
        try {
            categoryService.updateCategory(id, category);
        } catch (RuntimeException e) {
            rAttributes.addFlashAttribute("errorCategory", "Name already exists");
            return "redirect:/content";
        }

        return "redirect:/content";
    }

    @DeleteMapping(value = "/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/content";
    }
}
