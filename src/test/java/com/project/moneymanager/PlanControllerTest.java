package com.project.moneymanager;

import com.project.moneymanager.controllers.PlanController;
import com.project.moneymanager.models.Plan;
import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.PlanRepository;
import com.project.moneymanager.services.PlanService;
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
@Sql(value = {"create-user-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"delete-user-after.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PlanController planController;

    @Autowired
    PlanRepository planRepository;

    @Autowired
    PlanService planService;

    @Autowired
    UserService userService;


    @Test
    void contextLoads() {
        assertThat(planController).isNotNull();
        assertThat(userService).isNotNull();
    }


    @Test
    void createPlan() throws Exception {
        User user = userService.findUserByUsername("Illia");
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        Plan plan = new Plan("August", 1000, pattern.parse("2021-08-01"), pattern.parse("2021-09-01"));
        plan.setUser(user);
        this.mockMvc.perform(post("/plans/new")
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", plan.getName())
                .param("limitz", String.valueOf(plan.getLimitz()))
                .param("start_datez", pattern.format(plan.getStart_datez()))
                .param("end_datez", pattern.format(plan.getEnd_datez())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
        planService.deletePlan(planService.findAllPlans().get(0).getId());
    }

    @Test
    void updatePlan() throws Exception {
        User user = userService.findUserByUsername("Illia");
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        Plan plan = new Plan("August", 1000, pattern.parse("2021-08-01"), pattern.parse("2021-09-01"));
        plan.setUser(user);
        planService.addPlan(plan);
        Plan newPlan = new Plan("August 1/2", 500, null, null);
        Long id = planService.findAllPlans().get(0).getId();
        this.mockMvc.perform(patch("/plans/" + id)
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .param("name", newPlan.getName())
                .param("limitz", String.valueOf(newPlan.getLimitz())))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
        planService.deletePlan(id);
    }

    @Test
    void deletePlan() throws Exception {
        User user = userService.findUserByUsername("Illia");
        SimpleDateFormat pattern = new SimpleDateFormat("yyyy-MM-dd");
        Plan plan = new Plan("August", 1000, pattern.parse("2021-08-01"), pattern.parse("2021-09-01"));
        plan.setUser(user);
        planService.addPlan(plan);
        Long id = planService.findAllPlans().get(0).getId();
        this.mockMvc.perform(delete("/plans/delete/" + id)
                .with(user("Illia"))
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/content"));
    }
}
