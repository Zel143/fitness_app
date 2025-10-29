package com.fittrack.controller;

import com.fittrack.model.User;
import com.fittrack.model.WorkoutPlan;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

/**
 * WorkoutPlansController - Controller for the WorkoutPlans.fxml view
 * Manages user workout plans
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
     * Load workout plans (mock data for now)
     */
    private void loadWorkoutPlans() {
        if (currentUser == null) return;

        // Mock data - In real implementation, load from database
        WorkoutPlan plan1 = new WorkoutPlan();
        plan1.planName = "Full Body Strength";
        plan1.description = "3-day full body workout focusing on compound movements";
        plan1.difficulty = "Intermediate";
        plan1.durationWeeks = 8;

        WorkoutPlan plan2 = new WorkoutPlan();
        plan2.planName = "Cardio Endurance";
        plan2.description = "4-week running program to build endurance";
        plan2.difficulty = "Beginner";
        plan2.durationWeeks = 4;

        WorkoutPlan plan3 = new WorkoutPlan();
        plan3.planName = "Muscle Building";
        plan3.description = "5-day split for muscle hypertrophy";
        plan3.difficulty = "Advanced";
        plan3.durationWeeks = 12;

        plansList.addAll(plan1, plan2, plan3);
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
        String planName = planNameField.getText();
        String description = descriptionArea.getText();
        String difficulty = difficultyComboBox.getValue();
        String durationStr = durationWeeksField.getText();

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
        } catch (NumberFormatException e) {
            showError("Duration must be a number");
            return;
        }

        // Create new plan
        WorkoutPlan newPlan = new WorkoutPlan();
        newPlan.planName = planName;
        newPlan.description = description;
        newPlan.difficulty = difficulty;
        newPlan.durationWeeks = duration;

        // Add to list (in real implementation, save to database)
        plansList.add(newPlan);
        showSuccess("Workout plan added successfully!");
        clearForm();
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
                plansList.remove(selectedPlan);
                planDetailsArea.clear();
                showSuccess("Workout plan deleted successfully!");
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
    }

    /**
     * Display success message
     */
    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }
}
