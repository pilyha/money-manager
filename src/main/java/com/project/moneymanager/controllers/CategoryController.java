package com.project.moneymanager.controllers;

import com.project.moneymanager.models.Category;
import com.project.moneymanager.models.User;
import com.project.moneymanager.services.CategoryService;
import com.project.moneymanager.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

    public CategoryController(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @GetMapping("/new")
    public String newCategory(Principal principal, @ModelAttribute("category") Category category, Model model) {
        model.addAttribute("username", principal.getName());
        return "addCategory.html";
    }

    @PostMapping(value = "/new")
    public String createCategory(Principal principal, @Valid @ModelAttribute("category") Category category, BindingResult result,
                                 RedirectAttributes rAttributes) {
        User user = userService.findUserByUsername(principal.getName());
        if (result.hasErrors()) {
            return "redirect:/dashboard";
        }
        for (Category cat : categoryService.findAllCategory()) {
            if (category.getName().equals(cat.getName())) {
                rAttributes.addFlashAttribute("error", "name already exists");
                return "redirect:/dashboard";
            }
        }
        category.setUser(user);
        categoryService.addCategory(category);
        return "redirect:/dashboard";
    }

    @PatchMapping(value="/{id}")
    public String editCategory(@Valid@ModelAttribute("cate") Category category,
                               @PathVariable("id") Long id,
                               BindingResult result) {
        if(result.hasErrors()){
            return "redirect:/content";
        }
        categoryService.updateCategory(id,category);
        return "redirect:/content";
    }


    @DeleteMapping(value = "/{id}")
    public String deleteCategory(@PathVariable("id") Long id) {
        categoryService.deleteCategory(id);
        return "redirect:/content";
    }
}
