package com.project.moneymanager;

import com.project.moneymanager.controllers.CategoryController;
import com.project.moneymanager.models.Category;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.CategoryRepository;
import com.project.moneymanager.services.CategoryService;
import com.project.moneymanager.services.UserService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"create-user-before.sql"},executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"delete-user-after.sql"},executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    CategoryController categoryController;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(categoryController).isNotNull();
    }

    @Test
    void createCategory() throws Exception {
        User user = userService.findUserByUsername("Illia");
        Category category = new Category("Oil");
        category.setUser(user);
        this.mockMvc.perform(post("/categories/new")
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name",category.getName()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
        categoryService.deleteCategory(categoryService.findAllCategory().get(0).getId());
    }

    @Test
    void updateCategory() throws Exception {
        User user = userService.findUserByUsername("Illia");
        Category category = new Category("Water");
        category.setUser(user);
        categoryService.addCategory(category);
        Category newCategory = new Category("Chips");
        Long id = categoryService.findAllCategory().get(0).getId();
        this.mockMvc.perform(patch("/categories/" + id)
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name",newCategory.getName()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
        categoryService.deleteCategory(id);
    }

    @Test
    void deleteCategory() throws Exception{
        User user = userService.findUserByUsername("Illia");
        Category category = new Category("Water");
        category.setUser(user);
        categoryService.addCategory(category);
        Long id = categoryService.findAllCategory().get(0).getId();
        this.mockMvc.perform(delete("/categories/delete/" + id)
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
    }
}
