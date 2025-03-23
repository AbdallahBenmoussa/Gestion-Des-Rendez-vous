package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import javax.swing.text.html.ImageView;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class ReservationController {

    @FXML
    private Button ajouerButton;

    @FXML
    private Button modifferButton;

    @FXML
    private Button suppButton;

    @FXML
    private ImageView ajouterImage;


    @FXML
    private Button exitButton ;

    private Stage stage;
    private Scene scene;
    private Parent root;


    public void switchToMenu(javafx.event.ActionEvent actionEvent) throws IOException {
        root = FXMLLoader.load(getClass().getResource("ajouterRendivou.fxml"));
        stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void exit(){
        System.exit(0);
    }

}
