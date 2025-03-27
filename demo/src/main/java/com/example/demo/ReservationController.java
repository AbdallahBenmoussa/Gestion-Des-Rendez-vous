package com.example.demo;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import javax.swing.text.html.ImageView;
import java.io.IOException;

public class ReservationController {


    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private Scene scene;
    private Parent root;



    @FXML
    private AnchorPane LeftBottom_anchor;

    @FXML
    private Pane add_anchor;

    @FXML
    private Button b_AjoutezReservation;

    @FXML
    private Button b_Recherche;

    @FXML
    private Button b_VoirLesSalleDisponible;

    @FXML
    private Pane background;

    @FXML
    private Button closeButton;

    @FXML
    private AnchorPane exit_label;

    @FXML
    private AnchorPane h1_label;

    @FXML
    private AnchorPane left_anchorpane;

    @FXML
    private Button minimizeButton;

    @FXML
    private Pane search_anchor;

    @FXML
    private AnchorPane table_anchor;



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

    public void buttonAjouter() {
        add_anchor.setTranslateX(-300); // Position initiale hors écran
        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), add_anchor);
        slide.setToX(0); // Déplacement vers la position normale
        slide.play();
    }
}