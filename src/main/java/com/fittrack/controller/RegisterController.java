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

    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Label errorLabel;

    private DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleRegisterButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic validation
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill in all fields.");
            return;
        }
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match.");
            return;
        }

        // Create user object
        User newUser = new User();
        newUser.username = username;
        newUser.email = email;

        // Attempt to register
        boolean success = dbManager.register(newUser, password);

        if (success) {
            // Registration successful, switch to login
            try {
                // You could add a success message on the login page
                SceneSwitcher.switchScene(event, "/com/fittrack/view/Login.fxml");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            // Registration failed (e.g., username/email already exists)
            errorLabel.setText("Registration failed. Username or email may already be in use.");
        }
    }

    @FXML
    private void openLoginScene(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "/com/fittrack/view/Login.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
