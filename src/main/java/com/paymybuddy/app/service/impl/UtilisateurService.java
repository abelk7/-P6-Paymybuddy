package com.paymybuddy.app.service.impl;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.repository.UtilisateurRepository;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Transactional
@Service("utilisateurService")
public class UtilisateurService implements IUtilisateurService {
    private static final Logger LOG = LoggerFactory.getLogger(UtilisateurService.class);
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Utilisateur getUser(String email) {
        LOG.info("Recherche de l'lutilisateur avec l'email : {}", email);
        return utilisateurRepository.findByEmail(email);
    }
    @Override
    public Utilisateur saveUser(Utilisateur utilisateur) {
        LOG.info("Enregistrement d'un nouveau utilisateur {} {} dans la base de donnée", utilisateur.getPrenom(), utilisateur.getNom());
        utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        return utilisateurRepository.save(utilisateur);
    }

    @Override
    public Utilisateur save(Utilisateur utilisateur) {
        LOG.info("Enregistrement de l'utilisateur {} {} dans la base de donnée", utilisateur.getPrenom(), utilisateur.getNom());
        return utilisateurRepository.save(utilisateur);
    }
}
