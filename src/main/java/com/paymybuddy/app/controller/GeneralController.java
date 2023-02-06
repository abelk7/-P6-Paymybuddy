package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

    @GetMapping("/")
    public String getIndexPage(Model model){
        return "home";
    }
    @GetMapping("/home")
    public String getHomePage(Model model, Authentication authentication){

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if(userDetails != null){
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
           if(utilisateur != null){
               utilisateur.setPassword(null);
               model.addAttribute("utilisateurCourant", utilisateur);
           }
        }
        return "home";
    }

    @GetMapping("/login")
    public String getLoginPage(Model model){
        return "login";
    }

    @GetMapping("/logout")
    public String logoutPage(Model model){
        return "login";
    }


    @GetMapping("/register")
    public String getRegisterPage(Model model){
        return "register";
    }

}
