package com.example.demo;

import com.example.demo.controllers.DatabaseController;
import com.example.demo.models.Reservation;
import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationController {

    // UI Components
    @FXML private TableView<Reservation> reservationTable;
    @FXML private TableColumn<Reservation, Integer> colId;
    @FXML private TableColumn<Reservation, String> colSalle;
    @FXML private TableColumn<Reservation, String> colEmploye;
    @FXML private TableColumn<Reservation, String> colDateTime;
    @FXML private TableColumn<Reservation, String> colDuree;
    @FXML private TableColumn<Reservation, Void> colAction;
    @FXML private TextField searchField;
    @FXML private Button deleteSelectedButton;

    // Panes and anchors
    @FXML private Pane add_anchor;
    @FXML private Pane modify_anchor;
    @FXML private Pane background;
    @FXML private AnchorPane LeftBottom_anchor;
    @FXML private AnchorPane left_anchorpane;
    @FXML private AnchorPane table_anchor;
    @FXML private AnchorPane h1_label;
    @FXML private AnchorPane exit_label;

    // Buttons
    @FXML private Button closeButton;
    @FXML private Button minimizeButton;
    @FXML private Button b_AjoutezReservation;
    @FXML private Button b_Recherche;
    @FXML private Button b_VoirLesSalleDisponible;
    @FXML private Button b_cancelAjouter;
    @FXML private Button b_cancelModifier;
    @FXML private Button confirmer_modification;
    @FXML private Button ajouter_boutton_verifivation;

    // Data and state management
    private ObservableList<Reservation> allReservations;
    private FilteredList<Reservation> filteredReservations;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupWindowDrag();
        loadReservations();
        setupSearchListener();
        setupDeleteButton();
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("numRes"));
        colSalle.setCellValueFactory(new PropertyValueFactory<>("codeSalle"));
        colEmploye.setCellValueFactory(new PropertyValueFactory<>("nomEmp"));

        colDateTime.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDateTimeFormatted()));

        colDuree.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDureeFormatted()));

        // Checkbox column setup
        colAction.setCellFactory(column -> new TableCell<Reservation, Void>() {
            private final CheckBox checkBox = new CheckBox();
            private final HBox container = new HBox(checkBox);

            {
                container.setAlignment(Pos.CENTER);
                checkBox.setOnAction(event -> {
                    Reservation res = getTableView().getItems().get(getIndex());
                    res.setSelected(checkBox.isSelected());
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Reservation res = getTableView().getItems().get(getIndex());
                    checkBox.setSelected(res.isSelected());
                    setGraphic(container);
                }
            }
        });
    }

    private void setupDeleteButton() {
        deleteSelectedButton.setOnAction(event -> deleteSelectedReservations());
    }

    private void deleteSelectedReservations() {
        List<Reservation> selected = allReservations.stream()
                .filter(Reservation::isSelected)
                .collect(Collectors.toList());

        if (selected.isEmpty()) {
            showAlert("Aucune sélection", "Veuillez sélectionner au moins une réservation à supprimer");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de suppression");
        confirmation.setHeaderText("Supprimer les réservations sélectionnées");
        confirmation.setContentText("Êtes-vous sûr de vouloir supprimer " + selected.size() + " réservation(s) ?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DatabaseController db = new DatabaseController();
                for (Reservation res : selected) {
                    db.supprimerReservation(res.getNumRes());
                }
                loadReservations();
            } catch (SQLException e) {
                showAlert("Erreur", "Échec de la suppression: " + e.getMessage());
            }
        }
    }

    private void setupWindowDrag() {
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

    private void loadReservations() {
        try {
            DatabaseController db = new DatabaseController();
            List<Reservation> reservations = db.getReservations();
            allReservations = FXCollections.observableArrayList(reservations);
            filteredReservations = new FilteredList<>(allReservations);
            reservationTable.setItems(filteredReservations);
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to load reservations: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert("Error", "Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void setupSearchListener() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredReservations.setPredicate(reservation -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                if (String.valueOf(reservation.getNumRes()).contains(lowerCaseFilter)) {
                    return true;
                } else if (reservation.getNomEmp().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reservation.getCodeSalle().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reservation.getDateTimeFormatted().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (reservation.getDureeFormatted().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });
    }

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
    private void buttonAjouter() {
        toggleAnimation_ajoutezReservation();
    }

    @FXML
    private void slideIn(Pane pane) {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.5), pane);
        slide.setToX(250);
        slide.play();
    }

    @FXML
    private void slideOut(Pane pane) {
        TranslateTransition slide = new TranslateTransition(Duration.seconds(0.5), pane);
        slide.setToX(0);
        slide.play();
    }

    public void togglePane(Pane pane, boolean enable) {
        FadeTransition fade = new FadeTransition(Duration.millis(500), pane);
        if (enable) {
            pane.setDisable(false);
            fade.setFromValue(0);
            fade.setToValue(1);
        } else {
            fade.setFromValue(1);
            fade.setToValue(0);
            fade.setOnFinished(e -> pane.setDisable(true));
        }
        fade.play();
    }

    @FXML
    private void animation_slide(Pane pane) {
        if (pane.getTranslateX() == 0) {
            togglePane(left_anchorpane, false);
            togglePane(LeftBottom_anchor, false);
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.5), table_anchor);
            slide.setToX(28);
            slide.play();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), table_anchor);
            scaleTransition.setToX(0.93);
            scaleTransition.play();
            ScaleTransition scaleTransition2 = new ScaleTransition(Duration.seconds(0.5), h1_label);
            scaleTransition2.setToX(0.77);
            scaleTransition2.play();
            TranslateTransition slide2 = new TranslateTransition(Duration.seconds(0.5), h1_label);
            slide2.setToX(120);
            slide2.play();
            slideIn(pane);
        } else {
            togglePane(left_anchorpane, true);
            togglePane(LeftBottom_anchor, true);
            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.5), table_anchor);
            slide.setToX(0);
            slide.play();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), table_anchor);
            scaleTransition.setToX(1);
            scaleTransition.play();
            ScaleTransition scaleTransition2 = new ScaleTransition(Duration.seconds(0.5), h1_label);
            scaleTransition2.setToX(1);
            scaleTransition2.play();
            TranslateTransition slide2 = new TranslateTransition(Duration.seconds(0.5), h1_label);
            slide2.setToX(0);
            slide2.play();
            slideOut(pane);
        }
    }

    @FXML
    private void toggleAnimation_ajoutezReservation() {
        animation_slide(add_anchor);
    }

    @FXML
    private void toggleAnimation_modifierReservation() {
        animation_slide(modify_anchor);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void exit() {
        System.exit(0);
    }
}