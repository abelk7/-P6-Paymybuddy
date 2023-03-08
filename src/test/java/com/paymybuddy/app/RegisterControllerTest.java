package com.paymybuddy.app;

import com.paymybuddy.app.controller.RegisterController;
import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.security.SecurityConfig;
import com.paymybuddy.app.service.IRoleService;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(SecurityConfig.class)
@WebMvcTest(RegisterController.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUtilisateurService utilisateurService;
    @MockBean
    private IRoleService roleService;

    @DisplayName("1°) Tentative d'acceder à la page register")
    @Test
    @Order(1)
    void testGetRegisterPage() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        this.mockMvc
                .perform(get("/register").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(view().name("register"))
                .andExpect(status().isOk());
    }

    @DisplayName("2°) Tentative de s'inscrire avec un email déjà utilisé")
    @Order(2)
    @Test
    void testRegisterWithAnUsedEmail() throws Exception {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(3L);
        utilisateur.setNom("userA3TestNom");
        utilisateur.setPrenom("userA3TestPrenom");
        utilisateur.setEmail("user@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 12, 27);
        LocalDate dateInscription = LocalDate.now();
        utilisateur.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurService.getUser(anyString())).thenReturn(utilisateur);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Un utilisateur existe déjà avec l'addresse email fourni"))
                .andExpect(status().isOk());
    }

    @DisplayName("3°) Tentative de s'inscrire avec un prenom invalide")
    @Order(3)
    @Test
    void testRegisterWithInvalidFirstName() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Le prenom  n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("4°) Tentative de s'inscrire avec un nom invalide")
    @Order(4)
    @Test
    void testRegisterWithInvalidLastName() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Le nom  n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("4°) Tentative de s'inscrire avec un email invalide")
    @Order(4)
    @Test
    void testRegisterWithInvalidEmail() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("us123456");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "L'adresse email n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("5°) Tentative de s'inscrire avec un mot de passe invalide")
    @Order(5)
    @Test
    void testRegisterWithInvalidPassword() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();


        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword(null);
        utilisateurDTO.setPasswordRepeat(null);
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Le mot de passe n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("6°) Tentative de s'inscrire avec un mot de passe inférieur à 8 caractères")
    @Order(6)
    @Test
    void testRegisterWithPasswordLessThanEightCharacters() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("1234567");
        utilisateurDTO.setPasswordRepeat("1234567");
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Votre mot de passe doit comporter plus de 8 caractères"))
                .andExpect(status().isOk());
    }

    @DisplayName("7°) Tentative de s'inscrire avec un mot de passe différent de la confirmation")
    @Order(7)
    @Test
    void testRegisterWithPasswordDifferentWithConfirmation() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("useruser1@user.com");
        utilisateurDTO.setPassword("12345678");
        utilisateurDTO.setPasswordRepeat("12345678911");
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Les mots de passe ne sont pas identiques"))
                .andExpect(status().isOk());
    }

    @DisplayName("8°) Tentative de s'inscrire avec une date de naissance invalide")
    @Order(8)
    @Test
    void testRegisterWithInvalidBirthday() throws Exception {
        when(utilisateurService.getUser(anyString())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("useruser1@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(null);

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "La date de naissance n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("9°) Tentative de s'inscrire avec une erreur lors de l'enregistrement")
    @Order(9)
    @Test
    void testRegisterWithErrorWhenSaveUser() throws Exception {
        ZoneId defaultZoneId = ZoneId.systemDefault();

        when(utilisateurService.getUser(anyString())).thenReturn(null);
        when(utilisateurService.save(any())).thenReturn(null);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("register"))
                .andExpect(model().attribute("message", "Une erreur est survenue lors de l'enregistrement du nouveau utilisateur"))
                .andExpect(status().isOk());
    }

    @DisplayName("10°) Tentative de s'inscrire avec des informations correct")
    @Order(10)
    @Test
    void testRegisterWithCorrectInformations() throws Exception {
        when(utilisateurService.getUser(anyString())).thenReturn(null);

        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(3L);
        utilisateur.setNom("userPrenom");
        utilisateur.setPrenom("userNom");
        utilisateur.setEmail("user@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 12, 27);
        LocalDate dateInscription = LocalDate.now();
        utilisateur.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurService.saveUser(any())).thenReturn(utilisateur);

        List<Role> roleList = new ArrayList<>();

        Role role1 = new Role(1L, "USER");
        Role role2 = new Role(2L, "ADMIN");
        roleList.add(role1);
        roleList.add(role2);

        when(roleService.getAllRoles()).thenReturn(roleList);

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("userPrenom");
        utilisateurDTO.setNom("userNom");
        utilisateurDTO.setEmail("user@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        LocalDate dateNaissance2 = LocalDate.of(1999, 12, 27);
        utilisateurDTO.setDateNaissance(Date.from(dateNaissance2.atStartOfDay(defaultZoneId).toInstant()));

        this.mockMvc
                .perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attributeExists("utilisateur"))
                .andExpect(view().name("login"))
                .andExpect(model().attribute("successInscription", true))
                .andExpect(status().isCreated());
    }

}
