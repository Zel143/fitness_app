package com.fittrack.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Goal - Stores user fitness goals
 * 
 * Simple usage:
 *   Goal goal = Goal.create("weight_loss", 70.0, "kg", LocalDate.now().plusMonths(3));
 *   goal.userId = 1;
 */
public class Goal {
    
    // === Public Fields - Access directly! ===
    public int goalId;
    public int userId;
    public String goalType;      // "weight_loss", "muscle_gain", "endurance", "flexibility"
    public Double targetValue;
    public String targetUnit;    // "kg", "minutes", "reps", etc.
    public LocalDate targetDate;
    public LocalDate createdAt;
    public String status;        // "active", "completed", "abandoned"
    
    // === Constructors ===
    
    // Create empty goal with today's date and active status
    public Goal() {
        this.createdAt = LocalDate.now();
        this.status = "active";
    }
    
    // === Helper Methods ===
    
    /**
     * Quick way to create a goal
     * Example: Goal goal = Goal.create("weight_loss", 70.0, "kg", targetDate);
     */
    public static Goal create(String goalType, Double targetValue, String targetUnit, LocalDate targetDate) {
        Goal goal = new Goal();
        goal.goalType = goalType;
        goal.targetValue = targetValue;
        goal.targetUnit = targetUnit;
        goal.targetDate = targetDate;
        return goal;
    }
    
    /**
     * Create a goal for a specific user
     */
    public static Goal createForUser(int userId, String goalType, Double targetValue, 
                                     String targetUnit, LocalDate targetDate) {
        Goal goal = create(goalType, targetValue, targetUnit, targetDate);
        goal.userId = userId;
        return goal;
    }
    
    /**
     * Check if goal is active
     */
    public boolean isActive() {
        return "active".equalsIgnoreCase(status);
    }
    
    /**
     * Check if goal is completed
     */
    public boolean isCompleted() {
        return "completed".equalsIgnoreCase(status);
    }
    
    /**
     * Check if goal is abandoned
     */
    public boolean isAbandoned() {
        return "abandoned".equalsIgnoreCase(status);
    }
    
    /**
     * Mark goal as completed
     */
    public void complete() {
        this.status = "completed";
    }
    
    /**
     * Mark goal as abandoned
     */
    public void abandon() {
        this.status = "abandoned";
    }
    
    /**
     * Get days remaining until target date
     * Returns negative if overdue
     */
    public long getDaysRemaining() {
        if (targetDate == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
    }
    
    /**
     * Check if goal is overdue
     */
    public boolean isOverdue() {
        return getDaysRemaining() < 0;
    }
    
    /**
     * Get a nice display string
     */
    public String getDisplayText() {
        return goalType.replace("_", " ") + ": " + targetValue + " " + targetUnit;
    }
    
    @Override
    public String toString() {
        return getDisplayText() + " (by " + targetDate + ")";
    }
}
