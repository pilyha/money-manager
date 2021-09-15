package com.project.moneymanager.services;

import com.project.moneymanager.models.User;

import java.util.List;

public interface UserService {

    void saveWithUserRole(User user);

    void saveUserWithAdminRole(User user);

    User findByUsername(String username);

    User findById(Long id);

    User getUserByEmail(String email);

    List<User> findAll();
}
