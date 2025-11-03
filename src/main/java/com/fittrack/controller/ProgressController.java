package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;

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

    @FXML private Label welcomeLabel;
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
            setupTableColumns();
            loadWeightHistory();
            updateChart();
            updateStats();
        } else {
            welcomeLabel.setText("Progress Tracking");
            System.out.println("⚠ Warning: No user logged in");
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
        
        System.out.println("✓ Loaded " + history.size() + " weight history entries from database");
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

        System.out.println("DEBUG: Saving weight entry - User ID: " + currentUser.getUserId() + 
                         ", Weight: " + weight + ", Date: " + date);

        // Save to database
        boolean success = dbManager.saveWeightHistory(newEntry);

        if (success) {
            System.out.println("✓ Weight entry saved to database with ID: " + newEntry.getId());
            
            // Reload from database to ensure correct data and IDs
            loadWeightHistory();
            
            // Update UI
            updateChart();
            updateStats();
            
            // Clear form
            weightField.clear();
            datePicker.setValue(LocalDate.now());
            
            showSuccess("Weight recorded successfully!");
            System.out.println("✓ Weight entry added: " + weight + " kg on " + date);
        } else {
            showError("Failed to save weight entry. Please try again.");
            System.err.println("✗ Failed to save weight history to database");
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
                System.out.println("DEBUG: Deleting weight entry with ID: " + selectedEntry.getId());
                
                boolean success = dbManager.deleteWeightHistory(selectedEntry.getId());
                
                if (success) {
                    System.out.println("✓ Weight entry deleted from database with ID: " + selectedEntry.getId());
                    
                    // Reload from database to ensure data is current
                    loadWeightHistory();
                    
                    // Update UI
                    updateChart();
                    updateStats();
                    
                    showSuccess("Entry deleted successfully!");
                } else {
                    showError("Failed to delete weight entry. Please try again.");
                    System.err.println("✗ Failed to delete weight history from database");
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
