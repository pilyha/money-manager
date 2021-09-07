package com.project.moneymanager;

import com.project.moneymanager.controllers.IncomeController;
import com.project.moneymanager.models.Category;
import com.project.moneymanager.models.Income;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.IncomeRepository;
import com.project.moneymanager.services.IncomeService;
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
public class IncomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IncomeController incomeController;

    @Autowired
    IncomeRepository incomeRepository;

    @Autowired
    IncomeService incomeService;

    @Autowired
    UserService userService;

    @Test
    void contextLoads() {
        assertThat(incomeController).isNotNull();
    }

    @Test
    void createIncome() throws Exception {
        User user = userService.findUserByUsername("Illia");
        Income income = new Income(1000,"Salary",(Calendar.getInstance().getTime()));
        income.setUser(user);
        this.mockMvc.perform(post("/incomes/new")
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("amount", String.valueOf(income.getAmount()))
                .param("description",income.getDescription())
                .param("date", new SimpleDateFormat("yyyy-MM-dd ").format(income.getDate())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
        incomeService.deleteIncome(user,incomeService.findAllIncomes().get(0).getId());
    }

    @Test
    void updateIncome() throws Exception {
        User user = userService.findUserByUsername("Illia");
        Income income = new Income(1000,"Salary",(Calendar.getInstance().getTime()));
        income.setUser(user);
        incomeService.addIncome(user,income);
        Income newIncome = new Income(500,"Present",null);
        Long id  = incomeService.findAllIncomes().get(0).getId();
        this.mockMvc.perform(patch("/incomes/" + id)
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("amount", String.valueOf(newIncome.getAmount()))
                .param("description", newIncome.getDescription()))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
        incomeService.deleteIncome(user,id);
    }

    @Test
    void deleteIncome() throws Exception{
        User user = userService.findUserByUsername("Illia");
        Income income = new Income(1000,"Salary",(Calendar.getInstance().getTime()));
        income.setUser(user);
        incomeService.addIncome(user,income);
        Long id  = incomeService.findAllIncomes().get(0).getId();
        this.mockMvc.perform(delete("/incomes/" + id)
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
    }
}
