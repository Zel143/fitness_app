package com.fittrack.util;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.WeightHistory;
import com.fittrack.model.WorkoutLog;
import com.fittrack.model.User;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Utility class to export user data to CSV.
 */
public class DataExporter {

    private final DatabaseManager dbManager;

    public DataExporter(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    /**
     * Exports the user's data (Weight History and Workout Logs) to a CSV file.
     *
     * @param user The user whose data to export.
     * @param file The file to write the CSV data to.
     * @return true if export is successful, false otherwise.
     */
    public boolean exportUserData(User user, File file) {
        if (user == null || file == null) {
            return false;
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
            List<WeightHistory> weightHistory = dbManager.getWeightHistory(user.getUserId());
            List<WorkoutLog> workoutLogs = dbManager.getWorkoutLogs(user.getUserId());
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

            writer.println("=== FitTrack User Data Export ===");
            writer.println("User: " + user.getUsername());
            writer.println("Date: " + java.time.LocalDate.now());
            writer.println();

            // Export Weight History
            writer.println("=== Weight History ===");
            writer.println("Date,Weight (kg)");
            for (WeightHistory entry : weightHistory) {
                writer.printf("%s,%.2f%n",
                    entry.getDate().format(formatter),
                    entry.getWeight());
            }
            writer.println();

            // Export Workout Logs
            writer.println("=== Workout Logs ===");
            writer.println("Date,Workout/Exercise,Sets,Reps,Weight");
            for (WorkoutLog log : workoutLogs) {
                writer.printf("%s,%s,%d,%d,%.2f%n",
                    log.getDate().format(formatter),
                    escapeCsv(log.getWorkoutName()),
                    log.getSets(),
                    log.getReps(),
                    log.getWeightUsed());
            }

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String escapeCsv(String value) {
        if (value == null) return "";
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
