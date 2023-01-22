##INSERT ROLES###
INSERT INTO roles (id, libelle)
SELECT 1, 'USER'
WHERE NOT EXISTS(select * from roles where libelle = 'USER');
#-----

INSERT INTO roles (id, libelle)
SELECT 2, 'ADMIN'
WHERE NOT EXISTS(select * from roles where libelle = 'ADMIN');

##INSERT TYPES_TRANSACTION###
INSERT INTO typestransaction (libelle)
SELECT 'DEPOT'
WHERE NOT EXISTS(select * from typestransaction where libelle = 'DEPOT');
#-----

INSERT INTO typestransaction (libelle)
SELECT 'VIREMENT'
WHERE NOT EXISTS(select * from typestransaction where libelle = 'VIREMENT');


##INSERT UTILISATEURS###
###New user1###
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
SELECT 1,
       'user1_nom',
       'user1_prenom',
       'user1@gmail.com',
       '$2a$12$qohMa8VM5GpCTYl6dYqF/uUQPLK81.DcmBlZesjawixS9cgcCbgby',
       '2023-01-22',
       '1993-07-02'
WHERE NOT EXISTS(select * from utilisateurs where email = 'user1@gmail.com');
#Role user1
INSERT INTO utilisateurs_roles (utilisateur_id, role_id)
SELECT 1, 1
WHERE NOT EXISTS(select * from utilisateurs_roles where utilisateur_id = 1 AND role_id = 1);
INSERT INTO utilisateurs_roles (utilisateur_id, role_id)
SELECT 1, 2
WHERE NOT EXISTS(select * from utilisateurs_roles where utilisateur_id = 1 AND role_id = 2);
#Compte user1
INSERT INTO comptes (id, solde, utilisateur_id)
SELECT 1, 400, 1
WHERE NOT EXISTS(select * from comptes where utilisateur_id = 1);
#-----

###New user2###
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
SELECT 2,
       'user2_nom',
       'user2_prenom',
       'user2@gmail.com',
       '$2a$12$lc.JqdL89SG5DZ/Ib1QZyOiNN.pLQeVatWRl3V3lU2rflrKO73vfq',
       '2022-12-22',
       '1999-03-02'
WHERE NOT EXISTS(select * from utilisateurs where email = 'user2@gmail.com');
#Role user2
INSERT INTO utilisateurs_roles (utilisateur_id, role_id)
SELECT 2, 1
WHERE NOT EXISTS(select * from utilisateurs_roles where utilisateur_id = 2 AND role_id = 1);
#Compte user2
INSERT INTO comptes (id, solde, utilisateur_id)
SELECT 2, 0, 2
WHERE NOT EXISTS(select *from comptes where utilisateur_id = 2);
#-----

###New user3###
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
SELECT 3,
       'user3_nom',
       'user3_prenom',
       'user3@gmail.com',
       '$2a$12$b6NEEHkQeB6e7uNcQ9gdc.AaeqXQIZfsv8D1yBiS4xYZB4JL6i.XC',
       '2022-05-26',
       '2001-04-22'
WHERE NOT EXISTS(select * from utilisateurs where email = 'user3@gmail.com');
#Role user3
INSERT INTO utilisateurs_roles (utilisateur_id, role_id)
SELECT 3, 1
WHERE NOT EXISTS(select * from utilisateurs_roles where utilisateur_id = 3 AND role_id = 1);
#Compte user3
INSERT INTO comptes (id, solde, utilisateur_id)
SELECT 3, 1600, 3
WHERE NOT EXISTS(select *from comptes where utilisateur_id = 3);
#-----

###New user4###
INSERT INTO utilisateurs (id, nom, prenom, email, password, date_inscription, date_naissance)
SELECT 4,
       'user1_nom',
       'user4_prenom',
       'user4@gmail.com',
       '$2a$12$Sylytdig68cBmkU5w4fNW.s1THISe4KV0U5kVwVd8l16F5n2aEzcG',
       '2022-07-14',
       '1990-08-02'
WHERE NOT EXISTS(select * from utilisateurs where email = 'user4@gmail.com');
#Role user4
INSERT INTO utilisateurs_roles (utilisateur_id, role_id)
SELECT 4, 1
WHERE NOT EXISTS(select * from utilisateurs_roles where utilisateur_id = 4 AND role_id = 1);
#Compte user4
INSERT INTO comptes (id, solde, utilisateur_id)
SELECT 4, 76, 4
WHERE NOT EXISTS(select *from comptes where utilisateur_id = 4);
