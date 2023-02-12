package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Query(value = "SELECT * FROM roles r WHERE r.libelle='USER'", nativeQuery = true)
    Role getRoleUser();

    @Query(value = "SELECT * FROM roles r WHERE r.libelle='ADMIN'", nativeQuery = true)
    Role getRoleAdmin();
}
