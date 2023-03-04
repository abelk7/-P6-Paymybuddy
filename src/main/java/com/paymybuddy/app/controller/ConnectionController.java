package com.paymybuddy.app.controller;

import com.paymybuddy.app.model.Connection;
import com.paymybuddy.app.model.Transaction;
import com.paymybuddy.app.model.Utilisateur;
import com.paymybuddy.app.payload.VirementDTO;
import com.paymybuddy.app.service.ITransactionService;
import com.paymybuddy.app.service.IUtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ConnectionController {

    @Autowired
    private IUtilisateurService utilisateurService;
    @Autowired
    private ITransactionService transactionService;

    @RequestMapping(value = "/connection/add", method = RequestMethod.POST)
    public String postNewConnection(Model model, @RequestParam String email, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        List<Transaction> transactionList = new ArrayList<>();

        Utilisateur utilisateur = utilisateurService.getUser(email);
        Utilisateur utilisateurConnected = utilisateurService.getUserConnected(authentication);

        if(utilisateurConnected != null) {
            transactionList = transactionService.findAllTransactionOfUser(utilisateurConnected.getEmail());
        }

        if (utilisateur == null) {
            addModelAttributeConnection(model, utilisateurConnected.getConnections(), transactionList,
                    utilisateurConnected.getCompte().getSolde(),true, "Aucun utilisateur n'a été retrouvé avec l'adresse email fourni",
                    false,false, new VirementDTO(null, BigDecimal.ZERO, "")
            );
            return "transfer";
        }
        if (userDetails != null) {
//            Utilisateur utilisateurConnecte = utilisateurService.getUser(userDetails.getUsername());
            if (utilisateurConnected != null) {
                Connection newConnectionUtilisateurConnecte = new Connection(null, utilisateurConnected, utilisateur);
                Connection newConnectionUtilisateur = new Connection(null, utilisateur, utilisateurConnected);

                List<Connection> connectionListUtilisateurConnecte = utilisateurConnected.getConnections();
                List<Connection> connectionListUtilisateur = utilisateur.getConnections();
                connectionListUtilisateurConnecte.add(newConnectionUtilisateurConnecte);
                connectionListUtilisateur.add(newConnectionUtilisateur);
                utilisateurConnected.setConnections(connectionListUtilisateurConnecte);
                utilisateur.setConnections(connectionListUtilisateur);

                utilisateurService.save(utilisateurConnected);
                utilisateurService.save(utilisateur);

                addModelAttributeConnection(model, utilisateur.getConnections(), transactionList,
                        utilisateur.getCompte().getSolde(),false, "L'utilisateur " + utilisateur.getPrenom() + " " + utilisateur.getNom() + " à été ajouté à vos contacts",
                        true,false, new VirementDTO(null, BigDecimal.ZERO, "")
                );
                return "transfer";
            }
        }

        addModelAttributeConnection(model, utilisateur.getConnections(), transactionList,
                utilisateur.getCompte().getSolde(),true, "Une erreur est survenue lors de l'ajout de ma nouvelle connetion...",
                false,false, new VirementDTO(null, BigDecimal.ZERO, "")
        );

        return "login";
    }

    private Model addModelAttributeConnection(Model model, List<Connection> listConnection, List<Transaction> listTransaction, BigDecimal solde, boolean error, String message, boolean addContact, boolean transfer, VirementDTO virementDTO) {
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