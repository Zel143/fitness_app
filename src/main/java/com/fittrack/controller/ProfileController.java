package com.fittrack.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.util.DataExporter;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import java.io.File;

/**
 * ProfileController - Controller for the Profile.fxml view
 * Handles user profile management with database integration
 */
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
    @FXML private Label messageLabel;
    @FXML private TextField ageField;
    @FXML private ComboBox<String> genderComboBox;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private ComboBox<String> fitnessLevelComboBox;

    // Database manager
    private final DatabaseManager dbManager = new DatabaseManager();

    private User currentUser;

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        // Get the logged-in user from SessionManager
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            welcomeLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            loadUserProfile();
        } else {
            welcomeLabel.setText("Welcome!");
            logger.warn("⚠ Warning: No user logged in");
        }

        // Populate combo boxes
        genderComboBox.setItems(FXCollections.observableArrayList(
            "Male", "Female", "Other", "Prefer not to say"
        ));

        fitnessLevelComboBox.setItems(FXCollections.observableArrayList(
            "Beginner", "Intermediate", "Advanced", "Expert"
        ));

        messageLabel.setText("");
    }

    /**
     * Load the current user's profile data into the form fields
     */
    private void loadUserProfile() {
        if (currentUser.getAge() != null) {
            ageField.setText(String.valueOf(currentUser.getAge()));
        }
        
        if (currentUser.getGender() != null) {
            genderComboBox.setValue(currentUser.getGender());
        }
        
        if (currentUser.getHeight() != null) {
            heightField.setText(String.valueOf(currentUser.getHeight()));
        }
        
        if (currentUser.getWeight() != null) {
            weightField.setText(String.valueOf(currentUser.getWeight()));
        }
        
        if (currentUser.getFitnessLevel() != null) {
            fitnessLevelComboBox.setValue(currentUser.getFitnessLevel());
        }
    }

    /**
     * Handle the Save button click
     * Saves profile data to the database
     */
    @FXML
    private void handleSaveButtonAction(ActionEvent event) {
        try {
            // Validate and parse age
            Integer age = null;
            if (!ageField.getText().trim().isEmpty()) {
                age = Integer.parseInt(ageField.getText().trim());
                if (age < 10 || age > 120) {
                    showError("Please enter a valid age (10-120).");
                    return;
                }
            }

            // Validate and parse height
            Double height = null;
            if (!heightField.getText().trim().isEmpty()) {
                height = Double.parseDouble(heightField.getText().trim());
                if (height < 50 || height > 300) {
                    showError("Please enter a valid height in cm (50-300).");
                    return;
                }
            }

            // Validate and parse weight
            Double weight = null;
            if (!weightField.getText().trim().isEmpty()) {
                weight = Double.parseDouble(weightField.getText().trim());
                if (weight < 20 || weight > 500) {
                    showError("Please enter a valid weight in kg (20-500).");
                    return;
                }
            }

            // Get combo box values
            String gender = genderComboBox.getValue();
            String fitnessLevel = fitnessLevelComboBox.getValue();

            // Save to database
            if (currentUser != null) {
                currentUser.setAge(age);
                currentUser.setGender(gender);
                currentUser.setHeight(height);
                currentUser.setWeight(weight);
                currentUser.setFitnessLevel(fitnessLevel);
                
                boolean success = dbManager.updateUserProfile(currentUser);
                
                if (success) {
                    SessionManager.getInstance().setLoggedInUser(currentUser);
                    showSuccess("Profile saved successfully!");
                    logger.info("✓ Profile updated in database!");
                } else {
                    showError("Failed to save profile. Please try again.");
                }
            } else {
                showError("No user logged in.");
            }

        } catch (NumberFormatException e) {
            showError("Please enter valid numbers for age, height, and weight.");
        }
    }

    /**
     * Handle the Export Data button click
     */
    @FXML
    private void handleExportButtonAction(ActionEvent event) {
        if (currentUser == null) {
            showError("No user logged in.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Data");
        fileChooser.setInitialFileName("fittrack_export_" + java.time.LocalDate.now() + ".csv");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        // Get the stage from the event source
        javafx.scene.Node source = (javafx.scene.Node) event.getSource();
        javafx.stage.Stage stage = (javafx.stage.Stage) source.getScene().getWindow();

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            DataExporter exporter = new DataExporter(dbManager);
            if (exporter.exportUserData(currentUser, file)) {
                showSuccess("Data exported successfully to " + file.getName());
            } else {
                showError("Failed to export data.");
            }
        }
    }

    /**
     * Handle the Dashboard button click
     */
    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Dashboard.fxml", "FitTrack - Dashboard");
        } catch (IOException e) {
            showError("Error loading dashboard.");
            logger.error("✗ Error loading dashboard", e);
        }
    }

    /**
     * Handle Goals button click
     */
    @FXML
    private void handleGoalsButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Goals.fxml", "FitTrack - Goals");
        } catch (IOException e) {
            showError("Error loading goals screen.");
            logger.error("✗ Error loading goals screen", e);
        }
    }

    /**
     * Handle Workouts button click
     */
    @FXML
    private void handleWorkoutsButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "WorkoutPlans.fxml", "FitTrack - Workouts");
        } catch (IOException e) {
            showError("Error loading workouts screen.");
            logger.error("✗ Error loading workouts screen", e);
        }
    }

    /**
     * Handle Progress button click
     */
    @FXML
    private void handleProgressButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Progress.fxml", "FitTrack - Progress");
        } catch (IOException e) {
            showError("Error loading progress screen.");
            logger.error("✗ Error loading progress screen", e);
        }
    }

    /**
     * Handle Food Log button click
     */
    @FXML
    private void handleFoodLogButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "FoodLog.fxml", "FitTrack - Food Log");
        } catch (IOException e) {
            showError("Error loading food log screen.");
            logger.error("✗ Error loading food log screen", e);
        }
    }

    /**
     * Handle the Logout button click
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        // Clear the session
        SessionManager.getInstance().logout();
        logger.info("✓ User logged out");

        // Navigate back to login
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (IOException e) {
            showError("Error loading login screen.");
            logger.error("✗ Error loading login screen", e);
        }
    }

    /**
     * Helper method to display error messages
     */
    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    /**
     * Helper method to display success messages
     */
    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }
}
