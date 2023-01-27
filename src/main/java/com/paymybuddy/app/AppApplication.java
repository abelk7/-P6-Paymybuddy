package com.paymybuddy.app;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
@RequiredArgsConstructor
public class AppApplication {
    static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);
    public static void main(String[] args) {
        final UtilisateurRepository utilisateurRepository;
        SpringApplication.run(AppApplication.class, args);
    }
    @Bean
    public CommandLineRunner demo(UtilisateurRepository utilisateurRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        return (args) -> {
            Role roleUser = new Role(1L, "USER");
            Role roleAdmin = new Role(2L, "ADMIN");

            roleRepository.save(roleUser);
            roleRepository.save(roleAdmin);

            List<Role> roleListUser = new ArrayList<>();
            List<Role> roleListAdmin = new ArrayList<>();

            roleListUser.add(roleRepository.findById(1L).get());
            roleListAdmin.addAll(roleRepository.findAll());

            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.setNom("userNom");
            utilisateur1.setPrenom("userPrenom");
            utilisateur1.setEmail("user@user.com");
            utilisateur1.setRoles(roleListUser);
            utilisateur1.setPassword(passwordEncoder.encode("123456789"));
            utilisateur1.setDateInscription(new Date());
            utilisateur1.setDateNaissance(new Date());

            Utilisateur utilisateur2 = new Utilisateur();
            utilisateur2.setNom("adminNom");
            utilisateur2.setPrenom("adminPrenom");
            utilisateur2.setEmail("admin@admin.com");
            utilisateur2.setRoles(roleListAdmin);
            utilisateur2.setPassword(passwordEncoder.encode("123456789"));
            utilisateur2.setDateInscription(new Date());
            utilisateur2.setDateNaissance(new Date());

            List<String> roleUtilisateur1 = new ArrayList<>();
            utilisateur1.getRoles().forEach( role ->
                    roleUtilisateur1.add(role.getLibelle())
            );

            List<String> roleUtilisateur2 = new ArrayList<>();
            utilisateur2.getRoles().forEach( role ->
                    roleUtilisateur2.add(role.getLibelle())
            );

            LOGGER.info("Generate utilisateur1 ({}) with role{} pass: {}",
                    utilisateur1.getEmail(), roleUtilisateur1, "123456789");
            utilisateurRepository.save(utilisateur1);
            LOGGER.info("Generate utilisateur2 ({}) with role{} pass: {}",
                    utilisateur2.getEmail(), roleUtilisateur2, "123456789");
            utilisateurRepository.save(utilisateur2);
        };
    }

}
