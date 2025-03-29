package com.example.demo.models;

import java.sql.Date;
import java.sql.Time;

public class Reservation {
    private int numRes;
    private String nomEmp;
    private Salle salle;
    private Date dateRes;
    private Time heureDebut;
    private Time duree;
    private boolean selected;

    public Reservation(String nomEmp, Salle salle, Date dateRes, Time heureDebut, Time duree) {
        this.nomEmp = nomEmp;
        this.salle = salle;
        this.dateRes = dateRes;
        this.heureDebut = heureDebut;
        this.duree = duree;
        this.numRes = generateId();
    }

    public Reservation(int numRes, String nomEmp, Salle salle, Date dateRes, Time heureDebut, Time duree) {
        this.numRes = numRes;
        this.nomEmp = nomEmp;
        this.salle = salle;
        this.dateRes = dateRes;
        this.heureDebut = heureDebut;
        this.duree = duree;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getNumRes() {
        return numRes;
    }

    public String getNomEmp() {
        return nomEmp;
    }

    public Salle getSalle() {
        return salle;
    }

    public String getCodeSalle() {
        return salle.getCodeSalle();
    }

    public Date getDateRes() {
        return dateRes;
    }

    public Time getHeureDebut() {
        return heureDebut;
    }

    public Time getDuree() {
        return duree;
    }

    public String getDureeFormatted() {
        if (duree == null) return "";
        return String.format("%d:%02d", duree.getHours(), duree.getMinutes());
    }

    public String getDateTimeFormatted() {
        if (dateRes == null || heureDebut == null) return "";
        return String.format("%td/%<tm/%<tY %<tH:%<tM",
                new java.util.Date(dateRes.getTime() + heureDebut.getTime()));
    }

    private int generateId() {
        return (int)Long.parseLong((int)salle.getBatiment()
                + salle.getCodeSalle().substring(1)
                + dateRes.toString().replaceAll("-", "")
                + heureDebut.toString().replaceAll(":", ""));
    }
}