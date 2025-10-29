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

    @FXML private Label userLabel;
    @FXML private Label motivationLabel;
    @FXML private Label statsLabel;

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
        if (currentUser == null) return;

        StringBuilder stats = new StringBuilder();
        
        if (currentUser.getAge() != null || currentUser.getHeight() != null || 
            currentUser.getWeight() != null || currentUser.getFitnessLevel() != null) {
            
            if (currentUser.getAge() != null) {
                stats.append("Age: ").append(currentUser.getAge()).append(" years\n");
            }
            
            if (currentUser.getHeight() != null && currentUser.getWeight() != null) {
                stats.append("Height: ").append(currentUser.getHeight()).append(" cm | ");
                stats.append("Weight: ").append(currentUser.getWeight()).append(" kg\n");
                
                // Calculate BMI
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
        }
    }

    /**
     * Handle the Workout Plans button click
     */
    @FXML
    private void handleWorkoutPlansButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "WorkoutPlans.fxml", "FitTrack - Workout Plans");
        } catch (IOException e) {
            System.err.println("✗ Error loading Workout Plans: " + e.getMessage());
        }
    }

    /**
     * Handle the Progress button click
     */
    @FXML
    private void handleProgressButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Progress.fxml", "FitTrack - Track Progress");
        } catch (IOException e) {
            System.err.println("✗ Error loading Progress: " + e.getMessage());
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
        }
    }

    /**
     * Handle the Logout button click
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        // Clear the session
        SessionManager.getInstance().logout();
        System.out.println("✓ User logged out");

        // Navigate back to login
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (IOException e) {
            System.err.println("✗ Error loading Login: " + e.getMessage());
        }
    }
}
