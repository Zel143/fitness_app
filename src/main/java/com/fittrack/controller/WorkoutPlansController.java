package com.fittrack.controller;

import java.io.IOException;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.model.WorkoutPlan;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * WorkoutPlansController - Controller for the WorkoutPlans.fxml view
 * Manages user workout plans with database integration
 */
public class WorkoutPlansController {

    @FXML private Label welcomeLabel;
    @FXML private ListView<WorkoutPlan> plansListView;
    @FXML private TextArea planDetailsArea;
    
    @FXML private TextField planNameField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private TextField durationWeeksField;
    @FXML private Label messageLabel;

    // ✅ ADD THIS - This was missing!
    private final DatabaseManager dbManager = new DatabaseManager();
    private User currentUser;
    private final ObservableList<WorkoutPlan> plansList = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            welcomeLabel.setText(currentUser.getUsername() + "'s Workout Plans");
            System.out.println("✓ WorkoutPlans screen loaded for: " + currentUser.getUsername());
            setupListView();
            setupComboBoxes();
            loadWorkoutPlans();
        } else {
            welcomeLabel.setText("Workout Plans");
            System.out.println("⚠ Warning: No user logged in");
        }
    }

    /**
     * Setup list view
     */
    private void setupListView() {
        plansListView.setItems(plansList);
        
        // Custom cell factory to display plan names
        plansListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(WorkoutPlan plan, boolean empty) {
                super.updateItem(plan, empty);
                if (empty || plan == null) {
                    setText(null);
                } else {
                    setText(plan.planName + " (" + plan.difficulty + ")");
                }
            }
        });

        // Show details when a plan is selected
        plansListView.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> {
                if (newValue != null) {
                    showPlanDetails(newValue);
                }
            }
        );
    }

    /**
     * Setup combo boxes
     */
    private void setupComboBoxes() {
        difficultyComboBox.setItems(FXCollections.observableArrayList(
            "Beginner",
            "Intermediate",
            "Advanced",
            "Expert"
        ));
    }

    /**
     * Load workout plans from database
     */
    private void loadWorkoutPlans() {
        if (currentUser == null) return;

        plansList.clear();
        
        // ✅ FIX: Use database instead of mock data
        var plans = dbManager.getWorkoutPlans(currentUser.getUserId());
        plansList.addAll(plans);
        
        System.out.println("✓ Loaded " + plans.size() + " workout plans from database");
    }

    /**
     * Show plan details in text area
     */
    private void showPlanDetails(WorkoutPlan plan) {
        StringBuilder details = new StringBuilder();
        details.append("Plan: ").append(plan.planName).append("\n\n");
        details.append("Difficulty: ").append(plan.difficulty).append("\n");
        details.append("Duration: ").append(plan.durationWeeks).append(" weeks\n\n");
        details.append("Description:\n").append(plan.description).append("\n\n");
        details.append("Exercises:\n");
        details.append("(Exercise details coming soon...)\n");
        
        planDetailsArea.setText(details.toString());
    }

    /**
     * Handle Add Plan button click
     */
    @FXML
    private void handleAddPlanButtonAction() {
        String planName = planNameField.getText().trim();
        String description = descriptionArea.getText().trim();
        String difficulty = difficultyComboBox.getValue();
        String durationStr = durationWeeksField.getText().trim();

        System.out.println("ℹ Add Plan button clicked");
        System.out.println("ℹ Plan name: " + planName);

        // Validation
        if (planName.isEmpty()) {
            showError("Please enter a plan name");
            return;
        }

        if (description.isEmpty()) {
            showError("Please enter a description");
            return;
        }

        if (difficulty == null || difficulty.isEmpty()) {
            showError("Please select a difficulty level");
            return;
        }

        if (durationStr.isEmpty()) {
            showError("Please enter duration in weeks");
            return;
        }

        int duration;
        try {
            duration = Integer.parseInt(durationStr);
            if (duration < 1 || duration > 52) {
                showError("Duration must be between 1 and 52 weeks");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Duration must be a valid number");
            return;
        }

        System.out.println("ℹ Creating WorkoutPlan object...");

        // Create new plan
        WorkoutPlan newPlan = new WorkoutPlan();
        newPlan.userId = currentUser.getUserId();
        newPlan.planName = planName;
        newPlan.description = description;
        newPlan.difficulty = difficulty;
        newPlan.durationWeeks = duration;

        System.out.println("ℹ Saving to database...");

        // Save to database
        boolean success = dbManager.saveWorkoutPlan(newPlan);

        if (success) {
            System.out.println("✓ Workout plan saved to database with ID: " + newPlan.planId);
            
            // Add to UI list
            plansList.add(newPlan);
            
            // Show success message
            showSuccess("Workout plan added successfully!");
            
            // Clear form
            clearForm();
            
            System.out.println("✓ Workout plan added to UI: " + planName);
        } else {
            showError("Failed to save workout plan. Please try again.");
            System.err.println("✗ Failed to save workout plan to database");
        }
    }

    /**
     * Handle Delete Plan button click
     */
    @FXML
    private void handleDeletePlanButtonAction() {
        WorkoutPlan selectedPlan = plansListView.getSelectionModel().getSelectedItem();
        
        if (selectedPlan == null) {
            showError("Please select a plan to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Workout Plan?");
        confirmAlert.setContentText("Are you sure you want to delete '" + selectedPlan.planName + "'?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = dbManager.deleteWorkoutPlan(selectedPlan.planId);
                
                if (success) {
                    plansList.remove(selectedPlan);
                    planDetailsArea.clear();
                    showSuccess("Workout plan deleted successfully!");
                    System.out.println("✓ Workout plan deleted from database with ID: " + selectedPlan.planId);
                } else {
                    showError("Failed to delete workout plan. Please try again.");
                    System.err.println("✗ Failed to delete workout plan from database");
                }
            }
        });
    }

    /**
     * Handle Back to Dashboard button click
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Dashboard.fxml", "FitTrack - Dashboard");
        } catch (IOException e) {
            System.err.println("✗ Error loading Dashboard: " + e.getMessage());
        }
    }

    /**
     * Clear the form fields
     */
    private void clearForm() {
        planNameField.clear();
        descriptionArea.clear();
        difficultyComboBox.setValue(null);
        durationWeeksField.clear();
    }

    /**
     * Display error message
     */
    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
        System.err.println("✗ " + message);
    }

    /**
     * Display success message
     */
    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
        System.out.println("✓ " + message);
    }
}
