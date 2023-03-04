package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.UtilisateurRepository;
import com.paymybuddy.app.service.IUtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Transactional
@Service
public class UtilisateurService implements IUtilisateurService {
    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurService.class);
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Override
    public Utilisateur getUserConnected(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (userDetails != null) {
            return utilisateurRepository.findByEmail(userDetails.getUsername());
        }
        return null;
    }

    @Override
    public Utilisateur getUser(String email) {
        LOG.info("Recherche de l'utilisateur {}  dans la base de donnée", email);
        return utilisateurRepository.findByEmail(email);
    }

    @Override
    public Utilisateur saveUser(Utilisateur utilisateur) {
        LOG.info("Enregistrement d'un nouveau utilisateur {} {} dans la base de donnée", utilisateur.getPrenom(), utilisateur.getNom());
        utilisateur.setPassword(getPasswordEncoder().encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        LOG.info("Modification de l'utilisateur {} {} dans la base de donnée", utilisateur.getPrenom(), utilisateur.getNom());
        return utilisateurRepository.save(utilisateur);
    }



    @Override
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
