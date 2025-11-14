package com.fittrack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fittrack.model.DatabaseManager;

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

    private static final Logger logger = LoggerFactory.getLogger(FitTrackApp.class);

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Initialize database on startup
        DatabaseManager dbManager = new DatabaseManager();
        dbManager.createTables();
        
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
        logger.info("✓ FitTrack Application Started");
        logger.info("✓ Using SQLite DATABASE (File-based)");
        logger.info("✓ Database location: fittrack.db (in project folder)");
        logger.info("ℹ Register a new account or login with existing credentials");
    }

    @Override
    public void stop() {
        logger.info("✓ FitTrack Application Closed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
