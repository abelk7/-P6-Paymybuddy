package com.paymybuddy.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "connections")
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "utilisateur_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Utilisateur utilisateur;
    @JoinColumn(name = "connection_utilisateur_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @ToString.Exclude
    private Utilisateur connectionUtilisateur;
}
