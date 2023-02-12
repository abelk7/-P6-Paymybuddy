package com.paymybuddy.app.service;

import com.paymybuddy.app.model.Utilisateur;

public interface IUtilisateurService {
    Utilisateur getUser(String email);
    Utilisateur saveUser(Utilisateur utilisateur);
    Utilisateur save(Utilisateur utilisateur);
}
