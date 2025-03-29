package com.example.demo.models;

public class Salle {
    private String codeSalle;
    private char batiment;
    private int numSalle;

    public Salle(char batiment, int numSalle) {
        this.batiment = batiment;
        this.numSalle = numSalle;
        this.codeSalle = String.format("%s%02d", batiment, numSalle);
    }

    public String getCodeSalle() {
        return codeSalle; }
    public char getBatiment() {
        return batiment;
    }
    public int getNumSalle() {
        return numSalle;
    }
}