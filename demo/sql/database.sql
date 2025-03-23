CREATE database IF NOT EXISTS reservations;

USE reservations;

CREATE TABLE IF NOT EXISTS Reservation (
	id INT NOT NULL ,
	nomEmploye VARCHAR(64) NOT NULL,
	codeSalle CHAR(3) NOT NULL,
	dateRes DATE NOT NULL,
	duree TIME NOT NULL,
	PRIMARY KEY (id),
	FOREIGN KEY (codeSalle) REFERENCES Salle(id)
);

CREATE TABLE IF NOT EXISTS Salle (
	id CHAR(3) NOT NULL ,
    batiment CHAR NOT NULL,
    numSalle INT NOT NULL,
	PRIMARY KEY (id)
);