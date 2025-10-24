package com.fittrack.controller;

import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * LoginController - Controller for the Login.fxml view
 * Handles user login with mock data (no database required)
 */
public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    // Database manager would go here (commented out until database is ready)
    // private DatabaseManager dbManager = new DatabaseManager();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        // Clear any previous error messages
        errorLabel.setText("");
    }

    /**
     * Handle the Login button click
     * Uses MOCK DATA for testing without database
     */
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Please enter both username and password.");
            return;
        }

        // --- THIS IS YOUR MOCK LOGIC ---
        // Test your UI without a database
        // You can use username: "test" and password: "test" to login
        
        if (username.equals("test") && password.equals("test")) {
            
            System.out.println("✓ Mock Login Successful!");
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Success! Logging in...");

            // Create a "fake" user to test your SessionManager
            User fakeUser = new User();
            fakeUser.setUserId(1);
            fakeUser.setUsername("test");
            fakeUser.setEmail("test@fittrack.com");
            fakeUser.setAge(25);
            fakeUser.setGender("Other");
            fakeUser.setHeight(175.0);
            fakeUser.setWeight(70.0);
            fakeUser.setFitnessLevel("Intermediate");
            
            // Set the user in the SessionManager
            SessionManager.getInstance().setLoggedInUser(fakeUser);
            
            // Test your SceneSwitcher - navigate to Dashboard
            try {
                SceneSwitcher.switchScene(event, "Dashboard.fxml", "FitTrack - Dashboard");
                System.out.println("✓ Navigated to Dashboard");
            } catch (IOException e) {
                System.err.println("✗ Error loading Dashboard: " + e.getMessage());
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Error loading dashboard. Check console for details.");
            }

        } else {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Invalid username or password. (Try: test/test)");
        }
        
        // --- THIS IS YOUR REAL LOGIC (Commented out) ---
        // You will add this back AFTER the SQL problem is fixed.
        
        /*
        try {
            User user = dbManager.login(username, password);
            if (user != null) {
                SessionManager.getInstance().setLoggedInUser(user);
                SceneSwitcher.switchScene(event, "Dashboard.fxml", "FitTrack - Dashboard");
            } else {
                errorLabel.setStyle("-fx-text-fill: red;");
                errorLabel.setText("Invalid username or password.");
            }
        } catch (SQLException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Error loading dashboard.");
            e.printStackTrace();
        }
        */
    }

    /**
     * Handle the Register link click
     * Navigate to the registration screen
     */
    @FXML
    private void handleRegisterLinkAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Register.fxml", "FitTrack - Register");
        } catch (IOException e) {
            System.err.println("✗ Error loading Register screen: " + e.getMessage());
            errorLabel.setStyle("-fx-text-fill: red;");
            errorLabel.setText("Error loading registration screen.");
        }
    }
}
