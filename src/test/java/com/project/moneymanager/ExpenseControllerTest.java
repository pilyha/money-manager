package com.project.moneymanager;

import com.project.moneymanager.controllers.ExpenseController;
import com.project.moneymanager.models.Expense;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.ExpenseRepository;
import com.project.moneymanager.services.CategoryService;
import com.project.moneymanager.services.ExpenseService;
import com.project.moneymanager.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
@Sql(value = {"create-user-plan-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"delete-user-balance-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ExpenseController expenseController;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    ExpenseService expenseService;

    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    @Test
    @Order(1)
    void contextLoads() {
        assertThat(expenseController).isNotNull();
        assertThat(userService).isNotNull();
        assertThat(categoryService).isNotNull();
        assertThat(expenseService).isNotNull();
        assertThat(expenseRepository).isNotNull();
    }


    @Test
    void createExpenses() throws Exception {
        User user = userService.findByUsername("Illia");
        Expense expense = new Expense(999, "Gifts", (Calendar.getInstance().getTime()));
        expense.setUser(user);
        this.mockMvc.perform(post("/expenses/new")
                        .with(user("Illia"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("amount", String.valueOf(expense.getAmount()))
                        .param("description", expense.getDescription())
                        .param("date", new SimpleDateFormat("yyyy-MM-dd ").format(expense.getDate()))
                        .param("category", String.valueOf(categoryService.findCategoryById(1L).getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));

        Assertions.assertTrue(expenseRepository.existsById(expenseService.findAllExpenses().get(0).getId()));
        expenseService.deleteExpense(user, expenseService.findAllExpenses().get(0).getId());
    }

    @Test
    void updateExpense() throws Exception {
        User user = userService.findByUsername("Illia");
        Expense expense = new Expense(999, "Gifts", (Calendar.getInstance().getTime()));
        expense.setUser(user);
        expenseService.addExpense(user, expense);
        Expense newExpense = new Expense(500, "Present", null);
        Long id = expenseService.findAllExpenses().get(0).getId();
        this.mockMvc.perform(patch("/expenses/" + id)
                        .with(user("Illia"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                        .param("amount", String.valueOf(newExpense.getAmount()))
                        .param("description", newExpense.getDescription())
                        .param("category", String.valueOf(categoryService.findCategoryById(2L).getId())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));

        Assertions.assertEquals(newExpense.getDescription(), expenseService.findExpenseById(id).getDescription());
        expenseService.deleteExpense(user, id);
    }

    @Test
    void deleteExpense() throws Exception {
        User user = userService.findByUsername("Illia");
        Expense expense = new Expense(999, "Gifts", (Calendar.getInstance().getTime()));
        expense.setUser(user);
        expenseService.addExpense(user, expense);
        Long id = expenseService.findAllExpenses().get(0).getId();
        this.mockMvc.perform(delete("/expenses/" + id)
                        .with(user("Illia"))
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));

        Assertions.assertFalse(expenseRepository.existsById(id));
    }
}
