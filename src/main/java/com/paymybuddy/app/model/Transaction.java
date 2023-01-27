package com.paymybuddy.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal montant;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "typetransaction_id")
    private TypeTransaction typeTransaction;
    @OneToOne
    @JoinColumn(name = "compte_emetteur_id")
    private Compte compteEmetteur;
    @OneToOne
    @JoinColumn(name = "compte_beneficiaire_id")
    private Compte compteBeneficiaire;




}
