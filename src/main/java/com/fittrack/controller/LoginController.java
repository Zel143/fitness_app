package com.fittrack.controller;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.util.SceneSwitcher;
import com.fittrack.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        // REAL LOGIC - Uses database
        User user = dbManager.login(username, password);

        if (user != null) {
            SessionManager.getInstance().setLoggedInUser(user);
            try {
                SceneSwitcher.switchScene(event, "Dashboard.fxml");
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Error: Could not load dashboard.");
            }
        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    // Handles the "Register here" link click
    @FXML
    private void handleRegisterLinkAction(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "Register.fxml", "FitTrack - Register");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error: Could not load registration page.");
        }
    }
}