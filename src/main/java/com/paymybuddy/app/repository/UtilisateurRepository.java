package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);

//     @Query(value = "INSERT INTO utilisateurs (id, date_inscription, " +
//             "date_naissance, email, nom, password, prenom) values " +
//             "(null, : dateInscription, : dateNaissance, : email, : nom, : password, : prenom",nativeQuery = true)
//    Utilisateur save(@Param("dateInscription") Date dateInscription,
//                     @Param("dateNaissance") Date  dateNaissance,
//                     @Param("email") String email,
//                     @Param("nom") String nom,
//                     @Param("password") String password,
//                     @Param("prenom") String prenom);
}
