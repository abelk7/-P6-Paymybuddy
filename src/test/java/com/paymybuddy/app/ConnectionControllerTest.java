package com.paymybuddy.app;

import com.paymybuddy.app.controller.ConnectionController;
import com.paymybuddy.app.model.*;
import com.paymybuddy.app.security.SecurityConfig;
import com.paymybuddy.app.service.ITransactionService;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(ConnectionController.class)
public class ConnectionControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private IUtilisateurService utilisateurService;
    @MockBean
    private ITransactionService transactionService;

    @DisplayName("1°) Tentative d'ajouter une connection")
    @WithMockUser(username = "alain.page@user.com", roles = "USER", password ="123456789")
    @Test
    @Order(1)
    void testPostNewConnection() throws Exception {
        when(utilisateurService.getPasswordEncoder()).thenReturn(new BCryptPasswordEncoder());

        Role roleUser = new Role(1L, "USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleUser);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(7L);
        utilisateur1.setNom("Page");
        utilisateur1.setPrenom("alain");
        utilisateur1.setEmail("alain.page@user.com");
        utilisateur1.setRoles(roleList);
        utilisateur1.setPassword(utilisateurService.getPasswordEncoder().encode("123456789"));
        utilisateur1.setDateInscription(new Date());
        utilisateur1.setDateNaissance(new Date());


        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setId(7L);
        utilisateur2.setNom("Anissa");
        utilisateur2.setPrenom("Farqa");
        utilisateur2.setEmail("anissa.f@user.com");
        utilisateur2.setRoles(roleList);
        utilisateur2.setPassword(utilisateurService.getPasswordEncoder().encode("123456789"));
        utilisateur2.setDateInscription(new Date());
        utilisateur2.setDateNaissance(new Date());

        Compte compte1 = new Compte(null, BigDecimal.valueOf(305L), utilisateur1);
        Compte compte2 = new Compte(null, BigDecimal.valueOf(500L), utilisateur2);

        List<Connection> connectionList1 = new ArrayList<>();
        List<Connection> connectionList2 = new ArrayList<>();
        Connection connection1 = new Connection(1L,utilisateur1, utilisateur2);
        Connection connection2 = new Connection(2L,utilisateur2,utilisateur1);
        connectionList1.add(connection1);
        connectionList2.add(connection2);

        utilisateur1.setCompte(compte1);
        utilisateur1.setConnections(connectionList1);
        utilisateur2.setCompte(compte2);
        utilisateur2.setConnections(connectionList2);

        List<Transaction> transactionList = new ArrayList<>();
        TypeTransaction typeTransaction = new TypeTransaction(1L, "Virement");

        Transaction transaction1 = new Transaction();
        transaction1.setMontant(BigDecimal.valueOf(430L));
        transaction1.setCompteEmetteur(utilisateur1.getCompte());
        transaction1.setCompteBeneficiaire(utilisateur2.getCompte());
        transaction1.setTypeTransaction(typeTransaction);
        transaction1.setDate(new Date());
        transaction1.setLibelle(null);

        Transaction transaction2 = new Transaction();
        transaction2.setMontant(BigDecimal.valueOf(700L));
        transaction2.setCompteEmetteur(utilisateur1.getCompte());
        transaction2.setCompteBeneficiaire(utilisateur2.getCompte());
        transaction2.setTypeTransaction(typeTransaction);
        transaction2.setDate(new Date());
        transaction2.setLibelle(null);

        Transaction transaction3 = new Transaction();
        transaction3.setMontant(BigDecimal.valueOf(50L));
        transaction3.setCompteEmetteur(utilisateur1.getCompte());
        transaction3.setCompteBeneficiaire(utilisateur2.getCompte());
        transaction3.setTypeTransaction(typeTransaction);
        transaction3.setDate(new Date());
        transaction3.setLibelle(null);

        transactionList.add(transaction1);
        transactionList.add(transaction2);
        transactionList.add(transaction3);

        when(utilisateurService.getUserConnected(any())).thenReturn(utilisateur1);
        when(utilisateurService.getUser(anyString())).thenReturn(utilisateur2);
        when(transactionService.findAllTransactionOfUser(anyString())).thenReturn(transactionList);

        mockMvc.perform(post("/connection/add")
                        .param("email","alain.page@user.com"))
                .andExpect(model().attribute("message", "L'utilisateur Farqa Anissa à été ajouté à vos contacts"))
                .andExpect(view().name("transfer"))
                .andExpect(status().isOk());
    }

    @DisplayName("2°) Tentative d'ajouter un  utilisateur inexistant")
    @WithMockUser(username = "alain.page@user.com", roles = "USER", password ="123456789")
    @Test
    @Order(2)
    void testPostNewConnectionWithNoExistentUser() throws Exception {
        when(utilisateurService.getPasswordEncoder()).thenReturn(new BCryptPasswordEncoder());

        Role roleUser = new Role(1L, "USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleUser);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(7L);
        utilisateur1.setNom("Page");
        utilisateur1.setPrenom("alain");
        utilisateur1.setEmail("alain.page@user.com");
        utilisateur1.setRoles(roleList);
        utilisateur1.setPassword(utilisateurService.getPasswordEncoder().encode("123456789"));
        utilisateur1.setDateInscription(new Date());
        utilisateur1.setDateNaissance(new Date());

        Compte compte1 = new Compte(null, BigDecimal.valueOf(305L), utilisateur1);
        utilisateur1.setCompte(compte1);

        when(utilisateurService.getUserConnected(any())).thenReturn(utilisateur1);
        when(utilisateurService.getUser(anyString())).thenReturn(null);
        when(transactionService.findAllTransactionOfUser(anyString())).thenReturn(null);

        mockMvc.perform(post("/connection/add")
                        .param("email","marie.patricy@user.com"))
                .andExpect(model().attribute("message", "Aucun utilisateur n'a été retrouvé avec l'adresse email fourni"))
                .andExpect(view().name("transfer"))
                .andExpect(status().isOk());
    }

    @DisplayName("3°) Tentative d'ajouter un  utilisateur existant sans utiilisateur connecté")
    @WithMockUser(username = "alain.page@user.com", roles = "USER", password ="123456789")
    @Test
    @Order(3)
    void testPostNewConnectionWithExistentUserAndWithoutUserLogged() throws Exception {
        when(utilisateurService.getPasswordEncoder()).thenReturn(new BCryptPasswordEncoder());

        Role roleUser = new Role(1L, "USER");
        List<Role> roleList = new ArrayList<>();
        roleList.add(roleUser);

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(17L);
        utilisateur1.setNom("Durant");
        utilisateur1.setPrenom("Melusine");
        utilisateur1.setEmail("melusine.durant@user.com");
        utilisateur1.setRoles(roleList);
        utilisateur1.setPassword(utilisateurService.getPasswordEncoder().encode("123456789"));
        utilisateur1.setDateInscription(new Date());
        utilisateur1.setDateNaissance(new Date());

        Compte compte1 = new Compte(null, BigDecimal.valueOf(305L), utilisateur1);
        utilisateur1.setCompte(compte1);

        when(utilisateurService.getUserConnected(any())).thenReturn(null);
        when(utilisateurService.getUser(anyString())).thenReturn(utilisateur1);

        when(transactionService.findAllTransactionOfUser(anyString())).thenReturn(null);
        mockMvc.perform(post("/connection/add")
                        .param("email","melusine.durant@user.com"))
                .andExpect(model().attribute("message", "Une erreur est survenue lors de l'ajout de ma nouvelle connetion..."))
                .andExpect(view().name("login"))
                .andExpect(status().isOk());
    }
}
