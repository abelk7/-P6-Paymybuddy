package com.paymybuddy.app.it;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.service.IRoleService;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.fail;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtilisateurServiceTestIT {

    @Autowired
    private IUtilisateurService utilisateurService;
    @Autowired
    private IRoleService roleService;

    @DisplayName("Fetching user in database")
    @Order(1)
    @Test
    void testGetUser() {
        Utilisateur utilisateur = utilisateurService.getUser("user@user.com");
        assertThat(utilisateur).isNotNull();
    }

    @DisplayName("Fetching unexist user in database")
    @Order(2)
    @Test
    void testGetUserUnexistUser() {
        Utilisateur utilisateur = utilisateurService.getUser("user123@user.com");
        assertThat(utilisateur).isNull();
    }

    @DisplayName("Saving new user user")
    @Order(3)
    @Test
    void testSaveNewUser() {
        List<Role> roleListUser = new ArrayList<>();
        Role roleUser = new Role(1L, "USER");
        roleListUser.add(roleUser);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setNom("userNomTest");
        utilisateur1.setPrenom("userPrenomTest");
        utilisateur1.setEmail("usertest@user.com");
        utilisateur1.setRoles(roleListUser);
        utilisateur1.setPassword("123456789");
        utilisateur1.setDateInscription(new Date());
        utilisateur1.setDateNaissance(new Date());

        Utilisateur utilisateurSaved = utilisateurService.saveUser(utilisateur1);

        assertThat(utilisateurSaved).isNotNull();
    }

    @DisplayName("Saving user")
    @Order(4)
    @Test
    void testSaveUser() {
        Utilisateur utilisateur1 = utilisateurService.getUser("user2@user.com");

        utilisateur1.setNom("userNomTest2");
        utilisateur1.setPrenom("userPrenomTest2");
        utilisateur1.setEmail("usertest2@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(2004, 01, 02);
        LocalDate dateInscription = LocalDate.of(2022, 06, 26);
        utilisateur1.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur1.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        Utilisateur utilisateurSaved = utilisateurService.save(utilisateur1);

        assertThat(utilisateurSaved).isNotNull();
    }
}
