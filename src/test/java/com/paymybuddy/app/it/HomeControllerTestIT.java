package com.paymybuddy.app.it;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomeControllerTestIT {
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

    @DisplayName("Tentative d'acceder à la page racine par defaut")
    @Order(1)
    @Test
    void testGetIndexPage() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'acceder à la page home sans s'être connecter")
    @Order(2)
    @Test
    void testGetIndexHomePageWithoutLogged() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(model().attributeDoesNotExist("utilisateurCourant"))
                .andExpect(status().isOk());
    }

    @DisplayName("Tentative d'acceder à la page home avec un compte utilisateur connecté")
    @WithMockUser(username = "admin@admin.com")
    @Order(3)
    @Test
    void testGetIndexHomePageWithLoggedUser() throws Exception {
        mockMvc.perform(get("/home"))
                .andExpect(model().attributeExists("utilisateurCourant"))
                .andExpect(status().isOk());
    }
}
