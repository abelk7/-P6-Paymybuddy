package com.paymybuddy.app.payload;

import com.paymybuddy.app.model.Utilisateur;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirementDTO {
    private String utilisateurEmail;
    private BigDecimal montant;
    private String libelle;

}
