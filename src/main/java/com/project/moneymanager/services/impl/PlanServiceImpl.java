package com.project.moneymanager.services.impl;

import com.project.moneymanager.models.Plan;
import com.project.moneymanager.repositories.PlanRepository;
import com.project.moneymanager.services.PlanService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanServiceImpl implements PlanService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final PlanRepository planRepository;

    @Autowired
    public PlanServiceImpl(PlanRepository planRepository) {
        this.planRepository = planRepository;
    }

    public void addPlan(Plan plan) {
        if (plan != null) {
            LOGGER_INFO.info("Start create plan: " + plan.getName());
            planRepository.save(plan);
            LOGGER_INFO.info("End create plan: " + plan.getName());
        } else {
            LOGGER_ERROR.error("Plan is null!");
        }
    }

    public Plan findPlanById(Long id) {
        return planRepository.findById(id).orElse(null);
    }

    public void updatePlan(Long id, Plan plan) {
        if (planRepository.existsById(id)) {
            LOGGER_WARN.warn("Start update plan: " + id);
            Plan newPlan = findPlanById(id);
            newPlan.setLimitz(plan.getLimitz());
            newPlan.setName(plan.getName());
            planRepository.save(newPlan);
            LOGGER_WARN.warn("Start update note: " + id);
        } else {
            LOGGER_ERROR.error("Plan doesn't exists");
        }
    }

    public void deletePlan(Long id) {
        if (planRepository.existsById(id)) {
            LOGGER_WARN.warn("Start delete plan: " + id);
            planRepository.deleteById(id);
            LOGGER_WARN.warn("End delete plan: " + id);
        } else {
            LOGGER_ERROR.error("Plan doesn't exists");
        }
    }

    public List<Plan> findAllPlans() {
        LOGGER_INFO.info("Read all plans");
        return (List<Plan>) planRepository.findAll();
    }
}
