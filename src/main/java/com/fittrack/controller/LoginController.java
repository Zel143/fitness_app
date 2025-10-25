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

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private DatabaseManager dbManager = new DatabaseManager();

    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            return;
        }

        User user = dbManager.login(username, password);

        if (user != null) {
            // Login successful
            SessionManager.getInstance().setLoggedInUser(user);
            try {
                SceneSwitcher.switchScene(event, "/com/fittrack/view/Dashboard.fxml");
            } catch (Exception e) {
                e.printStackTrace();
                errorLabel.setText("Error: Could not load dashboard.");
            }
        } else {
            // Login failed
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void openRegisterScene(ActionEvent event) {
        try {
            SceneSwitcher.switchScene(event, "/com/fittrack/view/Register.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
