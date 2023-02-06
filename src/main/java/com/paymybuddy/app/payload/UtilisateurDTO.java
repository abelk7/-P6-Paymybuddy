package com.paymybuddy.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class UtilisateurDTO {
    private String prenom;
    private String nom;
    private String email;
    private String password;
    private String passwordRepeat;
    private Date DateNaissance;

}
