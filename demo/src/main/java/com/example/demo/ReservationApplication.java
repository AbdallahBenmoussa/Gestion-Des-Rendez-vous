package com.example.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ReservationApplication extends Application {

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/reservationScene.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            String css = getClass().getResource("/com/example/demo/Style.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            root.setStyle("-fx-border-color: #605856; -fx-border-width: 4px;");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setTitle("Reservation Application");
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier FXML : " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}