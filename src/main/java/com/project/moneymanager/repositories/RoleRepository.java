package com.project.moneymanager.repositories;

import com.project.moneymanager.models.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    List<Role> findByName(String name);
}
