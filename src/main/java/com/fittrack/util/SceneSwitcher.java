package com.fittrack.util;

import java.io.IOException;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * SceneSwitcher - Utility class for switching between FXML scenes
 * This class handles navigation between different views in the application
 */
public class SceneSwitcher {
    
    /**
     * Switch to a new scene
     * @param event the Event from the button click or mouse click
     * @param fxmlFileName the name of the FXML file (e.g., "Dashboard.fxml")
     * @throws IOException if the FXML file cannot be loaded
     */
    public static void switchScene(Event event, String fxmlFileName) throws IOException {
        // Build the full path to the FXML file
        String fxmlPath = "/com/fittrack/view/" + fxmlFileName;
        
        // Load the FXML file
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        // Get the current stage from the event source
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        
        // Create a new scene
        Scene scene = new Scene(root);
        
        // Set the scene and show it
        stage.setScene(scene);
        stage.show();
    }
    
    /**
     * Switch to a new scene with a specific title
     * @param event the Event from the button click or mouse click
     * @param fxmlFileName the name of the FXML file
     * @param title the title to set for the stage
     * @throws IOException if the FXML file cannot be loaded
     */
    public static void switchScene(Event event, String fxmlFileName, String title) throws IOException {
        switchScene(event, fxmlFileName);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setTitle(title);
    }
    
    /**
     * Get the controller from a loaded FXML file
     * This is useful when you need to pass data to the next controller
     * 
     * @param event the Event from the button click or mouse click
     * @param fxmlFileName the name of the FXML file
     * @return the FXMLLoader instance (call getController() on it)
     * @throws IOException if the FXML file cannot be loaded
     */
    public static FXMLLoader switchSceneAndGetController(Event event, String fxmlFileName) throws IOException {
        String fxmlPath = "/com/fittrack/view/" + fxmlFileName;
        FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource(fxmlPath));
        Parent root = loader.load();
        
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        
        return loader;
    }
}
