package com.paymybuddy.app.it;

import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import java.util.Date;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RegisterControllerTestIT {
    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Tentative d'acceder à la page register")
    @Order(1)
    @Test
    void testGetRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec informations correct")
    @Order(2)
    @Test
    void testRegisterUserWithValidInformation() throws Exception {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Alain");
        utilisateurDTO.setNom("Fouquet");
        utilisateurDTO.setEmail("f.alain@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(status().isCreated());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ prenom incorrect")
    @Order(3)
    @Test
    void testRegisterUserWithInvalidPrenom() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("");
        utilisateurDTO.setNom("Patrick");
        utilisateurDTO.setEmail("p.marc@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "Le prenom  n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ nom incorrect")
    @Order(4)
    @Test
    void testRegisterUserWithInvalidNom() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Marc");
        utilisateurDTO.setNom("");
        utilisateurDTO.setEmail("p.marc@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "Le nom  n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ email incorrect")
    @Order(5)
    @Test
    void testRegisterUserWithInvalidEmail() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Marc");
        utilisateurDTO.setNom("Patrick");
        utilisateurDTO.setEmail("p.marc");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "L'addresse email n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ email incorrect")
    @Order(6)
    @Test
    void testRegisterUserWithInvalidPassword() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Marc");
        utilisateurDTO.setNom("Patrick");
        utilisateurDTO.setEmail("p.marc@user.com");
        utilisateurDTO.setPassword("");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "Le mot de passe n'est pas valide"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ email incorrect")
    @Order(7)
    @Test
    void testRegisterUserWithInvalidPasswordLength() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Marc");
        utilisateurDTO.setNom("Patrick");
        utilisateurDTO.setEmail("p.marc@user.com");
        utilisateurDTO.setPassword("1234");
        utilisateurDTO.setPasswordRepeat("1234");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "Votre mot de passe doit comporter plus de 8 caractères"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ email incorrect")
    @Order(8)
    @Test
    void testRegisterUserWithInvalidRepeatPassword() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Marc");
        utilisateurDTO.setNom("Patrick");
        utilisateurDTO.setEmail("p.marc@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("azerty");
        utilisateurDTO.setDateNaissance(new Date());
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "Les mots de passe ne sont pas identiques"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec champ email incorrect")
    @Order(9)
    @Test
    void testRegisterUserWithInvalidDateNaissance() throws Exception {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setPrenom("Marc");
        utilisateurDTO.setNom("Patrick");
        utilisateurDTO.setEmail("p.marc@user.com");
        utilisateurDTO.setPassword("123456789");
        utilisateurDTO.setPasswordRepeat("123456789");
        utilisateurDTO.setDateNaissance(null);
        mockMvc.perform(post("/register/user").flashAttr("utilisateur", utilisateurDTO))
                .andExpect(model().attribute("message", "La date de naissance n'est pas valide"))
                .andExpect(status().isOk());
    }
}
