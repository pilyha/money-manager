package com.project.moneymanager.services;

import com.project.moneymanager.models.Balance;
import com.project.moneymanager.models.User;

public interface BalanceService {
    Balance getLastBalance(User user);

    void transaction(User user);
}
