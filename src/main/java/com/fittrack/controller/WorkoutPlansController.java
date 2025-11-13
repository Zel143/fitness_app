package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.model.WorkoutLog;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * WorkoutPlansController - Unified controller for Workout Plans and Workout Log
 * Manages both workout scheduling and daily exercise logging
 */
public class WorkoutPlansController {

    // Workout Plans Tab
    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
    @FXML private ListView<WorkoutPlan> plansListView;
    @FXML private TextArea planDetailsArea;
    
    @FXML private TextField planNameField;
    @FXML private TextArea descriptionArea;
    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private TextField durationWeeksField;
    
    // Workout Log Tab
    @FXML private TableView<WorkoutLog> workoutLogTable;
    @FXML private TableColumn<WorkoutLog, String> workoutNameColumn;
    @FXML private TableColumn<WorkoutLog, Integer> setsColumn;
    @FXML private TableColumn<WorkoutLog, Integer> repsColumn;
    @FXML private TableColumn<WorkoutLog, Double> weightColumn;
    @FXML private TableColumn<WorkoutLog, LocalDate> dateColumn;
    
    @FXML private TextField workoutNameField;
    @FXML private TextField setsField;
    @FXML private TextField repsField;
    @FXML private TextField weightField;
    @FXML private DatePicker datePicker;
    
    // Common
    @FXML private Label messageLabel;

    private final DatabaseManager dbManager = new DatabaseManager();
    private User currentUser;
    private final ObservableList<WorkoutPlan> plansList = FXCollections.observableArrayList();
    private final ObservableList<WorkoutLog> workoutLogList = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            welcomeLabel.setText(currentUser.getUsername() + "'s Workout Plans");
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            System.out.println("✓ WorkoutPlans screen loaded for: " + currentUser.getUsername());
            
            // Setup Workout Plans tab
            setupListView();
            setupComboBoxes();
            loadWorkoutPlans();
            
            // Setup Workout Log tab
            setupWorkoutLogTable();
            loadWorkoutLogs();
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
     * Handle Goals button click
     */
    @FXML
    private void handleGoalsButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Goals.fxml", "FitTrack - Goals");
        } catch (IOException e) {
            System.err.println("✗ Error loading Goals: " + e.getMessage());
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

    // ==================== WORKOUT LOG TAB METHODS ====================

    /**
     * Setup workout log table with column bindings
     */
    private void setupWorkoutLogTable() {
        workoutNameColumn.setCellValueFactory(new PropertyValueFactory<>("workoutName"));
        setsColumn.setCellValueFactory(new PropertyValueFactory<>("sets"));
        repsColumn.setCellValueFactory(new PropertyValueFactory<>("reps"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weightUsed"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        workoutLogTable.setItems(workoutLogList);
        
        System.out.println("✓ Workout log table configured");
    }

    /**
     * Load workout logs from database
     */
    private void loadWorkoutLogs() {
        if (currentUser == null) return;

        workoutLogList.clear();
        
        var logs = dbManager.getWorkoutLogs(currentUser.getUserId());
        workoutLogList.addAll(logs);
        
        System.out.println("✓ Loaded " + logs.size() + " workout logs from database");
    }

    /**
     * Handle Add Workout button click
     */
    @FXML
    private void handleAddWorkout() {
        String workoutName = workoutNameField.getText().trim();
        String setsStr = setsField.getText().trim();
        String repsStr = repsField.getText().trim();
        String weightStr = weightField.getText().trim();
        LocalDate date = datePicker.getValue();

        System.out.println("ℹ Add Workout button clicked");
        System.out.println("ℹ Workout name: " + workoutName);

        // Validation
        if (workoutName.isEmpty()) {
            showError("Please enter workout name");
            return;
        }

        if (setsStr.isEmpty()) {
            showError("Please enter number of sets");
            return;
        }

        if (repsStr.isEmpty()) {
            showError("Please enter number of reps");
            return;
        }

        if (weightStr.isEmpty()) {
            showError("Please enter weight used");
            return;
        }

        if (date == null) {
            showError("Please select a date");
            return;
        }

        int sets, reps;
        double weight;
        
        try {
            sets = Integer.parseInt(setsStr);
            if (sets < 1 || sets > 100) {
                showError("Sets must be between 1 and 100");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Sets must be a valid number");
            return;
        }

        try {
            reps = Integer.parseInt(repsStr);
            if (reps < 1 || reps > 1000) {
                showError("Reps must be between 1 and 1000");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Reps must be a valid number");
            return;
        }

        try {
            weight = Double.parseDouble(weightStr);
            if (weight < 0 || weight > 1000) {
                showError("Weight must be between 0 and 1000 kg");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Weight must be a valid number");
            return;
        }

        System.out.println("ℹ Creating WorkoutLog object...");

        // Create new workout log
        WorkoutLog newLog = new WorkoutLog();
        newLog.setUserId(currentUser.getUserId());
        newLog.setWorkoutName(workoutName);
        newLog.setSets(sets);
        newLog.setReps(reps);
        newLog.setWeightUsed(weight);
        newLog.setDate(date);

        System.out.println("ℹ Saving to database...");

        // Save to database
        boolean success = dbManager.saveWorkoutLog(newLog);

        if (success) {
            System.out.println("✓ Workout log saved to database with ID: " + newLog.getId());
            
            // Add to UI list
            workoutLogList.add(newLog);
            
            // Show success message
            showSuccess("Workout logged successfully!");
            
            // Clear form
            handleClear();
            
            System.out.println("✓ Workout log added to UI: " + workoutName);
        } else {
            showError("Failed to save workout log. Please try again.");
            System.err.println("✗ Failed to save workout log to database");
        }
    }

    /**
     * Handle Delete Workout button click
     */
    @FXML
    private void handleDeleteWorkout() {
        WorkoutLog selectedLog = workoutLogTable.getSelectionModel().getSelectedItem();
        
        if (selectedLog == null) {
            showError("Please select a workout to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Workout Log?");
        confirmAlert.setContentText("Are you sure you want to delete '" + selectedLog.getWorkoutName() + "'?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = dbManager.deleteWorkoutLog(selectedLog.getId());
                
                if (success) {
                    workoutLogList.remove(selectedLog);
                    showSuccess("Workout log deleted successfully!");
                    System.out.println("✓ Workout log deleted from database with ID: " + selectedLog.getId());
                } else {
                    showError("Failed to delete workout log. Please try again.");
                    System.err.println("✗ Failed to delete workout log from database");
                }
            }
        });
    }

    /**
     * Handle Clear button click - clears workout log form
     */
    @FXML
    private void handleClear() {
        workoutNameField.clear();
        setsField.clear();
        repsField.clear();
        weightField.clear();
        datePicker.setValue(null);
        messageLabel.setText("");
        System.out.println("✓ Workout log form cleared");
    }

    // ==================== HELPER METHODS ====================

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
