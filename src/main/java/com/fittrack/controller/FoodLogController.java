package com.fittrack.controller;

import com.fittrack.model.FoodLog;
import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.time.LocalDate;

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
     * Load food log (mock data for now)
     */
    private void loadFoodLog() {
        if (currentUser == null) return;

        // Mock data - In real implementation, load from database
        foodLogList.add(new FoodLog(1, currentUser.getUserId(), "Oatmeal with Banana", 350, 12.0, 58.0, 8.0, LocalDate.now()));
        foodLogList.add(new FoodLog(2, currentUser.getUserId(), "Grilled Chicken Salad", 420, 45.0, 25.0, 15.0, LocalDate.now()));
        foodLogList.add(new FoodLog(3, currentUser.getUserId(), "Protein Shake", 250, 30.0, 15.0, 5.0, LocalDate.now()));
        foodLogList.add(new FoodLog(4, currentUser.getUserId(), "Rice and Vegetables", 380, 10.0, 65.0, 8.0, LocalDate.now().minusDays(1)));
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
        String foodName = foodNameField.getText();
        String caloriesStr = caloriesField.getText();
        String proteinStr = proteinField.getText();
        String carbsStr = carbsField.getText();
        String fatsStr = fatsField.getText();
        LocalDate date = datePicker.getValue();

        // Validation
        if (foodName.isEmpty()) {
            showError("Please enter food name");
            return;
        }

        if (caloriesStr.isEmpty()) {
            showError("Please enter calories");
            return;
        }

        int calories;
        try {
            calories = Integer.parseInt(caloriesStr);
        } catch (NumberFormatException e) {
            showError("Calories must be a number");
            return;
        }

        double protein = parseDouble(proteinStr, "Protein");
        if (protein < 0) return;

        double carbs = parseDouble(carbsStr, "Carbs");
        if (carbs < 0) return;

        double fats = parseDouble(fatsStr, "Fats");
        if (fats < 0) return;

        if (date == null) {
            showError("Please select a date");
            return;
        }

        // Create new food log entry
        FoodLog newFood = new FoodLog(
            foodLogList.size() + 1,
            currentUser.getUserId(),
            foodName,
            calories,
            protein,
            carbs,
            fats,
            date
        );

        // Add to list (in real implementation, save to database)
        foodLogList.add(newFood);
        showSuccess("Food logged successfully!");
        
        // Update totals
        updateDailyTotals();
        
        // Clear form
        clearForm();
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
                foodLogList.remove(selectedFood);
                updateDailyTotals();
                showSuccess("Food entry deleted successfully!");
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
     * Parse double from string with error handling
     */
    private double parseDouble(String str, String fieldName) {
        if (str.isEmpty()) {
            return 0.0;
        }
        
        try {
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            showError(fieldName + " must be a number");
            return -1;
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
    }

    /**
     * Display success message
     */
    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }
}
