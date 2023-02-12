package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Role;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.service.IRoleService;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import com.google.common.base.Strings;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * General controller for
 * - login
 * - register
 * - home
 * requests
 */
@Controller
@RequiredArgsConstructor
public class GeneralController {
    private final IUtilisateurService utilisateurService;
    private final IRoleService roleService;

    @GetMapping("/")
    public String getIndexPage(Model model) {
        return "home";
    }

    @GetMapping("/home")
    @ResponseStatus(code = HttpStatus.OK)
    public String getHomePage(Model model, Authentication authentication) {
        UserDetails userDetails = null;

        if(authentication != null)
            userDetails = (UserDetails) authentication.getPrincipal();

        if (userDetails != null) {
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
            if (utilisateur != null) {
                utilisateur.setPassword(null);
                model.addAttribute("utilisateurCourant", utilisateur);
            }
        }
        return "home";
    }

    @GetMapping("/login")
    @ResponseStatus(code = HttpStatus.OK)
    public String getLoginPage(Model model) {
        return "login";
    }

    @GetMapping("/logout")
    @ResponseStatus(code = HttpStatus.OK)
    public String logoutPage(Model model) {
        return "login";
    }


    @GetMapping("/register")
    @ResponseStatus(code = HttpStatus.OK)
    public String getRegisterPage(@ModelAttribute(value = "utilisateur") UtilisateurDTO utilisateur, Model model) {
        model.addAttribute("utilisateurCourant", utilisateur);
        return "register";
    }

    @RequestMapping(value = "/register/user", method = RequestMethod.POST)
    public String inscriptionUtilisateur(@ModelAttribute(value = "utilisateur") UtilisateurDTO utilisateur, Model model, HttpServletResponse response) {
        Utilisateur checkUser = utilisateurService.getUser(utilisateur.getEmail());
        List<Role> rolesList = roleService.getAllRoles();

        if (checkUser != null) {
            modelRegistrationError(model, "Un utilisateur existe déjà avec l'addresse email fourni", utilisateur);
            return "register";
        }
        if (utilisateur != null) {
            if (Strings.isNullOrEmpty(utilisateur.getPrenom())) {
                modelRegistrationError(model, "Le prenom  n'est pas valide", utilisateur);
                return "register";
            }
            if (Strings.isNullOrEmpty(utilisateur.getNom())) {
                modelRegistrationError(model, "Le nom  n'est pas valide", utilisateur);
                return "register";
            }
            if (Strings.isNullOrEmpty(utilisateur.getEmail()) || !utilisateur.getEmail().contains("@")) {
                modelRegistrationError(model, "L'addresse email n'est pas valide", utilisateur);
                return "register";
            }
            if (Strings.isNullOrEmpty(utilisateur.getPassword())) {
                modelRegistrationError(model, "Le mot de passe n'est pas valide", utilisateur);
                return "register";
            }
            if(!Strings.isNullOrEmpty(utilisateur.getPassword()) && utilisateur.getPassword().length() < 8) {
                modelRegistrationError(model, "Votre mot de passe doit comporter plus de 8 caractères", utilisateur);
                return "register";
            }
            if (!utilisateur.getPassword().equals(utilisateur.getPasswordRepeat())) {
                modelRegistrationError(model, "Les mots de passe ne sont pas identiques", utilisateur);
                return "register";
            }
            if (utilisateur.getDateNaissance() == null) {
                modelRegistrationError(model, "La date de naissance n'est pas valide", utilisateur);
                return "register";
            }

            List<Role> newUserRole = new ArrayList<>();

            for (Role role: rolesList) {
                if (role.getLibelle().equals("USER")){
                    newUserRole.add(role);
                    break;
                }
            }
            Utilisateur newUser = new Utilisateur(
                    null,
                    utilisateur.getNom(), utilisateur.getPrenom(),
                    utilisateur.getEmail(), utilisateur.getPassword(),
                    utilisateur.getDateNaissance(), new Date(), new ArrayList<>(), newUserRole
            );

            Utilisateur u = utilisateurService.saveUser(newUser);
            if (u == null) {
                modelRegistrationError(model, "Une erreur est survenue lors de l'enregistrement du nouveau utilisateur", utilisateur);
                return "register";
            }
            model.addAttribute("successInscription", true);
            response.setStatus(201);
            return "login";

        }
        return "";
    }

    private Model modelRegistrationError(Model model, String message, UtilisateurDTO utilisateur) {
        model.addAttribute("message", message);
        model.addAttribute("error", true);
        model.addAttribute("utilisateur", utilisateur);
        return model;
    }

}
