package com.paymybuddy.app;

import com.paymybuddy.app.controller.ProfileController;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.security.SecurityConfig;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Import(SecurityConfig.class)
@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUtilisateurService utilisateurService;

    private ProfileController profileController;

    @DisplayName("1°) Tentative d'acceder à la page profile")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Test
    @Order(1)
    void testGetProfilePage() throws Exception {
        when(utilisateurService.getUserConnected(any())).thenReturn(null);

        this.mockMvc
                .perform(get("/profile"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("2°) Tentative d'acceder à la page de modification du profile")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Test
    @Order(2)
    void testgetProfileModifier() throws Exception {
        when(utilisateurService.getUserConnected(any())).thenReturn(null);

        this.mockMvc
                .perform(get("/profile/modifier"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("3°) Tentative de  modifier le mot de passe différent du confirm passe")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(3)
    @Test
    void testGetIndexHomePageWithoutLoginUser() throws Exception {

        Utilisateur utilisateur3 = new Utilisateur();
        utilisateur3.setId(3L);
        utilisateur3.setNom("userA3TestNom");
        utilisateur3.setPrenom("userA3TestPrenom");
        utilisateur3.setEmail("usera3@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 12, 27);
        LocalDate dateInscription = LocalDate.now();
        utilisateur3.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur3.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurService.getUserConnected(any())).thenReturn(utilisateur3);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("abcd12345");
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);

        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));


        this.mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("message", "Les mots de passe saisie ne sont pas identique"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("4°) Tentative de  modifier adresse email (incorrect)'")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(4)
    @Test
    void testModifyEmailWithWrongEmail() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("useuser.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());


        this.mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("message", "L'addresse email n'est pas valide"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("5°) Tentative de  modifier date naissance inférieur à 18 ans")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(5)
    @Test
    void testModifyAgeWith() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());


        this.mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("message", "Vous devez être majeur pour vous inscrire"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("6°) Tentative de  modifier user sans utilisateur connecté")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(6)
    @Test
    void testModifyUserInformationWhenNotLogged() throws Exception {
        when(utilisateurService.getUserConnected(any())).thenReturn(null);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        LocalDate dateNaissance = LocalDate.of(1995, 12, 27);
        ZoneId defaultZoneId = ZoneId.systemDefault();
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("message", "Aucun données concernant l'utilisateur saisie"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("7°) Tentative de  modifier information utilisateur correct")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(7)
    @Test
    void testModifyUserInformation() throws Exception {
        Utilisateur utilisateur3 = new Utilisateur();
        utilisateur3.setId(3L);
        utilisateur3.setNom("userA3TestNom");
        utilisateur3.setPrenom("userA3TestPrenom");
        utilisateur3.setEmail("usera3@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 12, 27);
        LocalDate dateInscription = LocalDate.now();
        utilisateur3.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur3.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurService.getUserConnected(any())).thenReturn(utilisateur3);
        when(utilisateurService.save(any())).thenReturn(utilisateur3);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(redirectedUrl("/logout"))
                .andExpect(status().isOk());
    }


    @DisplayName("8°) Tentative de  modifier information utilisateur correct sans trouvé utilisateur à modifier")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(8)
    @Test
    void testModifyUserWithCorrectInformationsWithoutFoundUserToModify() throws Exception {
        Utilisateur utilisateur3 = new Utilisateur();
        utilisateur3.setId(3L);
        utilisateur3.setNom("userA3TestNom");
        utilisateur3.setPrenom("userA3TestPrenom");
        utilisateur3.setEmail("usera3@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 12, 27);
        LocalDate dateInscription = LocalDate.now();
        utilisateur3.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur3.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurService.getUserConnected(any())).thenReturn(utilisateur3);
        when(utilisateurService.save(any())).thenReturn(null);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc.perform(post("/profile/modifier/user").flashAttr("utilisateurCourant", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(model().attribute("message", "Une erreur est survenu lors de la tentative d'enregistrement des données"))
                .andExpect(view().name("profile"))
                .andExpect(status().isOk());
    }

    @DisplayName("9°) Test calculate Age if user is majeur with majeur user")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(9)
    @Test
    void testPasswordConfirm_shouldReturnFalse() throws Exception {
        profileController = new ProfileController(utilisateurService);

        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate date = LocalDate.of(2022, 12, 27);

        boolean result = profileController.calculateAge(Date.from(date.atStartOfDay(defaultZoneId).toInstant()));

        assertThat(result).isFalse();
    }

    @DisplayName("10°) Test calculate Age if user is majeur with mineur user")
    @WithMockUser(username = "stephane.g@user.com", roles = "USER", password = "123456789")
    @Order(10)
    @Test
    void testPasswordConfirm_shouldReturnTrue() throws Exception {
        profileController = new ProfileController(utilisateurService);

        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate date = LocalDate.of(1997, 12, 27);

        boolean result = profileController.calculateAge(Date.from(date.atStartOfDay(defaultZoneId).toInstant()));

        assertThat(result).isTrue();
    }
}
