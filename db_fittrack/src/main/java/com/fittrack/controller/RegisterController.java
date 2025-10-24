package com.fittrack.controller;

import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

/**
 * RegisterController - Controller for the Register.fxml view
 * Handles user registration with mock data (no database required)
 */
public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    // Database manager would go here (commented out until database is ready)
    // private DatabaseManager dbManager = new DatabaseManager();

    /**
     * Initialize method called when the FXML is loaded
     */
    @FXML
    public void initialize() {
        messageLabel.setText("");
    }

    /**
     * Handle the Register button click
     * Uses MOCK DATA for testing without database
     */
    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // --- Input Validation ---
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (username.length() < 3) {
            showError("Username must be at least 3 characters long.");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        // --- THIS IS YOUR MOCK LOGIC ---
        // Simulate successful registration without database
        
        System.out.println("✓ Mock Registration Successful!");
        System.out.println("  Username: " + username);
        System.out.println("  Email: " + email);
        
        // Show success message
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText("✓ Account created successfully! Redirecting to login...");
        
        // Create a mock user object (just for demonstration)
        User mockUser = new User(username, email, "hashed_" + password);
        mockUser.setUserId(100); // Mock ID
        mockUser.setCreatedAt(LocalDate.now());
        
        System.out.println("  Created user: " + mockUser);
        
        // Wait a moment then redirect to login
        try {
            Thread.sleep(1500); // Brief pause so user sees success message
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            showError("Error loading login screen.");
            System.err.println("✗ Error: " + e.getMessage());
        }
        
        // --- THIS IS YOUR REAL LOGIC (Commented out) ---
        // You will add this back AFTER the SQL problem is fixed.
        
        /*
        try {
            // Check if username already exists
            if (dbManager.usernameExists(username)) {
                showError("Username already taken. Please choose another.");
                return;
            }
            
            // Check if email already exists
            if (dbManager.emailExists(email)) {
                showError("Email already registered. Please use another email or login.");
                return;
            }
            
            // Hash the password
            String passwordHash = PasswordHasher.hash(password);
            
            // Create new user
            User newUser = new User(username, email, passwordHash);
            boolean success = dbManager.registerUser(newUser);
            
            if (success) {
                messageLabel.setStyle("-fx-text-fill: green;");
                messageLabel.setText("Account created successfully! Redirecting to login...");
                
                // Wait briefly then redirect
                Thread.sleep(1500);
                SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
            } else {
                showError("Registration failed. Please try again.");
            }
            
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showError("An error occurred during registration.");
            e.printStackTrace();
        }
        */
    }

    /**
     * Handle the Login link click
     * Navigate back to the login screen
     */
    @FXML
    private void handleLoginLinkAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (IOException e) {
            showError("Error loading login screen.");
            System.err.println("✗ Error: " + e.getMessage());
        }
    }

    /**
     * Helper method to display error messages
     */
    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }
}
