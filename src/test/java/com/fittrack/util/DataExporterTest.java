package com.fittrack.util;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.User;
import com.fittrack.model.WeightHistory;
import com.fittrack.model.WorkoutLog;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class DataExporterTest {

    private DatabaseManager dbManager;
    private DataExporter exporter;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        String dbUrl = "jdbc:sqlite:" + tempDir.resolve("test_export.db").toAbsolutePath();
        dbManager = new DatabaseManager(dbUrl);
        dbManager.createTables();
        exporter = new DataExporter(dbManager);
    }

    @Test
    void testExportUserData() throws Exception {
        // Setup user
        User user = new User();
        user.setUsername("exportuser");
        user.setEmail("export@test.com");
        dbManager.register(user, "password");
        User savedUser = dbManager.login("exportuser", "password");

        // Add some data
        dbManager.saveWeightHistory(new WeightHistory(savedUser.getUserId(), 75.0, LocalDate.now()));
        WorkoutLog log = new WorkoutLog();
        log.setUserId(savedUser.getUserId());
        log.setWorkoutName("Bench Press");
        log.setSets(3);
        log.setReps(10);
        log.setWeightUsed(60.0);
        log.setDate(LocalDate.now());
        dbManager.saveWorkoutLog(log);

        // Export
        File exportFile = tempDir.resolve("export.csv").toFile();
        boolean result = exporter.exportUserData(savedUser, exportFile);

        assertTrue(result, "Export should succeed");
        assertTrue(exportFile.exists(), "Export file should exist");

        List<String> lines = Files.readAllLines(exportFile.toPath());
        assertFalse(lines.isEmpty(), "Export file should not be empty");

        // Basic check for content
        boolean foundUser = lines.stream().anyMatch(line -> line.contains("User: exportuser"));
        boolean foundWeight = lines.stream().anyMatch(line -> line.contains("75.00"));
        boolean foundWorkout = lines.stream().anyMatch(line -> line.contains("Bench Press"));

        assertTrue(foundUser, "Should contain username");
        assertTrue(foundWeight, "Should contain weight data");
        assertTrue(foundWorkout, "Should contain workout data");
    }
}
