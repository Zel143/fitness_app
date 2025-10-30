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
    public String goalType;
    public Double targetValue;
    public String targetUnit;
    public LocalDate targetDate;
    public LocalDate createdAt;
    public String status;
    
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
    
    // === Getters for JavaFX PropertyValueFactory ===
    
    public int getGoalId() { return goalId; }
    public void setGoalId(int goalId) { this.goalId = goalId; }
    
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getGoalType() { return goalType; }
    public void setGoalType(String goalType) { this.goalType = goalType; }
    
    public Double getTargetValue() { return targetValue; }
    public void setTargetValue(Double targetValue) { this.targetValue = targetValue; }
    
    public String getTargetUnit() { return targetUnit; }
    public void setTargetUnit(String targetUnit) { this.targetUnit = targetUnit; }
    
    public LocalDate getTargetDate() { return targetDate; }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }
    
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
