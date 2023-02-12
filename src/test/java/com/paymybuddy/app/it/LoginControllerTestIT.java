package com.paymybuddy.app.it;

import com.paymybuddy.app.service.IUtilisateurService;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LoginControllerTestIT {
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

    @DisplayName("Tentative d'acceder à la page login")
    @Order(1)
    @Test
    void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk());
    }

    @DisplayName("Connexion avec un utilisateur inscrit")
    @Order(2)
    @Test
    void testLogin() throws Exception {
        RequestBuilder requestBuilder = formLogin().user("user@user.com").password("123456789");
        mockMvc.perform(requestBuilder)
                .andExpect(redirectedUrl("/home"))
                .andExpect(status().isFound());

    }

    @DisplayName("Connexion avec un compte qui n'existe pas en bdd")
    @Order(3)
    @Test/*(expected = UsernameNotFoundException.class)*/
    void testLoginWithUnexistUser() {
        RequestBuilder requestBuilder = formLogin().user("asley@user.com").password("123456789");
        // TODO : TEST CONNNEXION, UsernameNotFoundException not Thrown
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            mockMvc.perform(requestBuilder)
                    .andExpect(redirectedUrl("/login?error"));
        });

//                .andExpect(result -> assertTrue(result.getResolvedException() instanceof UsernameNotFoundException));
    }

    @DisplayName("Deconnexion avec un utilisateur inscrit")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(4)
    @Test
    void testLogout() throws Exception {
        mockMvc.perform(post("/logout"))
                .andExpect(redirectedUrl("/login"))
                .andExpect(status().isFound());
    }
}
