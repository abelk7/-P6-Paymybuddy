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

        if (userDetails != null) {
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());

            if (utilisateur != null) {
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO(utilisateur);
                basicModelUserProfil(model, utilisateurDTO, false, false, false, false, null);
            }
        }
        return "profile";
    }

    @GetMapping("/profile/modifier")
    @ResponseStatus(code = HttpStatus.OK)
    public String getProfileModifier(Model model, Authentication authentication) {
        UserDetails userDetails = getUserDetails(authentication);

        if (userDetails != null) {
            Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());
            if (utilisateur != null) {
                UtilisateurDTO utilisateurDTO = new UtilisateurDTO(utilisateur);
                basicModelUserProfil(model, utilisateurDTO, true, false, false, false, null);
            }
        }
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
                basicModelUserProfil(model, utilisateurDTO, true, true, false, false, null);
            }
        }
        return "profile";
    }

    @RequestMapping(value = "/profile/modifier/user", method = RequestMethod.POST)
    public String postModifierUtilisateur(@ModelAttribute(value = "utilisateurCourant") UtilisateurDTO utilisateur, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        Utilisateur utilisateurRechercher = null;
        boolean changedPassword = false;

        if (userDetails != null) {
            utilisateurRechercher = utilisateurService.getUser(userDetails.getUsername());
            if (utilisateurRechercher != null) {
                utilisateurDTO.setNom(utilisateurRechercher.getNom());
                utilisateurDTO.setPrenom(utilisateurRechercher.getPrenom());
                utilisateurDTO.setDateNaissance(utilisateur.getDateNaissance());

            }
        }

        if (utilisateur.getPassword() != null && utilisateur.getPasswordRepeat() != null) {
            if (utilisateur.getPassword().length() >= 8) {
                if (utilisateur.getPassword().equals(utilisateur.getPasswordRepeat())) {
                    changedPassword = true;
                } else {
                    //Les mots de passe ne corresponds pas
                    model.addAttribute("utilisateurCourant", utilisateurDTO);
                    model.addAttribute("modifier", true);
                    model.addAttribute("modifierPass", true);
                    model.addAttribute("error", true);
                    model.addAttribute("success", false);
                    model.addAttribute("message", "Les mots de passe saisie ne sont pas identique");
                    return "profile";
                }

            } else {
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

        if (utilisateur.getEmail() == null || utilisateur.getEmail().equals("") || !utilisateur.getEmail().contains("@")) {
            model.addAttribute("utilisateurCourant", utilisateurDTO);
            model.addAttribute("modifier", true);
            model.addAttribute("modifierPass", false);
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "L'addresse email n'est pas valide");
            return "profile";
        }

        if (!calculateAge(utilisateur.getDateNaissance())) {

            basicModelUserProfil(model, utilisateurDTO, true, false, false, true, "Vous devez être majeur pour vous inscrire");
            return "profile";
        }

        if (utilisateurRechercher != null) {

            Utilisateur utilisateurAEnregistrer = utilisateurService.getUser(userDetails.getUsername());
            if (!utilisateur.getEmail().equals(utilisateurAEnregistrer.getPrenom())) {
                utilisateurAEnregistrer.setEmail(utilisateur.getEmail());
            }

            if (!utilisateur.getPrenom().equals(utilisateurAEnregistrer.getPrenom())) {
                utilisateurAEnregistrer.setPrenom(utilisateur.getPrenom());
            }
            if (!utilisateur.getNom().equals(utilisateurAEnregistrer.getNom())) {
                utilisateurAEnregistrer.setNom(utilisateurAEnregistrer.getNom());
            }

            if (changedPassword) {
                if (passwordConfirmer(utilisateur.getPassword(), utilisateur.getPasswordRepeat()) && !utilisateur.getPassword().equals("")) {
                    utilisateurAEnregistrer.setPassword(utilisateur.getPassword());
                } else {
                    utilisateurAEnregistrer.setPassword(utilisateurAEnregistrer.getPassword());
                }
            }

            Utilisateur u;

            if (utilisateur.getPassword() != null) {
                u = utilisateurService.saveUser(utilisateurAEnregistrer);
            } else {
                u = utilisateurService.save(utilisateurAEnregistrer);
            }


            utilisateurDTO = new UtilisateurDTO(utilisateur.getPrenom(), u.getNom(), u.getEmail(), null, null, u.getDateNaissance());
            return "redirect:/logout";
        } else {
            model.addAttribute("utilisateurCourant", utilisateurDTO);
            model.addAttribute("modifier", true);
            model.addAttribute("modifierPass", false);
            model.addAttribute("success", false);
            model.addAttribute("error", true);
            model.addAttribute("message", "Aucun données concernant l'utilisateur saisie");
            return "profile";
        }

    }

    private boolean passwordConfirmer(String password, String confirm) {
        if (password != null && confirm != null) {
            return password.equals(confirm);
        }
        return false;
    }

    public Model basicModelUserProfil(Model model, UtilisateurDTO utilisateurCourant, boolean modifier, boolean modifierPass,
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
