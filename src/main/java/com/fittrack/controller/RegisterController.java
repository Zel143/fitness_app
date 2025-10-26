package com.fittrack.controller;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    private final DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Please fill in all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Passwords do not match.");
            return;
        }
        if (password.length() < 6) {
            messageLabel.setStyle("-fx-text-fill: red;");
            messageLabel.setText("Password must be at least 6 characters.");
            return;
        }

        // Create user
        User newUser = User.create(username, email);

        // Register in database
        boolean success = dbManager.register(newUser, password);

        if (success) {
            showSuccess("Registration successful! Redirecting to login...");
            // Use JavaFX animation timeline for delay instead of Thread.sleep
            javafx.animation.PauseTransition delay = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(1.5));
            delay.setOnFinished(e -> {
                try {
                    SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showError("Error: Could not redirect to login.");
                }
            });
            delay.play();
        } else {
            showError("Registration failed! Username or email may already exist.");
        }
    }

    @FXML
    private void handleLoginLinkAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Login.fxml", "FitTrack - Login");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper method to display error messages
     */
    private void showError(String message) {
        messageLabel.setStyle("-fx-text-fill: red;");
        messageLabel.setText(message);
    }

    /**
     * Helper method to display success messages
     */
    private void showSuccess(String message) {
        messageLabel.setStyle("-fx-text-fill: green;");
        messageLabel.setText(message);
    }
}