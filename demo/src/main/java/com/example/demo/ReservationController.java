package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.swing.text.html.ImageView;
import java.io.IOException;

public class ReservationController {


    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private Button ajouerButton;
    @FXML
    private Button modifferButton;
    @FXML
    private Pane background;
    @FXML
    private Button suppButton;
    @FXML
    private ImageView ajouterImage;
    @FXML
    private Button exitButton;
    @FXML
    private Button minimizeButton;
    @FXML
    private Button closeButton;

    @FXML
    private void minimizeWindow() {
        Stage stage = (Stage) minimizeButton.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
    }

    public void switchToMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ajouterRendivou.fxml"));
        stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void initialize() {
        // Rendre la fenêtre déplaçable
        background.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        background.setOnMouseDragged(event -> {
            Stage stage = (Stage) background.getScene().getWindow();
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
    }

    public void exit() {
        System.exit(0);
    }
}