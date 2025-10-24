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

        // Create the scene
        Scene scene = new Scene(root, 400, 500);

        // Configure the stage
        primaryStage.setTitle("FitTrack - Login");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        // Console output
        System.out.println("✓ FitTrack Application Started");
        System.out.println("ℹ Using mock data (no database connection required)");
        System.out.println("ℹ Test login: username = 'test', password = 'test'");
    }

    @Override
    public void stop() {
        System.out.println("✓ FitTrack Application Closed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
