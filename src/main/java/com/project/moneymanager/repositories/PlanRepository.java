package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Plan;
import org.springframework.data.repository.CrudRepository;

import java.lang.annotation.Native;
import java.util.List;

public interface PlanRepository extends CrudRepository<Plan, Long> {
}
