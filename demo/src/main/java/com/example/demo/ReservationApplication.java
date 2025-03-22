package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class ReservationApplication extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/reservationScene.fxml"));
            Parent root = fxmlLoader.load();
            stage.setTitle("Reservation System");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
