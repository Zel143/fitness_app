package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.model.WeightHistory;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * ProgressController - Controller for the Progress.fxml view
 * Tracks user weight history and displays progress charts
 */
public class ProgressController {

    private static final Logger logger = LoggerFactory.getLogger(ProgressController.class);

    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
    @FXML private LineChart<String, Number> weightChart;
    @FXML private TableView<WeightHistory> weightHistoryTable;
    @FXML private TableColumn<WeightHistory, LocalDate> dateColumn;
    @FXML private TableColumn<WeightHistory, Double> weightColumn;
    
    @FXML private TextField weightField;
    @FXML private DatePicker datePicker;
    @FXML private Label messageLabel;
    @FXML private Label statsLabel;

    private final DatabaseManager dbManager = new DatabaseManager();
    private User currentUser;
    private final ObservableList<WeightHistory> weightHistoryList = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            welcomeLabel.setText(currentUser.getUsername() + "'s Progress");
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            setupTableColumns();
            loadWeightHistory();
            updateChart();
            updateStats();
        } else {
            welcomeLabel.setText("Progress Tracking");
            logger.warn("⚠ Warning: No user logged in");
        }

        // Set today's date as default
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        weightColumn.setCellValueFactory(new PropertyValueFactory<>("weight"));
        
        weightHistoryTable.setItems(weightHistoryList);
    }

    /**
     * Load weight history (mock data for now)
     */
    private void loadWeightHistory() {
        if (currentUser == null) return;

        weightHistoryList.clear();
        var history = dbManager.getWeightHistory(currentUser.getUserId());
        weightHistoryList.addAll(history);
        
        logger.info("✓ Loaded {} weight history entries from database", history.size());
    }

    /**
     * Update the weight chart
     */
    private void updateChart() {
        weightChart.getData().clear();

        // Ensure chart displays chronologically (oldest -> newest) regardless of table order
        var sortedForChart = weightHistoryList.stream()
                .sorted((a, b) -> a.getDate().compareTo(b.getDate()))
                .toList();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weight (kg)");

        for (WeightHistory wh : sortedForChart) {
            series.getData().add(new XYChart.Data<>(wh.getDate().toString(), wh.getWeight()));
        }

        weightChart.setLegendVisible(true);
        weightChart.setTitle("Weight Over Time");
        weightChart.getData().add(series);
    }

    /**
     * Update statistics
     */
    private void updateStats() {
        if (weightHistoryList.isEmpty()) {
            statsLabel.setText("No weight data available");
            return;
        }

        // Database returns entries in DESC order (newest first)
        // So index 0 = latest, last index = oldest
        double currentWeight = weightHistoryList.get(0).getWeight();
        double startWeight = weightHistoryList.get(weightHistoryList.size() - 1).getWeight();
        double change = currentWeight - startWeight;
        double changePercent = (change / startWeight) * 100;

        StringBuilder stats = new StringBuilder();
        stats.append(String.format("Current Weight: %.1f kg\n", currentWeight));
        stats.append(String.format("Starting Weight: %.1f kg\n", startWeight));
        stats.append(String.format("Total Change: %+.1f kg (%.1f%%)\n", change, changePercent));
        
        if (change < 0) {
            stats.append("Status: Weight Loss Progress! 🎉");
        } else if (change > 0) {
            stats.append("Status: Weight Gain");
        } else {
            stats.append("Status: Maintaining Weight");
        }

        statsLabel.setText(stats.toString());
    }

    /**
     * Handle Add Weight button click
     */
    @FXML
    private void handleAddWeightButtonAction() {
        String weightStr = weightField.getText();
        LocalDate date = datePicker.getValue();

        // Validation
        if (weightStr.isEmpty()) {
            showError("Please enter your weight");
            return;
        }

        double weight;
        try {
            weight = Double.parseDouble(weightStr);
        } catch (NumberFormatException e) {
            showError("Weight must be a number");
            return;
        }

        if (weight <= 0 || weight > 500) {
            showError("Please enter a valid weight");
            return;
        }

        if (date == null) {
            showError("Please select a date");
            return;
        }

        // Create new weight history entry
        WeightHistory newEntry = new WeightHistory(
            currentUser.getUserId(),
            weight,
            date
        );

        // Save to database
        boolean success = dbManager.saveWeightHistory(newEntry);

        if (success) {
            logger.info("✓ Weight entry saved to database with ID: {}", newEntry.getId());
            
            // Reload from database to ensure correct data and IDs
            loadWeightHistory();
            
            // Update UI
            updateChart();
            updateStats();
            
            // Clear form
            weightField.clear();
            datePicker.setValue(LocalDate.now());
            
            showSuccess("Weight recorded successfully!");
            logger.info("✓ Weight entry added: {} kg on {}", weight, date);
        } else {
            showError("Failed to save weight entry. Please try again.");
            logger.error("✗ Failed to save weight history to database");
        }
    }

    /**
     * Handle Delete Entry button click
     */
    @FXML
    private void handleDeleteEntryButtonAction() {
        WeightHistory selectedEntry = weightHistoryTable.getSelectionModel().getSelectedItem();
        
        if (selectedEntry == null) {
            showError("Please select an entry to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Weight Entry?");
        confirmAlert.setContentText("Delete entry for " + selectedEntry.getDate() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                boolean success = dbManager.deleteWeightHistory(selectedEntry.getId());
                
                if (success) {
                    logger.info("✓ Weight entry deleted from database with ID: {}", selectedEntry.getId());
                    
                    // Reload from database to ensure data is current
                    loadWeightHistory();
                    
                    // Update UI
                    updateChart();
                    updateStats();
                    
                    showSuccess("Entry deleted successfully!");
                } else {
                    showError("Failed to delete weight entry. Please try again.");
                    logger.error("✗ Failed to delete weight history from database");
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
            logger.error("✗ Error loading Dashboard.fxml", e);
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
            logger.error("✗ Error loading Goals.fxml", e);
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
            logger.error("✗ Error loading WorkoutPlans.fxml", e);
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
            logger.error("✗ Error loading FoodLog.fxml", e);
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
            logger.error("✗ Error loading Profile.fxml", e);
        }
    }

    /**
     * Handle Logout button click
     */
    @FXML
    private void handleLogoutButtonAction(ActionEvent event) {
        SessionManager.getInstance().logout();
        logger.info("✓ User logged out");
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (IOException e) {
            logger.error("✗ Error loading Login", e);
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
