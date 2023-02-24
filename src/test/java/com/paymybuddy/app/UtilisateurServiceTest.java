package com.paymybuddy.app;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.UtilisateurRepository;
import com.paymybuddy.app.service.IUtilisateurService;
import com.paymybuddy.app.service.impl.UtilisateurService;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UtilisateurServiceTest {
    private IUtilisateurService utilisateurService;
    private PasswordEncoder passwordEncoder;
    @Mock
    private UtilisateurRepository utilisateurRepository;

    @BeforeEach
    private void setup() {
        passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        utilisateurService = new UtilisateurService(utilisateurRepository, passwordEncoder);
    }

    @DisplayName(value = "1°) Recherche  d'un utilisateur")
    @Order(1)
    @Test
    void testGetUser() {

        Utilisateur utilisateur1 = new Utilisateur();
        utilisateur1.setId(1L);
        utilisateur1.setNom("userA1TestNom");
        utilisateur1.setPrenom("userA1TestPrenom");
        utilisateur1.setEmail("usera1@user.com");
        ZoneId defaultZoneId = ZoneId.systemDefault();
        LocalDate dateNaissance = LocalDate.of(1999, 12, 27);
        LocalDate dateInscription = LocalDate.now();
        utilisateur1.setDateInscription(Date.from(dateInscription.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur1.setDateNaissance(Date.from(dateNaissance.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurRepository.findByEmail(anyString())).thenReturn(utilisateur1);

        Utilisateur u = utilisateurService.getUser("usera1@user.com");

        assertThat(u).isNotNull();
        assertThat(u.getId()).isEqualTo(1L);
        assertThat(u.getEmail()).isEqualTo("usera1@user.com");
    }

    @DisplayName(value = "2°) Enregistrement d'un nouveau utilisateur")
    @Order(2)
    @Test
    void testSaveUser() {

        ZoneId defaultZoneId = ZoneId.systemDefault();
        Utilisateur utilisateur2 = new Utilisateur();
        utilisateur2.setId(2L);
        utilisateur2.setNom("userA2TestNom");
        utilisateur2.setPrenom("userA2TestPrenom");
        utilisateur2.setEmail("usera2@user.com");
        utilisateur2.setPassword(passwordEncoder.encode("123456789"));
        LocalDate dateNaissance3 = LocalDate.of(1998, 02, 25);
        LocalDate dateInscription3 = LocalDate.now();
        utilisateur2.setDateInscription(Date.from(dateInscription3.atStartOfDay(defaultZoneId).toInstant()));
        utilisateur2.setDateNaissance(Date.from(dateNaissance3.atStartOfDay(defaultZoneId).toInstant()));

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur2);

        Utilisateur u = utilisateurService.saveUser(utilisateur2);

        assertThat(u).isNotNull();
        assertThat(u.getId()).isEqualTo(2L);
        assertThat(u.getEmail()).isEqualTo("usera2@user.com");

    }

    @DisplayName(value = "3°) Modification d'information d'un utilisateur")
    @Order(3)
    @Test
    void testSave() {
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

        when(utilisateurRepository.save(any(Utilisateur.class))).thenReturn(utilisateur3);

        Utilisateur u = utilisateurService.save(utilisateur3);

        assertThat(u).isNotNull();
        assertThat(u.getId()).isEqualTo(3L);
        assertThat(u.getEmail()).isEqualTo("usera3@user.com");
    }
}
