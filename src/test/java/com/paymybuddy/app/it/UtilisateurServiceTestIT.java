package com.paymybuddy.app.it;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UtilisateurServiceTestIT {

    @Autowired
    IUtilisateurService utilisateurService;

    @DisplayName("Fetching user in database")
    @Test
    public void testGetUser() throws Exception {
        Utilisateur utilisateur = utilisateurService.getUser("user@user.com");
        assertThat(utilisateur).isNotNull();
    }

    @DisplayName("Fetching unexist user in database")
    @Test
    public void testGetUserUnexistUser() throws Exception {
        Utilisateur utilisateur = utilisateurService.getUser("user123@user.com");
        assertThat(utilisateur).isNull();
    }

    @DisplayName("Saving new user user")
    @Test
    public void testSaveNewUser() {
        List<Role> roleListUser= new ArrayList<>();
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
    @Test
    public void testSaveUser() {
        Utilisateur utilisateur = utilisateurService.getUser("user@user.com");
        utilisateur.setNom("newNomTest");
        utilisateur.setPrenom("newPrenomTest");
        utilisateur.setEmail("user123@user.com");

        Utilisateur utilisateurSaved = utilisateurService.save(utilisateur);

        assertThat(utilisateurSaved).isNotNull();
    }

}
