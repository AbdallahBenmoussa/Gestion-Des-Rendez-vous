package com.example.demo.models;

import java.sql.Time;
import java.sql.Date;

public class Salle {
    private String codeSalle;
    private char batiment;
    private int numSalle;

    public Salle(char batiment, Integer numSalle) {
        this.codeSalle = batiment + (numSalle.toString().length() == 2 ? numSalle.toString(): "0" + numSalle.toString());
        this.numSalle = numSalle;
        this.batiment = batiment;
    }

    public Reservation reserver(String nomEmp, Date dateRes, Time duree) {
        return new Reservation(nomEmp, this, dateRes, duree);
    }

    public int getNumSalle() {
        return numSalle;
    }

    public char getBatiment() {
        return batiment;
    }

    public String getCodeSalle() {
        return codeSalle;
    }
}
