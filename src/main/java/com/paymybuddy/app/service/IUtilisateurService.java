package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Utilisateur;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface IUtilisateurService {
    Utilisateur getUserConnected(Authentication authentication);
    Utilisateur getUser(String email);
    Utilisateur saveUser(Utilisateur utilisateur);
    Utilisateur save(Utilisateur utilisateur);
    PasswordEncoder getPasswordEncoder();
}
