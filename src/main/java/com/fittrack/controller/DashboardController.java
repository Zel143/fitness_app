package com.fittrack.controller;

import java.io.IOException;

import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * DashboardController - Controller for the Dashboard.fxml view
 * Displays the main dashboard with navigation options
 */
public class DashboardController {

    @FXML
    private Label userLabel;

    @FXML
    private Label statsLabel;

    private User currentUser;

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
        } else {
            userLabel.setText("Welcome, Guest!");
            System.out.println("⚠ Warning: No user logged in");
        }
    }

    /**
     * Update the stats display based on user profile
     */
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
     * Handle the Profile button click
     */
    @FXML
    private void handleProfileButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Profile.fxml", "FitTrack - Profile");
        } catch (IOException e) {
            System.err.println("✗ Error loading Profile: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the Goals button click
     */
    @FXML
    private void handleGoalsButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Goals.fxml", "FitTrack - My Goals");
        } catch (IOException e) {
            System.err.println("✗ Error loading Goals: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the Workout Plans button click
     * ✅ THIS IS THE MISSING METHOD!
     */
    @FXML
    private void handleWorkoutPlansButtonAction(ActionEvent event) {
        try {
            System.out.println("ℹ Workout Plans button clicked!");
            SceneSwitcher.switchScene(event, "WorkoutPlans.fxml", "FitTrack - Workout Plans");
            System.out.println("✓ WorkoutPlans screen loaded successfully!");
        } catch (IOException e) {
            System.err.println("✗ Error loading Workout Plans: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the Progress button click
     */
    @FXML
    private void handleProgressButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Progress.fxml", "FitTrack - Progress");
        } catch (IOException e) {
            System.err.println("✗ Error loading Progress: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the Food Log button click
     */
    @FXML
    private void handleFoodLogButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "FoodLog.fxml", "FitTrack - Food Log");
        } catch (IOException e) {
            System.err.println("✗ Error loading Food Log: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Handle the Logout button click
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        SessionManager.getInstance().logout();
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (IOException e) {
            System.err.println("✗ Error loading Login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
