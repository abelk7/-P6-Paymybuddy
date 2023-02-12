package com.paymybuddy.app;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.repository.UtilisateurRepository;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootApplication
@RequiredArgsConstructor
public class AppApplication {
    static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo( RoleRepository roleRepository, IUtilisateurService utilisateurService) {
        return (args) -> {
            String pattern = "dd/mm/yyyy";
            TimeZone tz = TimeZone.getDefault(); //getting up local time zone
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+2"));
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            simpleDateFormat.setTimeZone(tz);

            Role roleUserCreate = new Role(1L, "USER");
            Role roleAdminCreate = new Role(2L, "ADMIN");

            roleRepository.save(roleUserCreate);
            roleRepository.save(roleAdminCreate);

            List<Role> roleUser = new ArrayList<>();
            List<Role> roleAdmin = new ArrayList<>();

            roleUser.add(roleRepository.getRoleUser());
            roleAdmin.add(roleRepository.getRoleAdmin());

            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.setNom("userNom");
            utilisateur1.setPrenom("userPrenom");
            utilisateur1.setEmail("user@user.com");
            utilisateur1.setRoles(roleUser);
            utilisateur1.setPassword("123456789");
            utilisateur1.setDateInscription(new Date());
            utilisateur1.setDateNaissance(new Date());

            Utilisateur utilisateur2 = new Utilisateur();
            utilisateur2.setNom("user2Nom");
            utilisateur2.setPrenom("user2Prenom");
            utilisateur2.setEmail("user2@user.com");
            utilisateur2.setRoles(roleUser);
            utilisateur2.setPassword("123456789");
            utilisateur2.setDateInscription(new Date());
            utilisateur2.setDateNaissance(new Date());

            Utilisateur utilisateur3 = new Utilisateur();
            utilisateur3.setNom("adminNom");
            utilisateur3.setPrenom("adminPrenom");
            utilisateur3.setEmail("admin@admin.com");
            utilisateur3.setRoles(roleAdmin);
            utilisateur3.setPassword("123456789");
            utilisateur3.setDateInscription(new Date());
            utilisateur3.setDateNaissance(new Date());

            List<String> roleUtilisateur1 = new ArrayList<>();
            utilisateur1.getRoles().forEach(role ->
                    roleUtilisateur1.add(role.getLibelle())
            );

            List<String> roleUtilisateur2 = new ArrayList<>();
            utilisateur1.getRoles().forEach(role ->
                    roleUtilisateur2.add(role.getLibelle())
            );

            List<String> roleUtilisateur3 = new ArrayList<>();
            utilisateur3.getRoles().forEach(role ->
                    roleUtilisateur3.add(role.getLibelle())
            );

            LOGGER.info("Generate utilisateur1 ({}) with role{} pass: {}",
                    utilisateur1.getEmail(), roleUtilisateur1, "123456789");
            utilisateurService.saveUser(utilisateur1);
            LOGGER.info("Generate utilisateur2 ({}) with role{} pass: {}",
                    utilisateur2.getEmail(), roleUtilisateur2, "123456789");
            utilisateurService.saveUser(utilisateur2);
            LOGGER.info("Generate roleUtilisateur3 ({}) with role{} pass: {}",
                    utilisateur3.getEmail(), roleUtilisateur3, "123456789");
            utilisateurService.saveUser(utilisateur3);
        };
    }

}
