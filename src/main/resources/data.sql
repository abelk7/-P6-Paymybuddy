-- INSERT ROLES
INSERT INTO roles (id, libelle) values (1, 'USER');
INSERT INTO roles (id, libelle) values (2, 'ADMIN');

-- INSERT TYPES_TRANSACTIONS
INSERT INTO typestransactions (id, libelle) values (1, 'DEPOT');
INSERT INTO typestransactions (id, libelle)  values (2 ,'VIREMENT');

-- INSERT UTILISATEURS
-- user1
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
values (1, 'userNom', 'userPrenom', 'user@user.com', '$2a$12$qohMa8VM5GpCTYl6dYqF/uUQPLK81.DcmBlZesjawixS9cgcCbgby', '2023-01-22', '1993-07-02');
-- Role user1
INSERT INTO utilisateurs_roles (utilisateur_id, role_id) values (1,1);
-- Compte user1
INSERT INTO comptes (id, solde, utilisateur_id) values (1, 400, 1);

-- New user2
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
values (2, 'user2Nom', 'user2Prenom', 'user2@user.com', '$2a$12$lc.JqdL89SG5DZ/Ib1QZyOiNN.pLQeVatWRl3V3lU2rflrKO73vfq', '2022-12-22', '1999-03-02');
-- Role user2
INSERT INTO utilisateurs_roles (utilisateur_id, role_id) values (2,1);
-- Compte user2
INSERT INTO comptes (id, solde, utilisateur_id) values (2, 0, 2);

-- New user3
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
values (3, 'adminNom', 'adminPrenom', 'admin@admin.com', '$2a$12$b6NEEHkQeB6e7uNcQ9gdc.AaeqXQIZfsv8D1yBiS4xYZB4JL6i.XC', '2022-05-26', '2001-04-22');
-- Role user3
INSERT INTO utilisateurs_roles (utilisateur_id, role_id) values (3, 1);
INSERT INTO utilisateurs_roles (utilisateur_id, role_id) values (3, 2);
-- Compte user3
INSERT INTO comptes (id, solde, utilisateur_id) values (3, 1600, 3);

-- New user4
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
values (4, 'user4Nom', 'user4Prenom', 'user4@user.com', '$2a$12$Sylytdig68cBmkU5w4fNW.s1THISe4KV0U5kVwVd8l16F5n2aEzcG', '2022-07-14', '1990-08-02');
-- Role user4
INSERT INTO utilisateurs_roles (utilisateur_id, role_id) values (4,1);
-- Compte user4
INSERT INTO comptes (id, solde, utilisateur_id) values (4, 76, 4);
