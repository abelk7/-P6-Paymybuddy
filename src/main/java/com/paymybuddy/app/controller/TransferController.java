package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Connection;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.TypeTransaction;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.UtilisateurDTO;
import com.paymybuddy.app.payload.VirementDTO;
import com.paymybuddy.app.service.ITransactionService;
import com.paymybuddy.app.service.ITypeTransactionService;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class TransferController {
    private final IUtilisateurService utilisateurService;
    private final ITypeTransactionService typeTransactionService;
    private final ITransactionService transactionService;

    @GetMapping("/transfer")
    @ResponseStatus(code = HttpStatus.OK)
    public String getTransferPage(Model model,  Authentication authentication, @ModelAttribute(value = "virementDTO") VirementDTO virementDTO) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateur = utilisateurService.getUser(userDetails.getUsername());

        List<Transaction> transactionList = transactionService.findAllTransactionOfUser(utilisateur.getId());
        if(transactionList == null) {
            transactionList = new ArrayList<>();
        }

        if(utilisateur != null) {
            addModelAttributeTransfer(model, utilisateur.getConnections(), transactionList,
                    utilisateur.getCompte().getSolde(),false, null,
                    false,false, virementDTO
            );
            return "transfer";
        }

        model = addModelAttributeTransfer(model, Collections.EMPTY_LIST, transactionList,
                BigDecimal.ZERO,false, null,
                false,false, virementDTO
        );
        return "transfer";
    }

    @RequestMapping(value = "/transfer/pay", method = RequestMethod.POST)
    public String effectuerVirement(@ModelAttribute(value = "virementDTO") VirementDTO virementDTO, Model model, Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        Utilisateur utilisateurEmmeteur = utilisateurService.getUser(userDetails.getUsername());

        Utilisateur utilisateurRecepteur = utilisateurService.getUser(virementDTO.getUtilisateurEmail());

        TypeTransaction typeTransactionVirement = typeTransactionService.findByLibelle("Virement");

        List<Transaction> transactionList = transactionService.findAllTransactionOfUser(utilisateurEmmeteur.getId());
        if(transactionList == null) {
            transactionList = new ArrayList<>();
        }

        if (virementDTO != null && virementDTO.getMontant().compareTo(BigDecimal.valueOf(0.00)) == 1 && utilisateurEmmeteur.getCompte().getSolde().compareTo(BigDecimal.valueOf(0.00)) == 1) {
            if (utilisateurEmmeteur != null && utilisateurRecepteur != null) {
                utilisateurEmmeteur.getCompte().setSolde(utilisateurEmmeteur.getCompte().getSolde().subtract(virementDTO.getMontant()));
                utilisateurRecepteur.getCompte().setSolde(utilisateurRecepteur.getCompte().getSolde().add(virementDTO.getMontant()));
                Transaction transactionEnCour = new Transaction();
                transactionEnCour.setMontant(virementDTO.getMontant());
                transactionEnCour.setCompteEmetteur(utilisateurEmmeteur.getCompte());
                transactionEnCour.setCompteBeneficiaire(utilisateurRecepteur.getCompte());
                transactionEnCour.setTypeTransaction(typeTransactionVirement);
                transactionEnCour.setDate(new Date());
                transactionEnCour.setLibelle(virementDTO.getLibelle());

                utilisateurEmmeteur = utilisateurService.save(utilisateurEmmeteur);
                utilisateurRecepteur = utilisateurService.save(utilisateurRecepteur);
                transactionService.save(transactionEnCour);


                transactionList = transactionService.findAllTransactionOfUser(utilisateurEmmeteur.getId());

                virementDTO.setMontant(BigDecimal.ZERO);

                addModelAttributeTransfer(model, utilisateurEmmeteur.getConnections(), transactionList,
                        utilisateurEmmeteur.getCompte().getSolde(),false, "Virement effectué d'un montant de " + transactionEnCour.getMontant().toString()
                                + "€ à " + utilisateurRecepteur.getPrenom() + " " + utilisateurRecepteur.getNom(),
                        false,true, virementDTO
                );
                return "transfer";
            }
        } else if(virementDTO != null && virementDTO.getMontant().compareTo(BigDecimal.valueOf(0.00)) == 1 && utilisateurEmmeteur.getCompte().getSolde().compareTo(BigDecimal.valueOf(0.00)) == 0) {
            model = addModelAttributeTransfer(model, utilisateurEmmeteur.getConnections(), transactionList,
                    utilisateurEmmeteur.getCompte().getSolde(),true, "Vous devez alimentez votre compte pour pouvoir effectuer des nouveaux virement",
                    false,false, virementDTO
                    );
            return "transfer";
        } else {
            model = addModelAttributeTransfer(model, utilisateurEmmeteur.getConnections(), transactionList,
                    utilisateurEmmeteur.getCompte().getSolde(),true, "Veuillez saisir un montant supérieur à 0.00€",
                    false,false, virementDTO
            );
            return "transfer";
        }

        model = addModelAttributeTransfer(model, utilisateurEmmeteur.getConnections(), transactionList,
                utilisateurEmmeteur.getCompte().getSolde(),true, "Une erreur est lors de la tentative de virement.. veuillez nous excusez",
                false,false, virementDTO
        );

        return "transfer";
    }

    private Model addModelAttributeTransfer(Model model, List<Connection> listConnection, List<Transaction> listTransaction,BigDecimal solde,boolean error, String message, boolean addContact, boolean transfer, VirementDTO virementDTO) {
        model.addAttribute("listConnection", listConnection);
        model.addAttribute("listTransaction", listTransaction);
        model.addAttribute("solde", solde);
        model.addAttribute("error", error);
        model.addAttribute("message", message);
        model.addAttribute("addContact", addContact);
        model.addAttribute("transfer", transfer);
        model.addAttribute("virementDTO", virementDTO);
        return model;
    }

}
