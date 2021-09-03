package com.project.moneymanager.services;


import com.project.moneymanager.models.User;
import com.project.moneymanager.repositories.RoleRepository;
import com.project.moneymanager.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
//        user.setRoles(new HashSet<>(roleRepository.findAll()));
        userRepository.save(user);
    }

    public void saveWithUserRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_USER"));
        userRepository.save(user);
    }

    public void saveUserWithAdminRole(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(roleRepository.findByName("ROLE_ADMIN"));
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        return this.userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    public boolean authenticateUser(String email, String password) {
        User user = this.userRepository.findByEmail(email);
        if (user == null)
            return false;

        return BCrypt.checkpw(password, user.getPassword());
    }

    public List<User> findAll() {
        return (List<User>) userRepository.findAll();

    }

    public User findUserById(Long id) {
        Optional<User> u = userRepository.findById(id);

        if (u.isPresent()) {
            return u.get();
        } else {
            return null;
        }
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
