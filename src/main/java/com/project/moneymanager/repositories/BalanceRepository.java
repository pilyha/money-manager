package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Balance;
import org.springframework.data.repository.CrudRepository;

public interface BalanceRepository extends CrudRepository<Balance,Long> {
}
