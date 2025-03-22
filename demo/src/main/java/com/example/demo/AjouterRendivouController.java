package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AjouterRendivouController {

    @FXML
    private Button confimerReservationButton;

    @FXML
    private Button retoureToMainButton;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void backToMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("reservationScene.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


}
