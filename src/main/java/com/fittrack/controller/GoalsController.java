package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.Goal;
import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * GoalsController - Controller for the Goals.fxml view
 * Manages user fitness goals with database integration
 */
public class GoalsController {

    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
    @FXML private TableView<Goal> goalsTable;
    @FXML private TableColumn<Goal, String> goalTypeColumn;
    @FXML private TableColumn<Goal, Double> targetValueColumn;
    @FXML private TableColumn<Goal, String> targetUnitColumn;
    @FXML private TableColumn<Goal, LocalDate> targetDateColumn;
    @FXML private TableColumn<Goal, String> statusColumn;
    
    @FXML private ComboBox<String> goalTypeComboBox;
    @FXML private TextField targetValueField;
    @FXML private ComboBox<String> targetUnitComboBox;
    @FXML private DatePicker targetDatePicker;
    @FXML private Label messageLabel;

    private final DatabaseManager dbManager = new DatabaseManager();
    private User currentUser;
    private final ObservableList<Goal> goalsList = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getLoggedInUser();

        // Test database connection
        if (!dbManager.testConnection()) {
            showError("Database connection failed!");
            return;
        }

        if (currentUser != null) {
            welcomeLabel.setText(currentUser.getUsername() + "'s Fitness Goals");
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            setupTableColumns();
            setupComboBoxes();
            loadGoals();
        } else {
            welcomeLabel.setText("Goals");
            System.out.println("⚠ Warning: No user logged in");
        }
    }

    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        goalTypeColumn.setCellValueFactory(new PropertyValueFactory<>("goalType"));
        targetValueColumn.setCellValueFactory(new PropertyValueFactory<>("targetValue"));
        targetUnitColumn.setCellValueFactory(new PropertyValueFactory<>("targetUnit"));
        targetDateColumn.setCellValueFactory(new PropertyValueFactory<>("targetDate"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        goalsTable.setItems(goalsList);
    }

    /**
     * Setup combo boxes with options
     */
    private void setupComboBoxes() {
        goalTypeComboBox.setItems(FXCollections.observableArrayList(
            "Weight Loss",
            "Muscle Gain",
            "Run Distance",
            "Workout Frequency",
            "Body Fat %",
            "Strength Goal"
        ));

        targetUnitComboBox.setItems(FXCollections.observableArrayList(
            "kg",
            "lbs",
            "km",
            "miles",
            "%",
            "times/week"
        ));
    }

    /**
     * Load goals from database
     */
    private void loadGoals() {
        if (currentUser == null) return;

        goalsList.clear();
        var goals = dbManager.getGoals(currentUser.getUserId());
        goalsList.addAll(goals);
    }

    /**
     * Handle Add Goal button click
     */
    @FXML
    private void handleAddGoalButtonAction() {
        String goalType = goalTypeComboBox.getValue();
        String targetValueStr = targetValueField.getText();
        String targetUnit = targetUnitComboBox.getValue();
        LocalDate targetDate = targetDatePicker.getValue();

        // Validation
        if (goalType == null || goalType.isEmpty()) {
            showError("Please select a goal type");
            return;
        }

        if (targetValueStr.isEmpty()) {
            showError("Please enter a target value");
            return;
        }

        double targetValue;
        try {
            targetValue = Double.parseDouble(targetValueStr);
        } catch (NumberFormatException e) {
            showError("Target value must be a number");
            return;
        }

        if (targetUnit == null || targetUnit.isEmpty()) {
            showError("Please select a unit");
            return;
        }

        if (targetDate == null) {
            showError("Please select a target date");
            return;
        }

        // Create and save goal
        Goal goal = new Goal();
        goal.userId = currentUser.getUserId();
        goal.goalType = goalType;
        goal.targetValue = targetValue;
        goal.targetUnit = targetUnit;
        goal.targetDate = targetDate;
        goal.status = "active";

        System.out.println("DEBUG: Attempting to save goal for user: " + currentUser.getUserId());
        System.out.println("DEBUG: Goal details - Type: " + goalType + ", Value: " + targetValue + " " + targetUnit + ", Date: " + targetDate);

        if (dbManager.saveGoal(goal)) {
            System.out.println("✓ Goal saved to database with ID: " + goal.goalId);
            
            // Reload from database to get correct goal with ID
            loadGoals();
            
            // Clear form and show success
            clearForm();
            showSuccess("Goal added successfully!");
        } else {
            System.err.println("✗ Failed to save goal to database");
            showError("Failed to add goal. Please try again.");
        }
    }

    /**
     * Handle Delete Goal button click
     */
    @FXML
    private void handleDeleteGoalButtonAction() {
        Goal selectedGoal = goalsTable.getSelectionModel().getSelectedItem();
        
        if (selectedGoal == null) {
            showError("Please select a goal to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Goal?");
        confirmAlert.setContentText("Are you sure you want to delete this goal?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("DEBUG: Deleting goal with ID: " + selectedGoal.goalId);
                
                // Delete from database
                boolean success = dbManager.deleteGoal(selectedGoal.goalId);
                
                if (success) {
                    System.out.println("✓ Goal deleted from database with ID: " + selectedGoal.goalId);
                    
                    // Reload from database to ensure data is current
                    loadGoals();
                    
                    showSuccess("Goal deleted successfully!");
                } else {
                    showError("Failed to delete goal. Please try again.");
                    System.err.println("✗ Failed to delete goal from database");
                }
            }
        });
    }

    /**
     * Handle Dashboard button click
     */
    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Dashboard.fxml", "FitTrack - Dashboard");
        } catch (IOException e) {
            System.err.println("✗ Error loading Dashboard: " + e.getMessage());
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
            System.err.println("✗ Error loading Workouts: " + e.getMessage());
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
            System.err.println("✗ Error loading Progress: " + e.getMessage());
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
            System.err.println("✗ Error loading Food Log: " + e.getMessage());
        }
    }

    /**
     * Handle Profile button click
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
     * Handle Logout button click
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        SessionManager.getInstance().logout();
        System.out.println("✓ User logged out");
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (IOException e) {
            System.err.println("✗ Error loading Login: " + e.getMessage());
        }
    }

    /**
     * Handle Back to Dashboard button click
     */
    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        handleDashboardButtonAction(event);
    }

    /**
     * Clear the form fields
     */
    private void clearForm() {
        goalTypeComboBox.setValue(null);
        targetValueField.clear();
        targetUnitComboBox.setValue(null);
        targetDatePicker.setValue(null);
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
