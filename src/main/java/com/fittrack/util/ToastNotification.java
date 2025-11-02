package com.fittrack.util;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

/**
 * Toast notification utility for displaying temporary messages
 * Provides visual feedback for user actions
 */
public class ToastNotification {
    
    public enum Type {
        SUCCESS, ERROR, INFO, WARNING
    }
    
    /**
     * Display a toast notification
     * @param ownerStage The parent stage
     * @param message The message to display
     * @param type The type of notification (SUCCESS, ERROR, INFO, WARNING)
     * @param durationMs How long to show the toast (in milliseconds)
     */
    public static void show(Stage ownerStage, String message, Type type, int durationMs) {
        Stage toastStage = new Stage();
        toastStage.initOwner(ownerStage);
        toastStage.setResizable(false);
        toastStage.initStyle(StageStyle.TRANSPARENT);

        Label toastLabel = new Label(message);
        toastLabel.setStyle(getStyleForType(type));
        toastLabel.setMinWidth(300);
        toastLabel.setPrefHeight(50);
        toastLabel.setAlignment(Pos.CENTER);
        toastLabel.setPadding(new javafx.geometry.Insets(10, 20, 10, 20));

        StackPane root = new StackPane(toastLabel);
        root.setStyle("-fx-background-radius: 10; -fx-background-color: transparent;");
        
        Scene scene = new Scene(root);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        toastStage.setScene(scene);
        
        // Position at bottom center of owner stage
        toastStage.setX(ownerStage.getX() + ownerStage.getWidth() / 2 - 150);
        toastStage.setY(ownerStage.getY() + ownerStage.getHeight() - 100);

        toastStage.show();

        // Fade in and out animation
        Timeline fadeIn = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(toastStage.opacityProperty(), 0)),
            new KeyFrame(Duration.millis(300), new KeyValue(toastStage.opacityProperty(), 1))
        );
        
        Timeline fadeOut = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(toastStage.opacityProperty(), 1)),
            new KeyFrame(Duration.millis(300), new KeyValue(toastStage.opacityProperty(), 0))
        );
        
        fadeOut.setOnFinished(e -> toastStage.close());

        fadeIn.play();
        fadeIn.setOnFinished(e -> {
            Timeline pause = new Timeline(new KeyFrame(Duration.millis(durationMs)));
            pause.setOnFinished(evt -> fadeOut.play());
            pause.play();
        });
    }
    
    /**
     * Show success toast (green) - 2 second duration
     */
    public static void showSuccess(Stage ownerStage, String message) {
        show(ownerStage, "✓ " + message, Type.SUCCESS, 2000);
    }
    
    /**
     * Show error toast (red) - 3 second duration
     */
    public static void showError(Stage ownerStage, String message) {
        show(ownerStage, "✗ " + message, Type.ERROR, 3000);
    }
    
    /**
     * Show info toast (blue) - 2 second duration
     */
    public static void showInfo(Stage ownerStage, String message) {
        show(ownerStage, "ℹ " + message, Type.INFO, 2000);
    }
    
    /**
     * Show warning toast (orange) - 2.5 second duration
     */
    public static void showWarning(Stage ownerStage, String message) {
        show(ownerStage, "⚠ " + message, Type.WARNING, 2500);
    }
    
    private static String getStyleForType(Type type) {
        switch (type) {
            case SUCCESS:
                return "-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                       "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);";
            case ERROR:
                return "-fx-background-color: #f44336; -fx-text-fill: white; " +
                       "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);";
            case WARNING:
                return "-fx-background-color: #FF9800; -fx-text-fill: white; " +
                       "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);";
            case INFO:
            default:
                return "-fx-background-color: #2196F3; -fx-text-fill: white; " +
                       "-fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; " +
                       "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 2);";
        }
    }
}
