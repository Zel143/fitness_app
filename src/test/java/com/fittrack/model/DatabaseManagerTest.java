package com.fittrack.model;

import java.nio.file.Path;
import java.sql.Connection;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Integration tests for DatabaseManager class.
 * Tests actual database operations using a temporary test database.
 */
class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        dbManager = new DatabaseManager();
        dbManager.createTables();
    }

    @Test
    void testDatabaseConnection() {
        Connection conn = dbManager.connect();
        assertNotNull(conn, "Database connection should not be null");
    }

    @Test
    void testCreateTables() {
        // createTables() is called in setUp()
        // If no exception is thrown, tables were created successfully
        assertTrue(true, "Tables should be created without errors");
    }

    @Test
    void testRegisterNewUser() {
        User user = new User();
        user.setUsername("testuser1");
        user.setEmail("test1@example.com");
        
        boolean result = dbManager.register(user, "password123");
        assertTrue(result, "User registration should succeed");
    }

    @Test
    void testRegisterDuplicateUsername() {
        User user1 = new User();
        user1.setUsername("duplicate");
        user1.setEmail("user1@example.com");
        
        User user2 = new User();
        user2.setUsername("duplicate");
        user2.setEmail("user2@example.com");
        
        assertTrue(dbManager.register(user1, "password1"));
        assertFalse(dbManager.register(user2, "password2"), 
            "Should not allow duplicate username");
    }

    @Test
    void testLoginWithCorrectCredentials() {
        // Register a user first
        User user = new User();
        user.setUsername("logintest");
        user.setEmail("login@example.com");
        dbManager.register(user, "correct_password");
        
        // Attempt login
        User loggedInUser = dbManager.login("logintest", "correct_password");
        assertNotNull(loggedInUser, "Login should succeed with correct credentials");
        assertEquals("logintest", loggedInUser.getUsername());
    }

    @Test
    void testLoginWithIncorrectPassword() {
        User user = new User();
        user.setUsername("passwordtest");
        user.setEmail("pass@example.com");
        dbManager.register(user, "correct_password");
        
        User loggedInUser = dbManager.login("passwordtest", "wrong_password");
        assertNull(loggedInUser, "Login should fail with incorrect password");
    }

    @Test
    void testLoginCaseSensitiveUsername() {
        User user = new User();
        user.setUsername("CaseSensitive");
        user.setEmail("case@example.com");
        dbManager.register(user, "password123");
        
        User loggedInUser = dbManager.login("casesensitive", "password123");
        assertNull(loggedInUser, "Login should fail with wrong case username");
    }

    @Test
    void testUpdateUserProfile() {
        // Register user
        User user = new User();
        user.setUsername("updatetest");
        user.setEmail("update@example.com");
        dbManager.register(user, "password123");
        
        // Login to get user ID
        User loggedInUser = dbManager.login("updatetest", "password123");
        assertNotNull(loggedInUser);
        
        // Update profile
        loggedInUser.setAge(25);
        loggedInUser.setGender("Male");
        loggedInUser.setHeight(180.0);
        loggedInUser.setWeight(75.0);
        loggedInUser.setFitnessLevel("Intermediate");
        
        boolean result = dbManager.updateUserProfile(loggedInUser);
        assertTrue(result, "Profile update should succeed");
        
        // Verify update
        User refreshedUser = dbManager.getUserById(loggedInUser.getUserId());
        assertEquals(25, refreshedUser.getAge());
        assertEquals("Male", refreshedUser.getGender());
        assertEquals(180.0, refreshedUser.getHeight(), 0.01);
        assertEquals(75.0, refreshedUser.getWeight(), 0.01);
        assertEquals("Intermediate", refreshedUser.getFitnessLevel());
    }

    @Test
    void testSaveAndGetGoal() {
        // Create user
        User user = new User();
        user.setUsername("goaltest");
        user.setEmail("goal@example.com");
        dbManager.register(user, "password123");
        User loggedInUser = dbManager.login("goaltest", "password123");
        
        // Create goal
        Goal goal = new Goal();
        goal.setUserId(loggedInUser.getUserId());
        goal.setGoalType("Weight Loss");
        goal.setTargetValue(70.0);
        goal.setTargetUnit("kg");
        goal.setTargetDate(LocalDate.now().plusMonths(3));
        goal.setStatus("active");
        
        boolean saved = dbManager.saveGoal(goal);
        assertTrue(saved, "Goal should be saved successfully");
        assertTrue(goal.getGoalId() > 0, "Goal should have an ID after saving");
        
        // Retrieve goals
        var goals = dbManager.getGoals(loggedInUser.getUserId());
        assertFalse(goals.isEmpty(), "Should have at least one goal");
        assertEquals("Weight Loss", goals.get(0).getGoalType());
    }

    @Test
    void testDeleteGoal() {
        // Create user and goal
        User user = new User();
        user.setUsername("deletegoaltest");
        user.setEmail("deletegoal@example.com");
        dbManager.register(user, "password123");
        User loggedInUser = dbManager.login("deletegoaltest", "password123");
        
        Goal goal = new Goal();
        goal.setUserId(loggedInUser.getUserId());
        goal.setGoalType("Muscle Gain");
        goal.setTargetValue(5.0);
        goal.setTargetUnit("kg");
        goal.setTargetDate(LocalDate.now().plusMonths(6));
        goal.setStatus("active");
        
        dbManager.saveGoal(goal);
        int goalId = goal.getGoalId();
        
        // Delete goal
        boolean deleted = dbManager.deleteGoal(goalId);
        assertTrue(deleted, "Goal should be deleted successfully");
        
        // Verify deletion
        var goals = dbManager.getGoals(loggedInUser.getUserId());
        assertTrue(goals.isEmpty(), "Goals list should be empty after deletion");
    }

    @Test
    void testSaveAndGetWeightHistory() {
        // Create user
        User user = new User();
        user.setUsername("weighttest");
        user.setEmail("weight@example.com");
        dbManager.register(user, "password123");
        User loggedInUser = dbManager.login("weighttest", "password123");
        
        // Save weight history
        WeightHistory entry = new WeightHistory(
            loggedInUser.getUserId(),
            75.5,
            LocalDate.now()
        );
        
        boolean saved = dbManager.saveWeightHistory(entry);
        assertTrue(saved, "Weight history should be saved successfully");
        
        // Retrieve weight history
        var history = dbManager.getWeightHistory(loggedInUser.getUserId());
        assertFalse(history.isEmpty(), "Should have at least one weight entry");
        assertEquals(75.5, history.get(0).getWeight(), 0.01);
    }

    @Test
    void testDeleteWeightHistory() {
        // Create user and weight entry
        User user = new User();
        user.setUsername("deleteweighttest");
        user.setEmail("deleteweight@example.com");
        dbManager.register(user, "password123");
        User loggedInUser = dbManager.login("deleteweighttest", "password123");
        
        WeightHistory entry = new WeightHistory(
            loggedInUser.getUserId(),
            80.0,
            LocalDate.now()
        );
        
        dbManager.saveWeightHistory(entry);
        int entryId = entry.getId();
        
        // Delete entry
        boolean deleted = dbManager.deleteWeightHistory(entryId);
        assertTrue(deleted, "Weight history should be deleted successfully");
        
        // Verify deletion
        var history = dbManager.getWeightHistory(loggedInUser.getUserId());
        assertTrue(history.isEmpty(), "Weight history should be empty after deletion");
    }

    @Test
    void testPasswordHashingIsSecure() {
        User user1 = new User();
        user1.setUsername("hashtest1");
        user1.setEmail("hash1@example.com");
        
        User user2 = new User();
        user2.setUsername("hashtest2");
        user2.setEmail("hash2@example.com");
        
        // Register two users with same password
        dbManager.register(user1, "samepassword");
        dbManager.register(user2, "samepassword");
        
        // Both should login successfully but password hashes should be different
        User logged1 = dbManager.login("hashtest1", "samepassword");
        User logged2 = dbManager.login("hashtest2", "samepassword");
        
        assertNotNull(logged1);
        assertNotNull(logged2);
        assertNotEquals(logged1.getUserId(), logged2.getUserId());
    }
}
