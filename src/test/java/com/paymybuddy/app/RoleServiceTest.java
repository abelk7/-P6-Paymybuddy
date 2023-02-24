package com.paymybuddy.app;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.service.IRoleService;
import com.paymybuddy.app.service.impl.RoleService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RoleServiceTest {
    private IRoleService roleService;
    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    private void setup() {
        roleService = new RoleService(roleRepository);
    }

    @DisplayName(value = "1°) Recherche  de tous les roles")
    @Order(1)
    @Test
    void testGetAllRoles() {
        List<Role> roleList = new ArrayList<>();
        Role roleUser = new Role(1L,"USER");
        Role roleAdmin = new Role(2L, "ADMIN");
        roleList.add(roleUser);
        roleList.add(roleAdmin);

        when(roleRepository.findAll()).thenReturn(roleList);

        List<Role> rolesFind = roleService.getAllRoles();

        assertThat(rolesFind).isNotNull();
        assertThat(rolesFind.size()).isEqualTo(2);
        assertThat(rolesFind.get(0).getLibelle()).isEqualTo("USER");
        assertThat(rolesFind.get(1).getLibelle()).isEqualTo("ADMIN");
    }

    @DisplayName(value = "2°) Recherche  role USER")
    @Order(2)
    @Test
    void testGetRoleUser() {
        Role roleUser = new Role(1L,"USER");

        when(roleRepository.getRoleUser()).thenReturn(roleUser);

        Role role = roleService.getRoleUser();

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(1);
        assertThat(role.getLibelle()).isEqualTo("USER");
    }

    @DisplayName(value = "3°) Recherche  role ADMIN")
    @Order(3)
    @Test
    void testGetRoleAdmin() {
        Role roleAdmin = new Role(2L,"ADMIN");

        when(roleRepository.getRoleAdmin()).thenReturn(roleAdmin);

        Role role = roleService.getRoleAdmin();

        assertThat(role).isNotNull();
        assertThat(role.getId()).isEqualTo(2);
        assertThat(role.getLibelle()).isEqualTo("ADMIN");
    }
}
