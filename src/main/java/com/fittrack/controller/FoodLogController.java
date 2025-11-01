package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.FoodLog;
import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FoodLogController - Controller for the FoodLog.fxml view
 * Tracks daily food intake and calculates nutrition totals
 */
public class FoodLogController {

    @FXML private Label welcomeLabel;
    @FXML private TableView<FoodLog> foodLogTable;
    @FXML private TableColumn<FoodLog, String> foodNameColumn;
    @FXML private TableColumn<FoodLog, Integer> caloriesColumn;
    @FXML private TableColumn<FoodLog, Double> proteinColumn;
    @FXML private TableColumn<FoodLog, Double> carbsColumn;
    @FXML private TableColumn<FoodLog, Double> fatsColumn;
    @FXML private TableColumn<FoodLog, LocalDate> dateColumn;
    
    @FXML private TextField foodNameField;
    @FXML private TextField caloriesField;
    @FXML private TextField proteinField;
    @FXML private TextField carbsField;
    @FXML private TextField fatsField;
    @FXML private DatePicker datePicker;
    @FXML private Label messageLabel;
    @FXML private Label dailyTotalsLabel;

    private final DatabaseManager dbManager = new DatabaseManager();
    private User currentUser;
    private final ObservableList<FoodLog> foodLogList = FXCollections.observableArrayList();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            welcomeLabel.setText(currentUser.getUsername() + "'s Food Log");
            System.out.println("✓ FoodLog screen loaded for: " + currentUser.getUsername());
            setupTableColumns();
            loadFoodLog();
            updateDailyTotals();
        } else {
            welcomeLabel.setText("Food Log");
            System.out.println("⚠ Warning: No user logged in");
        }

        // Set today's date as default
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Setup table columns
     */
    private void setupTableColumns() {
        foodNameColumn.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        fatsColumn.setCellValueFactory(new PropertyValueFactory<>("fats"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        
        foodLogTable.setItems(foodLogList);
    }

    /**
     * Load food log from database
     */
    private void loadFoodLog() {
        if (currentUser == null) return;

        foodLogList.clear();
        var logs = dbManager.getFoodLog(currentUser.getUserId(), null);
        foodLogList.addAll(logs);
        
        System.out.println("✓ Loaded " + logs.size() + " food log entries from database");
    }

    /**
     * Update daily totals
     */
    private void updateDailyTotals() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) {
            selectedDate = LocalDate.now();
        }

        int totalCalories = 0;
        double totalProtein = 0;
        double totalCarbs = 0;
        double totalFats = 0;

        for (FoodLog food : foodLogList) {
            if (food.getDate().equals(selectedDate)) {
                totalCalories += food.getCalories();
                totalProtein += food.getProtein();
                totalCarbs += food.getCarbs();
                totalFats += food.getFats();
            }
        }

        StringBuilder totals = new StringBuilder();
        totals.append(String.format("Daily Totals for %s:\n", selectedDate));
        totals.append(String.format("Calories: %d kcal\n", totalCalories));
        totals.append(String.format("Protein: %.1f g | Carbs: %.1f g | Fats: %.1f g", totalProtein, totalCarbs, totalFats));

        dailyTotalsLabel.setText(totals.toString());
    }

    /**
     * Handle Add Food button click
     */
    @FXML
    private void handleAddFoodButtonAction() {
        // Get input values
        String foodName = foodNameField.getText().trim();
        String caloriesStr = caloriesField.getText().trim();
        String proteinStr = proteinField.getText().trim();
        String carbsStr = carbsField.getText().trim();
        String fatsStr = fatsField.getText().trim();
        LocalDate date = datePicker.getValue();

        System.out.println("ℹ Add Food button clicked");
        System.out.println("ℹ Food name: " + foodName);
        System.out.println("ℹ Creating FoodLog object...");
        System.out.println("ℹ Saving to database...");

        // Validation
        if (foodName.isEmpty()) {
            showError("Please enter food name");
            return;
        }

        if (caloriesStr.isEmpty()) {
            showError("Please enter calories");
            return;
        }

        // Parse calories
        int calories;
        try {
            calories = Integer.parseInt(caloriesStr);
            if (calories < 0) {
                showError("Calories must be a positive number");
                return;
            }
        } catch (NumberFormatException e) {
            showError("Calories must be a valid number");
            return;
        }

        // Parse protein, carbs, and fats (default to 0 if empty)
        double protein = 0;
        if (!proteinStr.isEmpty()) {
            try {
                protein = Double.parseDouble(proteinStr);
                if (protein < 0) {
                    showError("Protein must be a positive number");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Protein must be a valid number");
                return;
            }
        }

        double carbs = 0;
        if (!carbsStr.isEmpty()) {
            try {
                carbs = Double.parseDouble(carbsStr);
                if (carbs < 0) {
                    showError("Carbs must be a positive number");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Carbs must be a valid number");
                return;
            }
        }

        double fats = 0;
        if (!fatsStr.isEmpty()) {
            try {
                fats = Double.parseDouble(fatsStr);
                if (fats < 0) {
                    showError("Fats must be a positive number");
                    return;
                }
            } catch (NumberFormatException e) {
                showError("Fats must be a valid number");
                return;
            }
        }

        // Validate date
        if (date == null) {
            showError("Please select a date");
            return;
        }

        System.out.println("DEBUG: Creating FoodLog object...");

        // Create new food log entry
        FoodLog newFood = new FoodLog(
            currentUser.getUserId(),
            foodName,
            calories,
            protein,
            carbs,
            fats,
            date
        );

        System.out.println("DEBUG: Saving to database - User ID: " + currentUser.getUserId() + 
                         ", Food: " + foodName + ", Calories: " + calories);

        // Save to database
        boolean success = dbManager.saveFoodLog(newFood);

        if (success) {
            System.out.println("✓ Food entry saved to database with ID: " + newFood.getId());
            
            // Reload from database to get correct data with IDs
            loadFoodLog();
            
            // Update totals
            updateDailyTotals();
            
            // Clear form
            clearForm();
            
            // Show success message
            showSuccess("Food logged successfully!");
            
            System.out.println("✓ Food entry added to UI: " + foodName);
        } else {
            showError("Failed to save food entry. Please try again.");
            System.err.println("✗ Failed to save food log to database");
        }
    }

    /**
     * Handle Delete Food button click
     */
    @FXML
    private void handleDeleteFoodButtonAction() {
        FoodLog selectedFood = foodLogTable.getSelectionModel().getSelectedItem();
        
        if (selectedFood == null) {
            showError("Please select a food entry to delete");
            return;
        }

        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete Food Entry?");
        confirmAlert.setContentText("Delete " + selectedFood.getFoodName() + "?");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                System.out.println("DEBUG: Deleting food entry with ID: " + selectedFood.getId());
                
                boolean success = dbManager.deleteFoodLog(selectedFood.getId());
                
                if (success) {
                    System.out.println("✓ Food entry deleted from database with ID: " + selectedFood.getId());
                    
                    // Reload from database to ensure data is current
                    loadFoodLog();
                    
                    // Update totals
                    updateDailyTotals();
                    
                    showSuccess("Food entry deleted successfully!");
                } else {
                    showError("Failed to delete food entry. Please try again.");
                    System.err.println("✗ Failed to delete food log from database");
                }
            }
        });
    }

    /**
     * Handle View Date button click
     */
    @FXML
    private void handleViewDateButtonAction() {
        updateDailyTotals();
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
        foodNameField.clear();
        caloriesField.clear();
        proteinField.clear();
        carbsField.clear();
        fatsField.clear();
        datePicker.setValue(LocalDate.now());
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
