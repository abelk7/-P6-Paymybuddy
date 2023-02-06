package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class UtilisateurController {
    private final IUtilisateurService utilisateurService;
    @GetMapping("/profile")
    public String getProfilePage(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if(userDetails != null){
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());

            if(utilisateur != null){
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO(
                        utilisateur.getPrenom(), utilisateur.getNom(), utilisateur.getEmail(),
                        null,null,utilisateur.getDateNaissance()
                );

                model.addAttribute("utilisateurCourant", utilisateurDTO);
                model.addAttribute("modifier", false);
                model.addAttribute("modifierPass", false);
                model.addAttribute("success", false);
                model.addAttribute("error", false);
                model.addAttribute("message", "");
            }
        }
        return "profile";
    }

    @GetMapping("/profile/modifier")
    public String getProfileModifier(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if(userDetails != null){
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
            if(utilisateur != null){
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO(
                        utilisateur.getPrenom(), utilisateur.getNom(), utilisateur.getEmail(),
                        null,null,utilisateur.getDateNaissance()
                );
                model.addAttribute("utilisateurCourant", utilisateurDTO);
                model.addAttribute("modifier", true);
                model.addAttribute("modifierPass", false);                model.addAttribute("success", false);
                model.addAttribute("success", false);
                model.addAttribute("error", false);
                model.addAttribute("message", "");
            }
        }
        return "profile";
    }

    @GetMapping("/profile/modifier/password")
    public String getProfileModifierPassword(Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if(userDetails != null){
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
            if(utilisateur != null){
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO(
                        utilisateur.getPrenom(), utilisateur.getNom(), utilisateur.getEmail(),
                        null,null,utilisateur.getDateNaissance()
                );
                model.addAttribute("utilisateurCourant", utilisateurDTO);
                model.addAttribute("modifier", true);
                model.addAttribute("modifierPass", true);
                model.addAttribute("success", false);
                model.addAttribute("error", false);
                model.addAttribute("message", "");
            }
        }
        return "profile";
    }


    @RequestMapping(value = "/profile/modifier/user", method= RequestMethod.POST)
    public String postModifierUtilisateur(@ModelAttribute(value="utilisateurCourant") UtilisateurDTO utilisateur,Model model, Authentication authentication){
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        boolean changedPassword = false;

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO(
                utilisateur.getPrenom(), utilisateur.getNom(), utilisateur.getEmail(),
                null,null,utilisateur.getDateNaissance()
        );

        //TODO : Vérifier le changement de chaque champs indivielllement....
        if(utilisateur.getPasswordRepeat() != null){
            if(utilisateur.getPassword().length() >= 8){
                if(utilisateur.getPassword().equals(utilisateur.getPasswordRepeat())){
                    changedPassword = true;
                }else{
                    //Les mots de passe ne corresponds pas
                    model.addAttribute("utilisateurCourant", utilisateurDTO);
                    model.addAttribute("modifier", true);
                    model.addAttribute("modifierPass", true);
                    model.addAttribute("error", true);
                    model.addAttribute("success", false);
                    model.addAttribute("message", "Les mots de passe saisie ne sont pas identique");
                    return "profile";
                }

            }else {
                //Password inférieur à 8 caractère
                model.addAttribute("utilisateurCourant", utilisateurDTO);
                model.addAttribute("modifier", true);
                model.addAttribute("modifierPass", true);
                model.addAttribute("error", true);
                model.addAttribute("success", false);
                model.addAttribute("message", "Le mot de passe doit être supérieur ou égale à 8 caractères");
                return "profile";
            }

        }

        if(userDetails != null ){
            Utilisateur utilisateurRechercher = utilisateurService.getUser(userDetails.getUsername());
            if(utilisateur.getEmail() != null || !utilisateur.getEmail().equals("") && utilisateur.getEmail().contains("@")){

                if(utilisateurRechercher != null){
                    if(passwordConfirmer(utilisateur.getPassword(),utilisateur.getPasswordRepeat())){
                        Utilisateur utilisateurAEnregistrer = utilisateurService.getUser(userDetails.getUsername());
                        utilisateurAEnregistrer.setEmail(utilisateur.getEmail());
                        utilisateurAEnregistrer.setPassword(utilisateur.getPassword());
                        Utilisateur u = utilisateurService.saveUser(utilisateurAEnregistrer);

                         utilisateurDTO = new UtilisateurDTO(
                                utilisateur.getPrenom(), u.getNom(), u.getEmail(),
                                null,null,u.getDateNaissance()
                        );
                        model.addAttribute("utilisateurCourant", utilisateurDTO);
                        model.addAttribute("modifier", true);
                        model.addAttribute("modifierPass", false);
                        model.addAttribute("utilisateurEnregistrer", false);
                        model.addAttribute("error", false);
                        model.addAttribute("success", true);
                        model.addAttribute("message", "Les modifications ont été enregistrés");
                        return "profile";
                    }
                }
            }else {

                model.addAttribute("utilisateurCourant", utilisateurDTO);
                model.addAttribute("modifier", true);
                model.addAttribute("modifierPass", false);
                model.addAttribute("success", false);
                model.addAttribute("error", true);
                model.addAttribute("message", "....");

            }
        }
        model.addAttribute("modifier", true);
        return "profile";
    }

    private boolean passwordConfirmer(String password, String confirm){
        return password.equals(confirm);
    }
}
