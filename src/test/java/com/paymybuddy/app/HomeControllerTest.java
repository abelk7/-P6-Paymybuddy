//package com.paymybuddy.app;
//
//import com.paymybuddy.app.controller.HomeController;
//import com.paymybuddy.app.model.Compte;
//import com.paymybuddy.app.model.Role;
//import com.paymybuddy.app.model.Utilisateur;
//import com.paymybuddy.app.security.SecurityConfig;
//import com.paymybuddy.app.service.IUtilisateurService;
//import lombok.RequiredArgsConstructor;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Order;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.Import;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.BootstrapWith;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Date;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
////@Import(SecurityConfig.class)
////@WebMvcTest(HomeController.class)
//@RequiredArgsConstructor
//public class HomeControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//    //private final  WebApplicationContext context;
//
////    @BeforeEach
////    public void setup() {
////        mockMvc = MockMvcBuilders
////                .webAppContextSetup(context)
////                .apply(springSecurity())
////                .build();
////    }
////
////    @Autowired
////    PasswordEncoder passwordEncoder;
////
////    @MockBean
////    private IUtilisateurService utilisateurService;
//
////    @DisplayName("1°) Tentative d'acceder à la page racine par defaut")
////    @Test
////    @Order(1)
////    void testGetLoginPage() throws Exception {
////
////        this.mockMvc
////                .perform(get("/"))
////                .andExpect(status().isOk())
////                .andExpect(view().name("home"));
////
////    }
////
////    @DisplayName("2°) Tentative d'acceder à la page home sans s'être connecter")
////    @Order(2)
////    @Test
////    void testGetIndexHomePageWithoutLogged() throws Exception {
////
////        mockMvc.perform(get("/home"))
////                .andExpect(model().attributeDoesNotExist("utilisateurCourant"))
////                .andExpect(view().name("home"))
////                .andExpect(status().isOk());
////    }
//
////    @DisplayName("3°) Tentative d'acceder à la page home user connecter")
////    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
////    @Order(3)
////    @Test
////    void testGetIndexHomePageWithoutLoginUser() throws Exception {
////
////        Role roleUser = new Role(1L, "USER");
////        List<Role> roleList = new ArrayList<>();
////        roleList.add(roleUser);
////
////        Utilisateur utilisateur1 = new Utilisateur();
////        utilisateur1.setId(7L);
////        utilisateur1.setNom("Garcia");
////        utilisateur1.setPrenom("Stephane");
////        utilisateur1.setEmail("stephane.g@user.com");
////        utilisateur1.setRoles(roleList);
////        utilisateur1.setPassword(passwordEncoder.encode("123456789"));
////        utilisateur1.setDateInscription(new Date());
////        utilisateur1.setDateNaissance(new Date());
////        Compte compte1 = new Compte(null, BigDecimal.valueOf(305L), utilisateur1);
////        utilisateur1.setCompte(compte1);
////
////        when(utilisateurService.getUser(anyString())).thenReturn(utilisateur1);
////        mockMvc.perform(get("/home"))
////                .andExpect(model().attributeExists("utilisateurCourant"))
////                .andExpect(view().name("home"))
////                .andExpect(status().isOk());
////    }
//}
