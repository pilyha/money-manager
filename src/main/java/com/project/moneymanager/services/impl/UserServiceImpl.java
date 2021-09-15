package com.project.moneymanager.services.impl;


import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.RoleRepository;
import com.project.moneymanager.repositories.UserRepository;
import com.project.moneymanager.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER_INFO = LoggerFactory.getLogger("info");
    private static final Logger LOGGER_WARN = LoggerFactory.getLogger("warn");
    private static final Logger LOGGER_ERROR = LoggerFactory.getLogger("error");

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void saveWithUserRole(User user) {
        if (user != null) {
            LOGGER_INFO.info("Start create user: " + user.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(roleRepository.findByName("ROLE_USER"));
            userRepository.save(user);
            LOGGER_INFO.info("Start create plan: " + user.getUsername());
        } else {
            LOGGER_ERROR.error("User is null!");
        }
    }

    public void saveUserWithAdminRole(User user) {
        if (user != null) {
            LOGGER_INFO.info("Start create user: " + user.getUsername());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
            userRepository.save(user);
            LOGGER_INFO.info("Start create plan: " + user.getUsername());
        } else {
            LOGGER_ERROR.error("User is null!");
        }
    }

    public User findByUsername(String username) {
        if (userRepository.findByUsername(username) != null) {
            return userRepository.findByUsername(username);
        } else {
            LOGGER_ERROR.error("User doesn't exists");
            return null;
        }
    }

    public User findById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User getUserByEmail(String email) {
        if (userRepository.findByEmail(email) != null) {
            return userRepository.findByEmail(email);
        } else {
            LOGGER_ERROR.error("User doesn't exists");
            return null;
        }
    }

    public List<User> findAll() {
        LOGGER_INFO.info("Read all users");
        return (List<User>) userRepository.findAll();

    }
}
