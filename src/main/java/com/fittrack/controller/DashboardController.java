package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.Goal;
import com.fittrack.model.User;
import com.fittrack.model.WeightHistory;
import com.fittrack.model.WorkoutLog;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

/**
 * DashboardController - Controller for the Dashboard.fxml view
 * Displays the main dashboard with navigation options
 */
public class DashboardController {

    private static final Logger logger = LoggerFactory.getLogger(DashboardController.class);
    private static final double CM_TO_METERS = 100.0;

    @FXML
    private Label userLabel;

    @FXML
    private Label statsLabel;

    @FXML
    private Label motivationLabel;

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

    @FXML
    private Button statsButton;

    @FXML
    private Button exercisesButton;

    private User currentUser;
    private final DatabaseManager dbManager = new DatabaseManager();
    private final ObservableList<WorkoutLog> todayWorkouts = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        // Add hover animations
        if (statsButton != null) addHoverAnimation(statsButton);
        if (exercisesButton != null) addHoverAnimation(exercisesButton);

        // Get the logged-in user from SessionManager
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            // Reload user data from database to get latest profile updates
            User refreshedUser = dbManager.getUserById(currentUser.getUserId());
            if (refreshedUser != null) {
                currentUser = refreshedUser;
                SessionManager.getInstance().setLoggedInUser(currentUser);
            }
            
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            updateStatsDisplay();
            loadTodayWorkouts();
        } else {
            userLabel.setText("Welcome!");
            logger.warn("⚠ Warning: No user logged in");
        }

        // Setup table columns if table exists
        if (todayWorkoutTable != null) {
            exerciseColumn.setCellValueFactory(new PropertyValueFactory<>("workoutName"));
            setsColumn.setCellValueFactory(new PropertyValueFactory<>("sets"));
            repsColumn.setCellValueFactory(new PropertyValueFactory<>("reps"));
            weightColumn.setCellValueFactory(new PropertyValueFactory<>("weightUsed"));
            todayWorkoutTable.setItems(todayWorkouts);
        }
    }

    private void updateStatsDisplay() {
        StringBuilder stats = new StringBuilder();
        boolean hasAnyData = false;
        
        // Reload ALL data from database for fresh stats
        List<WeightHistory> weightHistory = dbManager.getWeightHistory(currentUser.getUserId());
        List<Goal> goals = dbManager.getGoals(currentUser.getUserId());
        
        // Personal Information Section
        stats.append("━━━ Personal Info ━━━\n");
        
        if (currentUser.getAge() != null) {
            stats.append("Age: ").append(currentUser.getAge()).append(" years\n");
            hasAnyData = true;
        }
        
        if (currentUser.getGender() != null && !currentUser.getGender().isEmpty()) {
            stats.append("Gender: ").append(currentUser.getGender()).append("\n");
            hasAnyData = true;
        }
        
        // Body Measurements Section
        if (currentUser.getHeight() != null || currentUser.getWeight() != null || !weightHistory.isEmpty()) {
            stats.append("\n━━━ Body Measurements ━━━\n");
        }
        
        if (currentUser.getHeight() != null) {
            stats.append("Height: ").append(currentUser.getHeight()).append(" cm\n");
            hasAnyData = true;
        }
        
        // Show latest weight from weight history if available, otherwise show profile weight
        if (!weightHistory.isEmpty()) {
            // Weight history is sorted newest first
            WeightHistory latestWeight = weightHistory.get(0);
            stats.append("Current Weight: ").append(latestWeight.getWeight()).append(" kg");
            stats.append(" (").append(latestWeight.getDate()).append(")\n");
            hasAnyData = true;
        } else if (currentUser.getWeight() != null) {
            stats.append("Weight: ").append(currentUser.getWeight()).append(" kg\n");
            hasAnyData = true;
        }

        // BMI Calculation and Category (only if both height and weight are available)
        Double weightForBMI = null;
        if (!weightHistory.isEmpty()) {
            weightForBMI = weightHistory.get(0).getWeight();
        } else if (currentUser.getWeight() != null) {
            weightForBMI = currentUser.getWeight();
        }
        
        if (currentUser.getHeight() != null && weightForBMI != null) {
            stats.append("\n━━━ Health Metrics ━━━\n");
            double heightInMeters = currentUser.getHeight() / CM_TO_METERS;
            double bmi = weightForBMI / (heightInMeters * heightInMeters);
            String bmiCategory = getBMICategory(bmi);
            stats.append(String.format("BMI: %.1f (%s)\n", bmi, bmiCategory));
            hasAnyData = true;
        }

        // Fitness Level Section
        if (currentUser.getFitnessLevel() != null && !currentUser.getFitnessLevel().isEmpty()) {
            stats.append("\n━━━ Fitness Level ━━━\n");
            stats.append(currentUser.getFitnessLevel()).append("\n");
            hasAnyData = true;
        }
        
        // Goals Summary
        if (!goals.isEmpty()) {
            stats.append("\n━━━ Goals Summary ━━━\n");
            long activeGoals = goals.stream()
                .filter(g -> g.getTargetDate() != null && g.getTargetDate().isAfter(LocalDate.now()))
                .count();
            stats.append("Active Goals: ").append(activeGoals).append(" / ").append(goals.size()).append(" total\n");
            hasAnyData = true;
        }

        // Display stats or prompt to complete profile
        if (hasAnyData) {
            statsLabel.setText(stats.toString());
        } else {
            String defaultText = "━━━ Get Started ━━━\n\nComplete your profile to see your fitness stats!\n\nClick 'My Profile' to add:\n• Age & Gender\n• Height & Weight\n• Fitness Level";
            statsLabel.setText(defaultText);
        }
    }

    /**
     * Determine BMI category based on BMI value.
     *
     * @param bmi the Body Mass Index value to categorize
     * @return a String representing the BMI category ("Underweight", "Normal", "Overweight", "Obese")
     */
    private String getBMICategory(double bmi) {
        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi < 25.0) {
            return "Normal";
        } else if (bmi < 30.0) {
            return "Overweight";
        } else {
            return "Obese";
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
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleProfileButtonAction(ActionEvent e) { switchScene(e, "Profile.fxml", "FitTrack - Profile"); }
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleGoalsButtonAction(ActionEvent e) { switchScene(e, "Goals.fxml", "FitTrack - My Goals"); }
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleWorkoutPlansButtonAction(ActionEvent e) { switchScene(e, "WorkoutPlans.fxml", "FitTrack - Workouts"); }
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleProgressButtonAction(ActionEvent e) { switchScene(e, "Progress.fxml", "FitTrack - Progress"); }
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleFoodLogButtonAction(ActionEvent e) { switchScene(e, "FoodLog.fxml", "FitTrack - Food Log"); }

    // Dashboard square handlers (Stats and Exercises)
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleStatsButtonAction(ActionEvent e) { switchScene(e, "Progress.fxml", "FitTrack - Progress"); }
    @SuppressWarnings("unused") // Called by FXML
    @FXML private void handleExercisesButtonAction(ActionEvent e) { switchScene(e, "WorkoutPlans.fxml", "FitTrack - Workouts"); }

    @SuppressWarnings("unused") // Called by FXML
    @FXML
    private void handleLogoutButtonAction(ActionEvent e) {
        SessionManager.getInstance().logout();
        switchScene(e, "Login.fxml", "FitTrack - Login");
    }

    private void switchScene(ActionEvent e, String fxml, String title) {
        try {
            SceneSwitcher.switchScene(e, fxml, title);
        } catch (IOException ex) {
            logger.error("✗ Error loading {}", fxml, ex);
        }
    }

    /**
     * Adds scale animation to buttons on hover
     */
    private void addHoverAnimation(Button button) {
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(200), button);
        scaleIn.setFromX(1.0);
        scaleIn.setFromY(1.0);
        scaleIn.setToX(1.05);
        scaleIn.setToY(1.05);

        ScaleTransition scaleOut = new ScaleTransition(Duration.millis(200), button);
        scaleOut.setFromX(1.05);
        scaleOut.setFromY(1.05);
        scaleOut.setToX(1.0);
        scaleOut.setToY(1.0);

        button.addEventHandler(MouseEvent.MOUSE_ENTERED, e -> scaleIn.play());
        button.addEventHandler(MouseEvent.MOUSE_EXITED, e -> scaleOut.play());
    }
}
