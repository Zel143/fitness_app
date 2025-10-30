package com.fittrack;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main Application class for FitTrack
 * Entry point for the JavaFX application
 */
public class FitTrackApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the Login.fxml file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/fittrack/view/Login.fxml"));
        Parent root = loader.load();

        // Create the scene with larger dimensions
        Scene scene = new Scene(root, 800, 600);

        // Configure the stage
        primaryStage.setTitle("FitTrack - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);  // Allow resizing
        primaryStage.setMinWidth(600);    // Minimum width
        primaryStage.setMinHeight(500);   // Minimum height
        primaryStage.show();

        // Console output
        System.out.println("✓ FitTrack Application Started");
        System.out.println("✓ Using REAL DATABASE (MySQL)");
        System.out.println("ℹ Register a new account or login with existing credentials");
    }

    @Override
    public void stop() {
        System.out.println("✓ FitTrack Application Closed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
