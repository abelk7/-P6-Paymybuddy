package com.paymybuddy.app.it;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.service.IRoleService;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoleServiceTestIT {
    @Autowired
    private IRoleService roleService;

    @DisplayName("Get roles from database")
    @Test
    public void testGetAllRoles() {
        List<Role> rolesList = roleService.getAllRoles();
        assertThat(rolesList).isNotNull();
        assertThat(rolesList.get(0).getLibelle()).isEqualTo("USER");
        assertThat(rolesList.get(1).getLibelle()).isEqualTo("ADMIN");
    }

    @DisplayName("Get role USER")
    @Test
    public void testGetRoleUser() {
        Role roleUser = roleService.getRoleUser();
        assertThat(roleUser).isNotNull();
        assertThat(roleUser.getId()).isEqualTo(1L);
        assertThat(roleUser.getLibelle()).isEqualTo("USER");
    }

    @DisplayName("Get role ADMIN")
    @Test
    public void testGetRoleAdmin() {
        Role roleAdmin = roleService.getRoleAdmin();
        assertThat(roleAdmin).isNotNull();
        assertThat(roleAdmin.getId()).isEqualTo(2L);
        assertThat(roleAdmin.getLibelle()).isEqualTo("ADMIN");
    }
}
