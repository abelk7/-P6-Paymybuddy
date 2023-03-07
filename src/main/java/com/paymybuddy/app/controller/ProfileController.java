package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class ProfileController {
    private final IUtilisateurService utilisateurService;

    @GetMapping("/profile")
    @ResponseStatus(code = HttpStatus.OK)
    public String getProfilePage(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UtilisateurDTO utilisateurDTO;

        if (userDetails != null) {
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());

            if (utilisateur != null) {
                utilisateurDTO = new UtilisateurDTO(utilisateur);
                addModelAttributeProfil(model, utilisateurDTO, false, false, false, false, null);
            }
        }
        utilisateurDTO = new UtilisateurDTO();
        addModelAttributeProfil(model, utilisateurDTO, false, false, false, false, null);
        return "profile";
    }

    @GetMapping("/profile/modifier")
    @ResponseStatus(code = HttpStatus.OK)
    public String getProfileModifier(Model model, Authentication authentication) {
        UserDetails userDetails = getUserDetails(authentication);
        UtilisateurDTO utilisateurDTO;

        if (userDetails != null) {
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
            if (utilisateur != null) {
                utilisateurDTO = new UtilisateurDTO(utilisateur);
                addModelAttributeProfil(model, utilisateurDTO, true, false, false, false, null);
            }
        }
        utilisateurDTO = new UtilisateurDTO();
        addModelAttributeProfil(model, utilisateurDTO, false, false, false, false, null);
        return "profile";
    }

    @GetMapping("/profile/modifier/password")
    @ResponseStatus(code = HttpStatus.OK)
    public String getProfileModifierPassword(Model model, Authentication authentication) {
        UserDetails userDetails = getUserDetails(authentication);

        if (userDetails != null) {
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
            if (utilisateur != null) {
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO(utilisateur);
                addModelAttributeProfil(model, utilisateurDTO, true, true, false, false, null);
            }
        }
        return "profile";
    }

    @RequestMapping(value = "/profile/modifier/user", method = RequestMethod.POST)
    @ResponseStatus(code = HttpStatus.OK)
    public String postModifierUtilisateur(@ModelAttribute(value = "utilisateurCourant") UtilisateurDTO utilisateur, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        Utilisateur utilisateurRechercher = null;
        boolean changedPassword = false;

        if (userDetails != null) {
            utilisateurRechercher = utilisateurService.getUserConnected(authentication);
            if (utilisateurRechercher != null) {
                utilisateurDTO.setNom(utilisateurRechercher.getNom());
                utilisateurDTO.setPrenom(utilisateurRechercher.getPrenom());
                utilisateurDTO.setDateNaissance(utilisateur.getDateNaissance());

            }
        }



        if (utilisateur.getEmail() == null || utilisateur.getEmail().equals("") || !utilisateur.getEmail().contains("@")) {
            addModelAttributeProfil(model, utilisateurDTO, true, false, false, true, "L'addresse email n'est pas valide");
            return "profile";
        }

        if (!calculateAge(utilisateur.getDateNaissance())) {

            addModelAttributeProfil(model, utilisateurDTO, true, false, false, true, "Vous devez être majeur pour vous inscrire");
            return "profile";
        }

        if (utilisateurRechercher != null) {

            Utilisateur utilisateurAEnregistrer = utilisateurService.getUserConnected(authentication);
            if (!utilisateur.getEmail().equals(utilisateurAEnregistrer.getPrenom())) {
                utilisateurAEnregistrer.setEmail(utilisateur.getEmail());
            }

            if (!utilisateur.getPrenom().equals(utilisateurAEnregistrer.getPrenom())) {
                utilisateurAEnregistrer.setPrenom(utilisateur.getPrenom());
            }
            if (!utilisateur.getNom().equals(utilisateurAEnregistrer.getNom())) {
                utilisateurAEnregistrer.setNom(utilisateurAEnregistrer.getNom());
            }

            if (utilisateur.getPassword() != null && utilisateur.getPasswordRepeat() != null) {
                if (utilisateur.getPassword().length() >= 8) {
                    if (utilisateur.getPassword().equals(utilisateur.getPasswordRepeat())) {

                            if (passwordConfirmer(utilisateur.getPassword(), utilisateur.getPasswordRepeat()) && !utilisateur.getPassword().equals("")) {
                                utilisateurAEnregistrer.setPassword(utilisateur.getPassword());
                                changedPassword = true;

                            } else {
                                utilisateurAEnregistrer.setPassword(utilisateurAEnregistrer.getPassword());
                            }

                    } else {
                        //Les mots de passe ne corresponds pas
                        addModelAttributeProfil(model, utilisateurDTO, true, true, false, true, "Les mots de passe saisie ne sont pas identique");
                        return "profile";
                    }

                } else {
                    //Password inférieur à 8 caractère
                    addModelAttributeProfil(model, utilisateurDTO, true, true, false, true, "Le mot de passe doit être supérieur ou égale à 8 caractères");
                    return "profile";
                }

            }




            Utilisateur u;
            if (changedPassword) {
                u = utilisateurService.saveUser(utilisateurAEnregistrer);
            } else {
                u = utilisateurService.save(utilisateurAEnregistrer);
            }

            if(u != null) {
                return "redirect:/logout";
            }else {
                addModelAttributeProfil(model, utilisateurDTO, false, false, false, true, "Une erreur est survenu lors de la tentative d'enregistrement des données");
                return "profile";
            }


        } else {
            addModelAttributeProfil(model, utilisateurDTO, false, false, false, true, "Aucun données concernant l'utilisateur saisie");
            return "profile";
        }

    }

    private boolean passwordConfirmer(String password, String confirm) {
        if (password != null && confirm != null) {
            return password.equals(confirm);
        }
        return false;
    }

    public Model addModelAttributeProfil(Model model, UtilisateurDTO utilisateurCourant, boolean modifier, boolean modifierPass,
                                         boolean success, boolean error, String message) {
        model.addAttribute("utilisateurCourant", utilisateurCourant);
        model.addAttribute("modifier", modifier);
        model.addAttribute("modifierPass", modifierPass);
        model.addAttribute("success", success);
        model.addAttribute("error", error);
        model.addAttribute("message", message);

        return model;
    }

    private UserDetails getUserDetails(Authentication authentication) {
        return (UserDetails) authentication.getPrincipal();
    }

    public boolean calculateAge(Date dateNaissance) {
        LocalDate localbirthDate = dateNaissance.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localnowDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        if ((dateNaissance != null) && (localnowDate != null)) {
            int age = Period.between(localbirthDate, localnowDate).getYears();
            return age >= 18;
        }
        return false;
    }

}
