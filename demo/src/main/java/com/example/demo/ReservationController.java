package com.example.demo;

import com.example.demo.controllers.DatabaseController;
import com.example.demo.exceptions.SalleNotAvailableException;
import com.example.demo.models.Reservation;
import com.example.demo.models.Salle;
import javafx.animation.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DateTimeStringConverter;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    @FXML private TextField mod_duree;
    @FXML private TextField mod_temp_debut;
    @FXML private ChoiceBox<Salle> mod_salle_picker;
    @FXML private Label nom_emp;
    @FXML private Label prenom_emp;
    @FXML private ChoiceBox<Salle> ajouter_salle_choice;
    //ajout
    @FXML private TextField ajout_temps_debut;
    @FXML private TextField ajout_duree;
    @FXML private TextField ajout_nom;
    @FXML private TextField ajout_prenom;
    @FXML private DatePicker ajout_date;
    @FXML private Button ajouter_salle_button;
    @FXML private TextField ajout_nom1;
    @FXML private TextField ajout_prenom1;
    //fin ajout


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
    @FXML private Button confirmer_modification;
    @FXML private Button ajouter_boutton_verifivation;
    @FXML private DatePicker mod_date_picker;
    @FXML private Pane add_salle_anchor;

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
        ajout_date.setValue(LocalDate.now());
        setupTableColumns();
        setupWindowDrag();
        loadReservations();
        initializeSallesChoiceBox();
        initializeTimeTextFields();
        setupSearchListener();
        setupDeleteButton();
        setupModifyButton();
        setupAjouterButton();
        setupAjouterSalleButton();
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

    // this is here for modification event handling
    private void setupModifyButton() {
        confirmer_modification.setOnAction(event -> modifySelectedReservation());
    }

    //this is here for add event handling
    private void setupAjouterButton() {
        ajouter_boutton_verifivation.setOnAction(event -> addReservation());
    }

    private void setupAjouterSalleButton() {
        ajouter_salle_button.setOnAction(event -> addSalle());
    }


    // add reservation
    private void addReservation() {

        try {
            DatabaseController db = new DatabaseController();
            db.ajouterReservation(new Reservation(
                    ajout_prenom.getText() + " " + ajout_nom.getText(),
                    ajouter_salle_choice.getValue(),
                    Date.valueOf(ajout_date.getValue()),
                    Time.valueOf(ajout_temps_debut.getText()),
                    Time.valueOf(ajout_duree.getText())

            ));
        } catch (SQLException | SalleNotAvailableException e) {
            showAlert("Erreur", "Échec de l'ajout: " + e.getMessage());
        }
        loadReservations();
        animation_slide(add_anchor);
    }

    // add salle
    private void addSalle() {

        try {
            DatabaseController db = new DatabaseController();
            db.ajouterSalle(new Salle(
                    ajout_nom1.getText().charAt(0),
                    Integer.parseInt(ajout_prenom1.getText())
            ));
        } catch (SQLException | SalleNotAvailableException e) {
            showAlert("Erreur", "Échec de l'ajout: " + e.getMessage());
        }
        initializeSallesChoiceBox();
        animation_slide(add_salle_anchor);
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

    // fills the salles choice box with data
    private void initializeSallesChoiceBox() {
        try {
            DatabaseController db = new DatabaseController();
            mod_salle_picker.setItems(FXCollections.observableList(db.getSalles()));
            ajouter_salle_choice.setItems(FXCollections.observableList(db.getSalles()));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    // initializes the time textfields
    private void initializeTimeTextFields() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            ajout_duree.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00:00")));
            ajout_temps_debut.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00:00")));
            mod_temp_debut.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00:00")));
            mod_duree.setTextFormatter(new TextFormatter<>(new DateTimeStringConverter(format), format.parse("00:00:00")));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    //this is here to modify the selected reservations
    private void modifySelectedReservation() {
        List<Reservation> selected = allReservations.stream()
                .filter(Reservation::isSelected)
                .collect(Collectors.toList());

        if (selected.isEmpty()) {
            showAlert("Aucune sélection", "Veuillez sélectionner une réservation à modifier");
            return;
        } else if (selected.size() > 1) {
            showAlert("Plusieurs sélections", "Sélectionner juste une réservation à modifier");
        }

        Reservation res = selected.get(0);

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirmation de modification");
        confirmation.setHeaderText("Modifier la reservation");
        confirmation.setContentText("Êtes-vous sûr de vouloir modifier la réservation?");

        Optional<ButtonType> result = confirmation.showAndWait();
        // put the modified reservation in the db
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                DatabaseController db = new DatabaseController();
                db.modifierReservation(new Reservation(
                        res.getNumRes(),
                        res.getNomEmp(),
                        mod_salle_picker.getValue(),
                        Date.valueOf(mod_date_picker.getValue()),
                        Time.valueOf(mod_temp_debut.getText()),
                        Time.valueOf(mod_duree.getText())

                ));
                loadReservations();
            } catch (SQLException | SalleNotAvailableException e) {
                showAlert("Erreur", "Échec de la modification: " + e.getMessage());
            }
        }
        animation_slide(modify_anchor);
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

    // this is here to fill the mod pane with the reservation's data
    private boolean fillModPane() {
        List<Reservation> selected = allReservations.stream()
                .filter(Reservation::isSelected)
                .collect(Collectors.toList());

        if (selected.isEmpty()) {
            showAlert("Aucune sélection", "Veuillez sélectionner au moins une réservation à modifier");
            return false;
        } else if (selected.size() > 1) {
            showAlert("Plusieurs sélections", "Veuillez sélectionner juste une réservation à modifier");
            return false;
        }
        Reservation reservation = selected.get(0);

        nom_emp.setText(reservation.getNomEmp().split(" ")[0]);
        prenom_emp.setText(reservation.getNomEmp().split(" ")[1]);
        mod_salle_picker.setValue(reservation.getSalle());
        mod_date_picker.setValue(reservation.getDateRes().toLocalDate());
        mod_temp_debut.setText(reservation.getHeureDebut().toString());
        mod_duree.setText(reservation.getDuree().toString());

        return true;
    }

    @FXML
    private void toggleAnimation_modifierReservation() {
        if (!fillModPane()) return;
        animation_slide(modify_anchor);
    }

    @FXML
    private void toggleAnimation_add_salle_anchor(){
        animation_slide(add_salle_anchor);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}