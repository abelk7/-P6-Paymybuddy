package com.paymybuddy.app.it;

import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.Before;
//import org.junit.Test;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.OrderWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProfileControllerTestIT {
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private IUtilisateurService utilisateurService;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Tentative d'acceder à la page profile")
    @Order(1)
    @Test
    void testGetProfilePage() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(redirectedUrl("http://localhost/login"))
                .andExpect(status().isFound());
    }

    @DisplayName("Tentative d'acceder à la page profile avec un utilisateur connecté")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(2)
    @Test
    void testGetProfileWithAuthentificatedUser() throws Exception {
        mockMvc.perform(get("/profile"))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attributeExists("modifier"))
                .andExpect(model().attributeExists("modifierPass"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeDoesNotExist("message"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'acceder au endpoint /profile/modifier sans utilisateur connecté")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(3)
    @Test
    void testGetModifyProfilWithAuthentificatedUser() throws Exception {
        mockMvc.perform(get("/profile/modifier"))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attributeExists("modifier"))
                .andExpect(model().attributeExists("modifierPass"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeDoesNotExist("message"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'acceder au endpoint /profile/modifier/password (utilisateur connecté)")
    @WithMockUser(username = "user2@user.com", roles = "USER", password = "123456789")
    @Order(4)
    @Test
    void testGetModifyProfilPasswordWithAuthentificatedUser() throws Exception {
        mockMvc.perform(get("/profile/modifier/password"))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attributeExists("modifier"))
                .andExpect(model().attributeExists("modifierPass"))
                .andExpect(model().attributeExists("success"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attributeDoesNotExist("message"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative de modifier password utilisateur avec fausse confirmaiton")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(5)
    @Test
    void testModifyPasswordDifferentWithRepeatPasswordUser() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("abcdefgh");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("modifier", true))
                .andExpect(model().attribute("modifierPass", true))
                .andExpect(model().attribute("success", false))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attribute("message", "Les mots de passe saisie ne sont pas identique"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative de modifier password utilisateur avec taille incorrect")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(6)
    @Test
    void testModifyPassworddUserWithInvalidLength() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("abcd");
        utilisateurDTO.setPasswordRepeat("abcd");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("modifier", true))
                .andExpect(model().attribute("modifierPass", true))
                .andExpect(model().attribute("success", false))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attribute("message", "Le mot de passe doit être supérieur ou égale à 8 caractères"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative de modifier email utilisateur avec email incorrect")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(7)
    @Test
    void testModifyEmailUserWithInvalidEmail() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("useruser.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1997, 01, 02);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("modifier", true))
                .andExpect(model().attribute("modifierPass", false))
                .andExpect(model().attribute("success", false))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attribute("message", "L'addresse email n'est pas valide"))
                .andExpect(status().isOk());
    }


    @DisplayName("Tentative de modifier utilisateur avec un âge utilisateur mineur")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(8)
    @Test
    void testModifyDateNaissanceUserWithValidInformation() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user123@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(2010, 01, 02);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("modifier", true))
                .andExpect(model().attribute("modifierPass", false))
                .andExpect(model().attribute("success", false))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attribute("message", "Vous devez être majeur pour vous inscrire"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative de modifier un utilisateur inexistant")
    @WithMockUser(username = "user123456@user.com", roles = "USER", password = "123456789")
    @Order(9)
    @Test
    void testModifyFakeUserWithValidInformation() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user123@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(2000, 01, 02);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("modifier", true))
                .andExpect(model().attribute("modifierPass", false))
                .andExpect(model().attribute("success", false))
                .andExpect(model().attribute("error", true))
                .andExpect(model().attribute("message", "Aucun données concernant l'utilisateur saisie"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative de modifier un utilisateur avec information Correct sans changement password")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(10)
    @Test
    void testModifyUserWithValidInformationWithoutChangePassWord() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenomTest");
        utilisateurDTO.setNom("userNomTest");
        utilisateurDTO.setEmail("user1234@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 01, 02);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attributeDoesNotExist("modifier"))
                .andExpect(model().attributeDoesNotExist("modifierPass"))
                .andExpect(model().attributeDoesNotExist("success"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(model().attributeDoesNotExist("message"))
                .andExpect(redirectedUrl("/logout"))
                .andExpect(status().isFound());
    }

    @DisplayName("Tentative de modifier un utilisateur avec information Correct et changement de password")
    @WithMockUser(username = "user2@user.com", roles = "USER", password = "123456789")
    @Order(11)
    @Test
    void testModifyUserWithValidInformationWithPassWordChange() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenomTest");
        utilisateurDTO.setNom("userNomTest");
        utilisateurDTO.setEmail("user1234@user.com");
        utilisateurDTO.setPassword("123456789123456789");
        utilisateurDTO.setPasswordRepeat("123456789123456789");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 01, 02);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));
        mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attributeDoesNotExist("modifier"))
                .andExpect(model().attributeDoesNotExist("modifierPass"))
                .andExpect(model().attributeDoesNotExist("success"))
                .andExpect(model().attributeDoesNotExist("error"))
                .andExpect(redirectedUrl("/logout"))
                .andExpect(status().isFound());
    }
}
