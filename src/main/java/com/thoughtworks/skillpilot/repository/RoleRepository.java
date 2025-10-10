package com.thoughtworks.skillpilot.repository;

import com.thoughtworks.skillpilot.model.Role;
import com.thoughtworks.skillpilot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
}

