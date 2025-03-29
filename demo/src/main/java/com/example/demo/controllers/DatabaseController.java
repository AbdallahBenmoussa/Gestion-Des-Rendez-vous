package com.example.demo.controllers;

import com.example.demo.models.Reservation;
import com.example.demo.models.Salle;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseController {
    private static final String URL = "jdbc:mysql://localhost:3306/reservations";
    private static final String USER = "root"; // Change to your MySQL username
    private static final String PASSWORD = "azertyuiop"; // Change to your MySQL password

    public Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database: " + e.getMessage());
        }
    }

    public void ajouterReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO Reservation (id, nomEmploye, codeSalle, dateRes, heureDebut, duree) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservation.getNumRes());
            ps.setString(2, reservation.getNomEmp());
            ps.setString(3, reservation.getCodeSalle());
            ps.setDate(4, reservation.getDateRes());
            ps.setTime(5, reservation.getHeureDebut());
            ps.setTime(6, reservation.getDuree());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to add reservation: " + e.getMessage());
        }
    }

    public List<Reservation> getReservations() throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT r.*, s.batiment, s.numSalle FROM Reservation r JOIN Salle s ON r.codeSalle = s.id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getString("nomEmploye"),
                        new Salle(rs.getString("batiment").charAt(0), rs.getInt("numSalle")),
                        rs.getDate("dateRes"),
                        rs.getTime("heureDebut"),
                        rs.getTime("duree")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch reservations: " + e.getMessage());
        }
        return reservations;
    }

    public void supprimerReservation(int reservationId) throws SQLException {
        String sql = "DELETE FROM Reservation WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, reservationId);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("No reservation found with ID: " + reservationId);
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to delete reservation: " + e.getMessage());
        }
    }

    public void modifierReservation(Reservation reservation) throws SQLException {
        String sql = "UPDATE Reservation SET nomEmploye = ?, codeSalle = ?, dateRes = ?, heureDebut = ?, duree = ? WHERE id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, reservation.getNomEmp());
            ps.setString(2, reservation.getCodeSalle());
            ps.setDate(3, reservation.getDateRes());
            ps.setTime(4, reservation.getHeureDebut());
            ps.setTime(5, reservation.getDuree());
            ps.setInt(6, reservation.getNumRes());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update reservation: " + e.getMessage());
        }
    }

    public void ajouterSalle(Salle salle) throws SQLException {
        String sql = "INSERT INTO Salle (id, batiment, numSalle) VALUES (?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, salle.getCodeSalle());
            ps.setString(2, String.valueOf(salle.getBatiment()));
            ps.setInt(3, salle.getNumSalle());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to add room: " + e.getMessage());
        }
    }

    public List<Salle> getSalles() throws SQLException {
        List<Salle> salles = new ArrayList<>();
        String sql = "SELECT * FROM Salle";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                salles.add(new Salle(
                        rs.getString("batiment").charAt(0),
                        rs.getInt("numSalle")
                ));
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to fetch rooms: " + e.getMessage());
        }
        return salles;
    }

    public boolean isSalleDisponible(String codeSalle, Date date, Time heureDebut, Time duree) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Reservation WHERE codeSalle = ? AND dateRes = ? AND " +
                "((heureDebut <= ? AND ADDTIME(heureDebut, duree) > ?) OR " +
                "(heureDebut < ADDTIME(?, ?) AND ADDTIME(heureDebut, duree) >= ?))";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, codeSalle);
            ps.setDate(2, date);
            ps.setTime(3, heureDebut);
            ps.setTime(4, heureDebut);
            ps.setTime(5, heureDebut);
            ps.setTime(6, duree);
            ps.setTime(7, heureDebut);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        }
        return true;
    }

    //Testing
    public static void main(String[] args) {
        try {
            DatabaseController db = new DatabaseController();
            System.out.println("Connection successful! Found rooms: " + db.getSalles().size());

            boolean available = db.isSalleDisponible(
                    "A01",
                    Date.valueOf("2023-12-15"),
                    Time.valueOf("14:00:00"),
                    Time.valueOf("01:00:00"));
            System.out.println("Room availability: " + available);

        } catch (SQLException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}