package com.paymybuddy.app.payload;

import com.paymybuddy.app.model.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UtilisateurDTO {
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String passwordRepeat;
    private Date dateNaissance;

    public UtilisateurDTO(Utilisateur utilisateur) {
        this.prenom = utilisateur.getPrenom();
        this.nom = utilisateur.getNom();
        this.email = utilisateur.getEmail();
        this.password = utilisateur.getPassword();
        this.dateNaissance = utilisateur.getDateNaissance();
    }
}
