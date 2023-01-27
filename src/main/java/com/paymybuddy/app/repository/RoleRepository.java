package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
