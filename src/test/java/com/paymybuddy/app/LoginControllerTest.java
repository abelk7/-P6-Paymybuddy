package com.paymybuddy.app;

import com.paymybuddy.app.model.Compte;
import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.UtilisateurRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LoginControllerTest {
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UtilisateurRepository utilisateurRepository;


    @Test
    @Order(1)
    void testGetLoginPage() throws Exception {

        this.mockMvc
                .perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));

    }

    @DisplayName("2°) Connexion avec un utilisateur")
    @Order(2)
    @Test
    void testLogin() throws Exception {
        Role roleUser = new Role(1L, "USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleUser);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(7L);
        utilisateur1.setNom("Paul");
        utilisateur1.setPrenom("Jordan");
        utilisateur1.setEmail("j.paul@user.com");
        utilisateur1.setRoles(roleList);
        utilisateur1.setPassword(passwordEncoder.encode("123456789"));
        utilisateur1.setDateInscription(new Date());
        utilisateur1.setDateNaissance(new Date());
        Compte compte1 = new Compte(null, BigDecimal.valueOf(305L), utilisateur1);
        utilisateur1.setCompte(compte1);

        when(utilisateurRepository.findByEmail(anyString())).thenReturn(utilisateur1);
        RequestBuilder requestBuilder = formLogin().user("j.paul@user.com").password("123456789");
        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().isFound());

    }

    @DisplayName("3°) Connexion avec fausse identifiants")
    @Order(3)
    @Test
    void testLoginWithBadCredential() throws Exception {
        Role roleUser = new Role(1L, "USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleUser);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(7L);
        utilisateur1.setNom("Paul");
        utilisateur1.setPrenom("Jordan");
        utilisateur1.setEmail("j.paul@user.com");
        utilisateur1.setRoles(roleList);
        utilisateur1.setPassword(passwordEncoder.encode("123456789987654321"));
        utilisateur1.setDateInscription(new Date());
        utilisateur1.setDateNaissance(new Date());
        Compte compte1 = new Compte(null, BigDecimal.valueOf(305L), utilisateur1);
        utilisateur1.setCompte(compte1);

        when(utilisateurRepository.findByEmail(anyString())).thenReturn(utilisateur1);
        RequestBuilder requestBuilder = formLogin().user("j.paul@user.com").password("123456789");
        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/login?error"))
                .andExpect(status().isFound());

    }

    @DisplayName("4°) Deconnexion d'un utilisateur")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(4)
    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(status().isFound());
    }
}
