package com.fittrack.controller;

import java.io.IOException;
import java.time.LocalDate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import javafx.scene.layout.Region; // <-- 1. IMPORT ADDED

/**
 * FoodLogController - Controller for the FoodLog.fxml view
 * Tracks daily food intake and calculates nutrition totals
 */
public class FoodLogController {

    private static final Logger logger = LoggerFactory.getLogger(FoodLogController.class);

    @FXML private Label welcomeLabel;
    @FXML private Label userLabel;
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

    @FXML
    public void initialize() {
        // *** 2. LINE ADDED TO FIX LAYOUT ***
        dailyTotalsLabel.setMinHeight(Region.USE_PREF_SIZE);

        currentUser = SessionManager.getInstance().getLoggedInUser();

        if (currentUser != null) {
            welcomeLabel.setText(currentUser.getUsername() + "'s Food Log");
            userLabel.setText("Welcome, " + currentUser.getUsername() + "!");
            logger.info("✓ FoodLog screen loaded for: {}", currentUser.getUsername());
            setupTableColumns();
            loadFoodLog();
            updateDailyTotals();
        } else {
            welcomeLabel.setText("Food Log");
            logger.warn("⚠ Warning: No user logged in");
        }

        datePicker.setValue(LocalDate.now());
        
        // Listen for date changes to update totals automatically
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                updateDailyTotals();
            }
        });
        
        // Listen for changes in the food log list to update totals
        foodLogList.addListener((javafx.collections.ListChangeListener.Change<? extends FoodLog> change) -> {
            updateDailyTotals();
        });
    }

    private void setupTableColumns() {
        foodNameColumn.setCellValueFactory(new PropertyValueFactory<>("foodName"));
        caloriesColumn.setCellValueFactory(new PropertyValueFactory<>("calories"));
        proteinColumn.setCellValueFactory(new PropertyValueFactory<>("protein"));
        carbsColumn.setCellValueFactory(new PropertyValueFactory<>("carbs"));
        fatsColumn.setCellValueFactory(new PropertyValueFactory<>("fats"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        foodLogTable.setItems(foodLogList);
    }

    private void loadFoodLog() {
        if (currentUser == null) return;

        foodLogList.clear();
        var logs = dbManager.getFoodLog(currentUser.getUserId(), null);
        foodLogList.addAll(logs);

        logger.info("✓ Loaded {} food log entries from database", logs.size());
    }

    private void updateDailyTotals() {
        LocalDate selectedDate = datePicker.getValue();
        if (selectedDate == null) selectedDate = LocalDate.now();

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

        // Build totals text with proper line breaks
        StringBuilder totals = new StringBuilder();
        totals.append(String.format("Daily Totals for %s:%n", selectedDate));
        totals.append(String.format("Calories: %d kcal%n", totalCalories));
        totals.append(String.format("Protein: %.1f g | Carbs: %.1f g | Fats: %.1f g",
                totalProtein, totalCarbs, totalFats));

        String totalsText = totals.toString();
        dailyTotalsLabel.setText(totalsText);
        
        // Force the label to wrap text and update its layout
        dailyTotalsLabel.setWrapText(true);
        
        logger.info("✓ Updated totals: {}", totalsText.replace("\n", " | "));
    }

    @FXML
    private void handleAddFoodButtonAction() {
        String foodName = foodNameField.getText().trim();
        String caloriesStr = caloriesField.getText().trim();
        String proteinStr = proteinField.getText().trim();
        String carbsStr = carbsField.getText().trim();
        String fatsStr = fatsField.getText().trim();
        LocalDate date = datePicker.getValue();

        if (foodName.isEmpty()) { showError("Please enter food name"); return; }
        if (caloriesStr.isEmpty()) { showError("Please enter calories"); return; }

        int calories;
        try {
            calories = Integer.parseInt(caloriesStr);
            if (calories < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            showError("Calories must be a valid positive number");
            return;
        }

        double protein = parseDoubleOrDefault(proteinStr);
        double carbs = parseDoubleOrDefault(carbsStr);
        double fats = parseDoubleOrDefault(fatsStr);

        if (date == null) { showError("Please select a date"); return; }

        FoodLog newFood = new FoodLog(
            currentUser.getUserId(),
            foodName,
            calories,
            protein,
            carbs,
            fats,
            date
        );

        boolean success = dbManager.saveFoodLog(newFood);
        if (success) {
            loadFoodLog();
            updateDailyTotals();
            clearForm();
            showSuccess("Food logged successfully!");
        } else {
            showError("Failed to save food entry. Please try again.");
        }
    }

    private double parseDoubleOrDefault(String input) {
        if (input.isEmpty()) return 0;
        try {
            double val = Double.parseDouble(input);
            return val >= 0 ? val : 0;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

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
                boolean success = dbManager.deleteFoodLog(selectedFood.getId());
                if (success) {
                    loadFoodLog();
                    updateDailyTotals();
                    showSuccess("Food entry deleted successfully!");
                } else {
                    showError("Failed to delete food entry. Please try again.");
                }
            }
        });
    }

    @FXML
    private void handleViewDateButtonAction() {
        updateDailyTotals();
    }

    /**
     * Handle Dashboard button click
     */
    @FXML
    private void handleDashboardButtonAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Dashboard.fxml", "FitTrack - Dashboard");
        } catch (IOException e) {
            logger.error("✗ Error loading Dashboard", e);
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
            logger.error("✗ Error loading Goals", e);
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
            logger.error("✗ Error loading Workouts", e);
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
            logger.error("✗ Error loading Progress", e);
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
            logger.error("✗ Error loading Profile", e);
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

    private void clearForm() {
        foodNameField.clear();
        caloriesField.clear();
        proteinField.clear();
        carbsField.clear();
        fatsField.clear();
        datePicker.setValue(LocalDate.now());
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }
}