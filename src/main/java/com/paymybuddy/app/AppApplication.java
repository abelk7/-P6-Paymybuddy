package com.paymybuddy.app;

import com.paymybuddy.app.model.*;
import com.paymybuddy.app.repository.RoleRepository;
import com.paymybuddy.app.repository.UtilisateurRepository;
import com.paymybuddy.app.service.ITypeTransactionService;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@ComponentScan(basePackages = {"com.paymybuddy.app.*"})
@SpringBootApplication
@RequiredArgsConstructor
public class AppApplication {
    static final Logger LOGGER = LoggerFactory.getLogger(AppApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(RoleRepository roleRepository, IUtilisateurService utilisateurService, ITypeTransactionService typeTransactionService) {
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

            TypeTransaction typeTransaction = new TypeTransaction(null, "Virement");
            typeTransactionService.save(typeTransaction);

            Utilisateur utilisateur1 = new Utilisateur();
            utilisateur1.setNom("userNom");
            utilisateur1.setPrenom("userPrenom");
            utilisateur1.setEmail("user@user.com");
            utilisateur1.setRoles(roleUser);
            utilisateur1.setPassword("123456789");
            utilisateur1.setDateInscription(new Date());
            utilisateur1.setDateNaissance(new Date());
            Compte compte1 = new Compte(null, BigDecimal.valueOf(299L), utilisateur1);
            utilisateur1.setCompte(compte1);

            Utilisateur utilisateur2 = new Utilisateur();
            utilisateur2.setNom("user2Nom");
            utilisateur2.setPrenom("user2Prenom");
            utilisateur2.setEmail("user2@user.com");
            utilisateur2.setRoles(roleUser);
            utilisateur2.setPassword("123456789");
            utilisateur2.setDateInscription(new Date());
            utilisateur2.setDateNaissance(new Date());
            Compte compte2 = new Compte(null, BigDecimal.valueOf(60L), utilisateur2);
            utilisateur2.setCompte(compte2);


            Utilisateur utilisateur3 = new Utilisateur();
            utilisateur3.setNom("adminNom");
            utilisateur3.setPrenom("adminPrenom");
            utilisateur3.setEmail("admin@admin.com");
            utilisateur3.setRoles(roleAdmin);
            utilisateur3.setPassword("123456789");
            utilisateur3.setDateInscription(new Date());
            utilisateur3.setDateNaissance(new Date());
            Compte compte3 = new Compte(null, BigDecimal.ZERO, utilisateur3);
            utilisateur3.setCompte(compte3);

            Utilisateur utilisateur4 = new Utilisateur();
            utilisateur4.setNom("user4Nom");
            utilisateur4.setPrenom("user4Prenom");
            utilisateur4.setEmail("user4@user.com");
            utilisateur4.setRoles(roleUser);
            utilisateur4.setPassword("123456789");
            utilisateur4.setDateInscription(new Date());
            utilisateur4.setDateNaissance(new Date());
            Compte compte4 = new Compte(null, BigDecimal.ZERO, utilisateur4);
            utilisateur4.setCompte(compte4);

            Utilisateur utilisateur5 = new Utilisateur();
            utilisateur5.setNom("user5Nom");
            utilisateur5.setPrenom("user5Prenom");
            utilisateur5.setEmail("user5@user.com");
            utilisateur5.setRoles(roleUser);
            utilisateur5.setPassword("123456789");
            utilisateur5.setDateInscription(new Date());
            utilisateur5.setDateNaissance(new Date());
            Compte compte5 = new Compte(null, BigDecimal.ZERO, utilisateur5);
            utilisateur5.setCompte(compte5);

            Utilisateur utilisateur6 = new Utilisateur();
            utilisateur6.setNom("user6Nom");
            utilisateur6.setPrenom("user6Prenom");
            utilisateur6.setEmail("user6@user.com");
            utilisateur6.setRoles(roleUser);
            utilisateur6.setPassword("123456789");
            utilisateur6.setDateInscription(new Date());
            utilisateur6.setDateNaissance(new Date());
            Compte compte6 = new Compte(null, BigDecimal.ZERO, utilisateur6);
            utilisateur6.setCompte(compte6);


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

            List<String> roleUtilisateur4 = new ArrayList<>();
            utilisateur4.getRoles().forEach(role ->
                    roleUtilisateur4.add(role.getLibelle())
            );

            List<String> roleUtilisateur5 = new ArrayList<>();
            utilisateur5.getRoles().forEach(role ->
                    roleUtilisateur5.add(role.getLibelle())
            );

            List<String> roleUtilisateur6 = new ArrayList<>();
            utilisateur5.getRoles().forEach(role ->
                    roleUtilisateur6.add(role.getLibelle())
            );

            List<Connection> listConnUtilisateur1 = new ArrayList<>();
            List<Connection> listConnUtilisateur2 = new ArrayList<>();

            Connection connection12 = new Connection(null, utilisateur1, utilisateur2);
            Connection connection13 = new Connection(null, utilisateur1, utilisateur3);
            Connection connection14 = new Connection(null, utilisateur1, utilisateur4);
            listConnUtilisateur1.add(connection12);
            listConnUtilisateur1.add(connection13);
            listConnUtilisateur1.add(connection14);
            utilisateur1.setConnections(listConnUtilisateur1);

            Connection connection25 = new Connection(null, utilisateur2, utilisateur5);
            Connection connection26 = new Connection(null, utilisateur2, utilisateur6);
            listConnUtilisateur2.add(connection25);
            listConnUtilisateur2.add(connection26);
            utilisateur2.setConnections(listConnUtilisateur2);


            LOGGER.info("Generate utilisateur1 ({}) with role{} pass: {}",
                    utilisateur1.getEmail(), roleUtilisateur1, "123456789");
            utilisateurService.saveUser(utilisateur1);
            LOGGER.info("Generate utilisateur2 ({}) with role{} pass: {}",
                    utilisateur2.getEmail(), roleUtilisateur2, "123456789");
            utilisateurService.saveUser(utilisateur2);
            LOGGER.info("Generate roleUtilisateur3 ({}) with role{} pass: {}",
                    utilisateur3.getEmail(), roleUtilisateur3, "123456789");
            utilisateurService.saveUser(utilisateur4);
            LOGGER.info("Generate roleUtilisateur4 ({}) with role{} pass: {}",
                    utilisateur4.getEmail(), roleUtilisateur4, "123456789");
            utilisateurService.saveUser(utilisateur5);
            LOGGER.info("Generate roleUtilisateur5 ({}) with role{} pass: {}",
                    utilisateur5.getEmail(), roleUtilisateur5, "123456789");
            utilisateurService.saveUser(utilisateur6);
            LOGGER.info("Generate roleUtilisateur6 ({}) with role{} pass: {}",
                    utilisateur6.getEmail(), roleUtilisateur6, "123456789");
            utilisateurService.saveUser(utilisateur3);
        };
    }

}
