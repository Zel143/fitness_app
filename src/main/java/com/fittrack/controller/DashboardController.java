package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.model.WorkoutLog;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * DashboardController - Controller for the Dashboard.fxml view
 * Displays the main dashboard with navigation options
 */
public class DashboardController {

    @FXML
    private Label userLabel;

    @FXML
    private Label statsLabel;

    @FXML
    private CheckBox themeToggle;

    @FXML
    private TableView<WorkoutLog> todayWorkoutTable;

    @FXML
    private TableColumn<WorkoutLog, String> exerciseColumn;

    @FXML
    private TableColumn<WorkoutLog, Integer> setsColumn;

    @FXML
    private TableColumn<WorkoutLog, Integer> repsColumn;

    @FXML
    private TableColumn<WorkoutLog, Double> weightColumn;

    private User currentUser;
    private final DatabaseManager dbManager = new DatabaseManager();
    private ObservableList<WorkoutLog> todayWorkouts = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        // Get the logged-in user from SessionManager
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            updateStatsDisplay();
            loadTodayWorkouts();
        } else {
            userLabel.setText("Welcome, Guest!");
            System.out.println("⚠ Warning: No user logged in");
        }

        // Setup table columns if table exists
        if (todayWorkoutTable != null) {
            exerciseColumn.setCellValueFactory(new PropertyValueFactory<>("workoutName"));
            setsColumn.setCellValueFactory(new PropertyValueFactory<>("sets"));
            repsColumn.setCellValueFactory(new PropertyValueFactory<>("reps"));
            weightColumn.setCellValueFactory(new PropertyValueFactory<>("weightUsed"));
            todayWorkoutTable.setItems(todayWorkouts);
        }

        Platform.runLater(() -> {
            Scene scene = userLabel.getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("src/main/styles/styleSheet.css").toExternalForm());

                Parent root = scene.getRoot();
                root.getStyleClass().addAll("base", "light");
            }

            if (themeToggle != null) {
                themeToggle.selectedProperty().addListener((obs, oldVal, newVal) -> toggleTheme(newVal));
            }
        });
    }

    private void toggleTheme(boolean darkMode) {
        Scene scene = themeToggle.getScene();
        if (scene != null) {
            Parent root = scene.getRoot();
            root.getStyleClass().removeAll("light", "dark");
            root.getStyleClass().add(darkMode ? "dark" : "light");
        }
    }

    private void updateStatsDisplay() {
        if (currentUser.hasCompleteProfile()) {
            StringBuilder stats = new StringBuilder();
            stats.append("Age: ").append(currentUser.getAge()).append(" years\n");
            stats.append("Height: ").append(currentUser.getHeight()).append(" cm\n");
            stats.append("Weight: ").append(currentUser.getWeight()).append(" kg\n");

            if (currentUser.getHeight() != null && currentUser.getWeight() != null) {
                double heightInMeters = currentUser.getHeight() / 100.0;
                double bmi = currentUser.getWeight() / (heightInMeters * heightInMeters);
                stats.append(String.format("BMI: %.1f\n", bmi));
            }

            if (currentUser.getFitnessLevel() != null) {
                stats.append("Fitness Level: ").append(currentUser.getFitnessLevel());
            }

            statsLabel.setText(stats.toString());
        } else {
            statsLabel.setText("Complete your profile to see your fitness stats!");
        }
    }

    /**
     * Load today's workout exercises from the database
     */
    private void loadTodayWorkouts() {
        if (currentUser == null || todayWorkoutTable == null) {
            return;
        }

        // Get all workout logs for the user
        List<WorkoutLog> allWorkouts = dbManager.getWorkoutLogs(currentUser.getUserId());
        
        // Filter for today's workouts
        LocalDate today = LocalDate.now();
        todayWorkouts.clear();
        
        for (WorkoutLog workout : allWorkouts) {
            if (workout.getDate() != null && workout.getDate().equals(today)) {
                todayWorkouts.add(workout);
            }
        }

        System.out.println("✓ Loaded " + todayWorkouts.size() + " workouts for today");
    }

    // Navigation Handlers
    @FXML private void handleProfileButtonAction(ActionEvent e) { switchScene(e, "Profile.fxml", "FitTrack - Profile"); }
    @FXML private void handleGoalsButtonAction(ActionEvent e) { switchScene(e, "Goals.fxml", "FitTrack - My Goals"); }
    @FXML private void handleWorkoutPlansButtonAction(ActionEvent e) { switchScene(e, "WorkoutPlans.fxml", "FitTrack - Workout Plans"); }
    @FXML private void handleProgressButtonAction(ActionEvent e) { switchScene(e, "Progress.fxml", "FitTrack - Progress"); }
    @FXML private void handleFoodLogButtonAction(ActionEvent e) { switchScene(e, "FoodLog.fxml", "FitTrack - Food Log"); }

    @FXML
    private void handleLogoutButtonAction(ActionEvent e) {
        SessionManager.getInstance().logout();
        switchScene(e, "Login.fxml", "FitTrack - Login");
    }

    private void switchScene(ActionEvent e, String fxml, String title) {
        try {
            SceneSwitcher.switchScene(e, fxml, title);
        } catch (IOException ex) {
            System.err.println("✗ Error loading " + fxml + ": " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
