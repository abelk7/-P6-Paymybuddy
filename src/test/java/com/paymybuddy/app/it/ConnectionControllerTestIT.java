package com.paymybuddy.app.it;

import lombok.RequiredArgsConstructor;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(JUnitPlatform.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@RequiredArgsConstructor
public class ConnectionControllerTestIT {

    private final WebApplicationContext context;
    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @DisplayName("1°) Tentative ajouter une connetion d'un utilisateur existant en base")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(1)
    @Test
    void testAddConnectionWithExistingUser() throws Exception {

        mockMvc.perform(post("/connection/add")
                    .param("email", "admin@admin.com")
                )
                .andExpect(model().attribute("addContact",true))
                .andExpect(model().attribute("error",false))
                .andExpect(status().isOk());

    }

    @DisplayName("2°) Tentative ajouter une connetion d'un utilisateur inexistant en base")
    @WithMockUser(username = "user@user.com", roles = "USER", password = "123456789")
    @Order(2)
    @Test
    void testAddConnectionWithNonExistingUser() throws Exception {

        mockMvc.perform(post("/connection/add")
                        .param("email", "adminAERTY123@admin.com")
                )
                .andExpect(model().attribute("addContact",false))
                .andExpect(model().attribute("error",true))
                .andExpect(model().attribute("message", "Aucun utilisateur n'a été retrouvé avec l'adresse email fourni"))
                .andExpect(status().isOk());

    }
}
