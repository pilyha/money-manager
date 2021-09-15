package com.project.moneymanager.services;

import com.project.moneymanager.models.Plan;

import java.util.List;

public interface PlanService {

    void addPlan(Plan plan);

    Plan findPlanById(Long id);

    void updatePlan(Long id, Plan plan);

    void deletePlan(Long id);

    List<Plan> findAllPlans();
}
