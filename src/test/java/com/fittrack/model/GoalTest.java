package com.fittrack.model;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Goal model class.
 * Tests all getters, setters, and data integrity.
 */
class GoalTest {

    private Goal goal;

    @BeforeEach
    void setUp() {
        goal = new Goal();
    }

    @Test
    void testGoalIdGetterSetter() {
        goal.setGoalId(1);
        assertEquals(1, goal.getGoalId(), "Goal ID should be 1");
    }

    @Test
    void testUserIdGetterSetter() {
        goal.setUserId(42);
        assertEquals(42, goal.getUserId(), "User ID should be 42");
    }

    @Test
    void testGoalTypeGetterSetter() {
        goal.setGoalType("Weight Loss");
        assertEquals("Weight Loss", goal.getGoalType(), "Goal type should be 'Weight Loss'");
    }

    @Test
    void testTargetValueGetterSetter() {
        goal.setTargetValue(75.5);
        assertEquals(75.5, goal.getTargetValue(), 0.01, "Target value should be 75.5");
    }

    @Test
    void testTargetValueCanBeNull() {
        goal.setTargetValue(null);
        assertNull(goal.getTargetValue(), "Target value should be able to be null");
    }

    @Test
    void testTargetUnitGetterSetter() {
        goal.setTargetUnit("kg");
        assertEquals("kg", goal.getTargetUnit(), "Target unit should be 'kg'");
    }

    @Test
    void testTargetDateGetterSetter() {
        LocalDate futureDate = LocalDate.now().plusMonths(3);
        goal.setTargetDate(futureDate);
        assertEquals(futureDate, goal.getTargetDate(), "Target date should match");
    }

    @Test
    void testTargetDateCanBeNull() {
        goal.setTargetDate(null);
        assertNull(goal.getTargetDate(), "Target date should be able to be null");
    }

    @Test
    void testStatusGetterSetter() {
        goal.setStatus("active");
        assertEquals("active", goal.getStatus(), "Status should be 'active'");
    }

    @Test
    void testCompleteGoal() {
        LocalDate targetDate = LocalDate.of(2025, 12, 31);
        
        goal.setGoalId(1);
        goal.setUserId(10);
        goal.setGoalType("Run Distance");
        goal.setTargetValue(10.0);
        goal.setTargetUnit("km");
        goal.setTargetDate(targetDate);
        goal.setStatus("active");

        assertEquals(1, goal.getGoalId());
        assertEquals(10, goal.getUserId());
        assertEquals("Run Distance", goal.getGoalType());
        assertEquals(10.0, goal.getTargetValue(), 0.01);
        assertEquals("km", goal.getTargetUnit());
        assertEquals(targetDate, goal.getTargetDate());
        assertEquals("active", goal.getStatus());
    }

    @Test
    void testGoalWithoutOptionalFields() {
        goal.setUserId(5);
        goal.setGoalType("Workout Frequency");
        goal.setStatus("active");

        assertEquals(5, goal.getUserId());
        assertEquals("Workout Frequency", goal.getGoalType());
        assertEquals("active", goal.getStatus());
        assertNull(goal.getTargetValue());
        assertNull(goal.getTargetDate());
    }

    @Test
    void testDifferentGoalTypes() {
        String[] goalTypes = {"Weight Loss", "Muscle Gain", "Run Distance", 
                             "Workout Frequency", "Body Fat %", "Strength Goal"};
        
        for (String type : goalTypes) {
            goal.setGoalType(type);
            assertEquals(type, goal.getGoalType(), "Goal type should be set correctly");
        }
    }

    @Test
    void testDifferentUnits() {
        String[] units = {"kg", "lbs", "km", "miles", "%", "reps"};
        
        for (String unit : units) {
            goal.setTargetUnit(unit);
            assertEquals(unit, goal.getTargetUnit(), "Target unit should be set correctly");
        }
    }

    @Test
    void testStatusValues() {
        String[] statuses = {"active", "completed", "cancelled"};
        
        for (String status : statuses) {
            goal.setStatus(status);
            assertEquals(status, goal.getStatus(), "Status should be set correctly");
        }
    }
}
