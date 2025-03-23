package com.example.demo.models;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    private int numRes;
    private String nomEmp;
    private Salle salle;
    private Date dateRes;
    private Time duree;

    public Reservation(String nomEmp, Salle salle, Date dateRes, Time duree) {
        this.nomEmp = nomEmp;
        this.salle = salle;
        this.dateRes = dateRes;
        this.duree = duree;
        this.numRes = Integer.parseInt(Integer.toString(salle.getNumSalle())
                + Integer.toString(( int )salle.getBatiment())
                + Long.toString(dateRes.getTime())
                + Long.toString(duree.getTime())
        );
    }

    public Reservation(int numRes, String nomEmp, Salle salle, Date dateRes, Time duree) {
        this.numRes = numRes;
        this.nomEmp = nomEmp;
        this.salle = salle;
        this.dateRes = dateRes;
        this.duree = duree;
    }

    public int getNumRes() {
        return numRes;
    }

    public Date getDateRes() {
        return dateRes;
    }

    public Time getDuree() {
        return duree;
    }

    public String getNomEmp() {
        return nomEmp;
    }

    public String getCodeSalle () {
        return salle.getCodeSalle();
    }
}
