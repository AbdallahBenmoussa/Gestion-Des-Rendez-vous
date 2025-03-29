CREATE database IF NOT EXISTS reservations;

USE reservations;

CREATE TABLE `Reservation` (
    `id` int NOT NULL,
    `nomEmploye` varchar(64) NOT NULL,
    `codeSalle` char(3) NOT NULL,
    `dateRes` date NOT NULL,
    `duree` time NOT NULL,
    `heureDebut` time DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `codeSalle` (`codeSalle`),
    FOREIGN KEY (`codeSalle`) REFERENCES `Salle` (`id`)
) ENGINE=InnoDB;


CREATE TABLE `Salle` (
    `id` char(3) NOT NULL,
    `batiment` char(1) NOT NULL,
    `numSalle` int NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB;