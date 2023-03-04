-- CREATE DATABASE IF NOT EXISTS db_paymybuddy;

-- SELECT DATABASE
-- USE db_paymybuddy;

-- #TABLE utilisateurs
CREATE TABLE IF NOT EXISTS `utilisateurs`
(
    `id`               BIGINT NOT NULL AUTO_INCREMENT,
    `nom`              VARCHAR(50),
    `prenom`           VARCHAR(50),
    `email`            VARCHAR(100),
    `password`         VARCHAR(255),
    `date_inscription` DATE,
    `date_naissance`   DATE,
    PRIMARY KEY (id)
    ) ENGINE = InnoDB;

-- #TABLE roles
CREATE TABLE IF NOT EXISTS `roles`
(
    `id`      BIGINT      NOT NULL AUTO_INCREMENT,
    `libelle` VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE = InnoDB;

-- #TABLE connections
CREATE TABLE IF NOT EXISTS `connections`
(
    `id`                        BIGINT NOT NULL AUTO_INCREMENT,
    `connection_utilisateur_id` BIGINT,
    `utilisateur_id`            BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (connection_utilisateur_id) REFERENCES utilisateurs (id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs (id)
    ) ENGINE = InnoDB;

-- #TABLE comptes
CREATE TABLE IF NOT EXISTS `comptes`
(
    `id`             BIGINT  NOT NULL AUTO_INCREMENT,
    `solde`          NUMERIC NOT NULL DEFAULT 0,
    `utilisateur_id` BIGINT,
    PRIMARY KEY (id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs (id)
    ) ENGINE = InnoDB;


-- #TABLE typestransactions
CREATE TABLE IF NOT EXISTS `typestransactions`
(
    `id`      BIGINT      NOT NULL AUTO_INCREMENT,
    `libelle` VARCHAR(50) NOT NULL,
    PRIMARY KEY (id)
    ) ENGINE = InnoDB;

-- #TABLE transactions
CREATE TABLE IF NOT EXISTS `transactions`
(
    `id`                 BIGINT  NOT NULL AUTO_INCREMENT,
    `montant`            NUMERIC NOT NULL DEFAULT 0,
    `description`                VARCHAR(255),
    `typetransaction_id` BIGINT NOT NULL,
    `compte_emetteur_id` BIGINT NOT NULL,
    `compte_beneficiaire_id` BIGINT NOT NULL,

    PRIMARY KEY (id),
    FOREIGN KEY (typetransaction_id) REFERENCES typestransactions (id),
    FOREIGN KEY (compte_emetteur_id) REFERENCES comptes (id),
    FOREIGN KEY (compte_beneficiaire_id) REFERENCES comptes (id)
    ) ENGINE = InnoDB;



-- ###### JOINTURE TABLE) #######

-- #TABLE utilisateurs_roles
CREATE TABLE IF NOT EXISTS `utilisateurs_roles`
(
    utilisateur_id BIGINT NOT NULL,
    role_id        BIGINT NOT NULL,
    PRIMARY KEY (utilisateur_id, role_id),
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateurs (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
    ) ENGINE = InnoDB;


