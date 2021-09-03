package com.project.moneymanager.services;

import com.project.moneymanager.models.Plan;
import com.project.moneymanager.repositories.PlanRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanService {

    private final PlanRepository planRepository;

    public PlanService(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public Plan addPlan(Plan plan) {
        planRepository.save(plan);
        return plan;
    }

    public Plan findPlanByID(Long id) {
        return planRepository.findById(id).orElse(null);
    }

    public void updatePlan(Long id, Plan plan) {
        Plan newPlan = findPlanByID(id);
        newPlan.setLimitz(plan.getLimitz());
        newPlan.setName(plan.getName());
        planRepository.save(newPlan);
    }

    public void deletePlan(Long id) {
        planRepository.deleteById(id);
    }

    public List<Plan> findAllPlans() {
        return (List<Plan>) planRepository.findAll();
    }
}
