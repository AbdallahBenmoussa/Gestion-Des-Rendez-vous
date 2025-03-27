package com.example.demo.controllers;

import com.example.demo.models.Reservation;
import com.example.demo.models.Salle;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseController {
    private Connection connection;

    // the constructor for the database controller
    public DatabaseController() throws SQLException{
        try {
            // change this to the username and password for your Mysql db on your pc
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/reservations", "user", "password");
        } catch (SQLException e) {
            throw e;
        }
    }

    // adds reservation to the db
    public void ajouterReservation(Reservation reservation) throws SQLException {
        // prepares the reservation insertion statement and executes it
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Reservation (id, nomEmploye, codeSalle, dateRes, duree) VALUES (?,?,?,?)")) {

            ps.setInt(1, reservation.getNumRes());
            ps.setString(2, reservation.getNomEmp());
            ps.setString(3, reservation.getCodeSalle());
            ps.setDate(4, reservation.getDateRes());
            ps.setTime(5, reservation.getDuree());

            System.out.println(ps.executeUpdate() + " columns inserted.");
        } catch (SQLException e) {
            throw e;
        }
    }

    // adds salle to the db
    public void ajouterSalle(Salle salle) throws SQLException {
        // prepares the salle insertion statement and executes it
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Salle (id, batiment, numSalle) VALUES(?, ?, ?)")) {

            ps.setString(1, salle.getCodeSalle());
            ps.setString(2, Character.toString(salle.getBatiment()));
            ps.setInt(3, salle.getNumSalle());

            System.out.println(ps.executeUpdate() + " columns inserted.");
        } catch (SQLException e) {
            throw e;
        }
    }

    // returns an arraylist of all reservations in the db
    public ArrayList<Reservation> getReservations() throws SQLException {
        ArrayList<Reservation> reservations = new ArrayList<>();
        // joins the 2 tables on the foreign key
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Reservation JOIN reservations.Salle S on S.id = Reservation.codeSalle")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                reservations.add(
                        new Reservation(rs.getInt("id"),
                                rs.getString("nomEmp"),
                                new Salle(rs.getString("batiment").charAt(0), rs.getInt("numSalle")),
                                rs.getDate("dateRes"),
                                rs.getTime("duree"))
                );
            }

            return reservations;

        } catch (SQLException e) {
            throw e;
        }
    }

    // returns an arraylist of all salles in the db
    public ArrayList<Salle> getSalles() throws SQLException {
        ArrayList<Salle> salles = new ArrayList<>();
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Salle")) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                salles.add(new Salle(rs.getString("batiment").charAt(0),
                        rs.getInt("numSalle"))
                );
            }
            return salles;
        }
    }

    // this is here for testing purposes
    /*public static void main(String[] args) {
        try {
            DatabaseController db = new DatabaseController();

            //db.ajouterSalle(new Salle('B', 6));
            for (Salle salle : db.getSalles()) {
                System.out.println(salle.getBatiment() + "/" + salle.getNumSalle() + ":" + salle.getCodeSalle());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }*/
}
