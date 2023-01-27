package com.paymybuddy.app.repository;

import com.paymybuddy.app.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    Utilisateur findByEmail(String email);
}
