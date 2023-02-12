package com.paymybuddy.app.it;

import com.paymybuddy.app.payload.UtilisateurDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Date;

import static org.junit.Assert.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.logout;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.param;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GeneralControllerTestIT {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mockMvc;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("Tentative d'acceder à la page racine par defaut")
    @Test
    public void testGetIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'acceder à la page home sans s'être connecter")
    @Test
    public void testGetIndexHomePageWithoutLogged() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(model().attributeDoesNotExist("utilisateurCourant"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'acceder à la page home avec un compte utilisateur connecté")
    @WithMockUser(username = "user@user.com")
    @Test
    public void testGetIndexHomePageWithLoggedUser() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(status().isOk());
    }

    /******Test Login*********/

    @DisplayName("Tentative d'acceder à la page login")
    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @DisplayName("Connexion avec un utilisateur inscrit")
    @Test
    public void testLogin() throws Exception {
        RequestBuilder requestBuilder = formLogin().user("user@user.com").password("123456789");
        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().isFound());

    }

    @DisplayName("Connexion avec un compte qui n'existe pas en bdd")
    @Test/*(expected = UsernameNotFoundException.class)*/
    public void testLoginWithUnexistUser() throws Exception, UsernameNotFoundException {
        RequestBuilder requestBuilder = formLogin().user("asley@user.com").password("123456789");
        // TODO : TEST CONNNEXION, UsernameNotFoundException not Thrown
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            mockMvc.perform(requestBuilder)
                    .andExpect(redirectedUrl("/login?error"));
        });

//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException));
    }

    @DisplayName("Deconnexion avec un utilisateur inscrit")
    @WithMockUser(username = "user@user.com",roles = "USER", password = "123456789")
    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(status().isFound());
    }

    /******Test Register *********/

    @DisplayName("Tentative d'acceder à ma page register")
    @Test
    public void testGetRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'inscrire un utilisateur avec informations correct")
    @Test
    public void testRegisterUserWithValidInformation() throws Exception {
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
    @Test
    public void testRegisterUserWithInvalidPrenom() throws Exception {

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
    @Test
    public void testRegisterUserWithInvalidNom() throws Exception {

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
    @Test
    public void testRegisterUserWithInvalidEmail() throws Exception {

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
    @Test
    public void testRegisterUserWithInvalidPassword() throws Exception {

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
    @Test
    public void testRegisterUserWithInvalidPasswordLength() throws Exception {

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
    @Test
    public void testRegisterUserWithInvalidRepeatPassword() throws Exception {

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
    @Test
    public void testRegisterUserWithInvalidDateNaissance() throws Exception {

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
