package com.paymybuddy.app.payload;

import com.paymybuddy.app.model.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern="yyyy-mm-dd")
    private Date dateNaissance;


    public UtilisateurDTO(Utilisateur utilisateur) {
        this.prenom = utilisateur.getPrenom();
        this.nom = utilisateur.getNom();
        this.email = utilisateur.getEmail();
        this.password = utilisateur.getPassword();
        this.dateNaissance = utilisateur.getDateNaissance();
    }
}
