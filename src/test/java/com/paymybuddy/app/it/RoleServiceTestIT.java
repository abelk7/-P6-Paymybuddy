package com.paymybuddy.app.it;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.service.IRoleService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleServiceTestIT {
    @Autowired
    private IRoleService roleService;

    @DisplayName("1°) Recherche  de tous les roles from database")
    @Test
    public void testGetAllRoles() {
        List<Role> rolesList = roleService.getAllRoles();
        assertThat(rolesList).isNotNull();
        assertThat(rolesList.get(0).getLibelle()).isEqualTo("USER");
        assertThat(rolesList.get(1).getLibelle()).isEqualTo("ADMIN");
    }

    @DisplayName("2°)Get role USER from database")
    @Test
    public void testGetRoleUser() {
        Role roleUser = roleService.getRoleUser();
        assertThat(roleUser).isNotNull();
        assertThat(roleUser.getId()).isEqualTo(1L);
        assertThat(roleUser.getLibelle()).isEqualTo("USER");
    }

    @DisplayName("3°)Get role ADMIN from database")
    @Test
    public void testGetRoleAdmin() {
        Role roleAdmin = roleService.getRoleAdmin();
        assertThat(roleAdmin).isNotNull();
        assertThat(roleAdmin.getId()).isEqualTo(2L);
        assertThat(roleAdmin.getLibelle()).isEqualTo("ADMIN");
    }
}
