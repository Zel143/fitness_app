package com.fittrack.controller;

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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;

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

        // Mock data - In real implementation, load from database
        weightHistoryList.add(new WeightHistory(1, currentUser.getUserId(), 85.5, LocalDate.now().minusDays(30)));
        weightHistoryList.add(new WeightHistory(2, currentUser.getUserId(), 84.2, LocalDate.now().minusDays(23)));
        weightHistoryList.add(new WeightHistory(3, currentUser.getUserId(), 83.8, LocalDate.now().minusDays(16)));
        weightHistoryList.add(new WeightHistory(4, currentUser.getUserId(), 82.5, LocalDate.now().minusDays(9)));
        weightHistoryList.add(new WeightHistory(5, currentUser.getUserId(), 81.9, LocalDate.now().minusDays(2)));

        // Add current weight if available
        if (currentUser.getWeight() != null) {
            weightHistoryList.add(new WeightHistory(6, currentUser.getUserId(), currentUser.getWeight(), LocalDate.now()));
        }
    }

    /**
     * Update the weight chart
     */
    private void updateChart() {
        weightChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Weight (kg)");

        for (WeightHistory wh : weightHistoryList) {
            series.getData().add(new XYChart.Data<>(wh.getDate().toString(), wh.getWeight()));
        }

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

        double currentWeight = weightHistoryList.get(weightHistoryList.size() - 1).getWeight();
        double startWeight = weightHistoryList.get(0).getWeight();
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
            weightHistoryList.size() + 1,
            currentUser.getUserId(),
            weight,
            date
        );

        // Add to list (in real implementation, save to database)
        weightHistoryList.add(newEntry);
        showSuccess("Weight recorded successfully!");
        
        // Update chart and stats
        updateChart();
        updateStats();
        
        // Clear form
        weightField.clear();
        datePicker.setValue(LocalDate.now());
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
                weightHistoryList.remove(selectedEntry);
                updateChart();
                updateStats();
                showSuccess("Entry deleted successfully!");
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
