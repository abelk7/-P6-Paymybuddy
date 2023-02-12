package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final IUtilisateurService utilisateurService;

    @GetMapping("/")
    public String getIndexPage(Model model) {
        return "home";
    }

    @GetMapping("/home")
    @ResponseStatus(code = HttpStatus.OK)
    public String getHomePage(Model model, Authentication authentication) {
        UserDetails userDetails = null;

        if (authentication != null)
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
}
