package com.fittrack.model;

import java.time.LocalDate;

/**
 * WorkoutPlan - Stores workout plan information
 * 
 * Simple usage:
 *   WorkoutPlan plan = WorkoutPlan.create("Beginner Strength", "beginner", 4);
 *   plan.description = "Full body workout for beginners";
 */
public class WorkoutPlan {
    
    // === Public Fields - Access directly! ===
    public int planId;
    public int userId;
    public String planName;
    public String description;
    public String difficulty;       // "beginner", "intermediate", "advanced"
    public int durationWeeks;
    public LocalDate createdAt;
    
    // === Constructors ===
    
    // Create empty plan with today's date
    public WorkoutPlan() {
        this.createdAt = LocalDate.now();
    }
    
    // === Helper Methods ===
    
    /**
     * Quick way to create a workout plan
     * Example: WorkoutPlan plan = WorkoutPlan.create("Push Day", "intermediate", 8);
     */
    public static WorkoutPlan create(String name, String difficulty, int weeks) {
        WorkoutPlan plan = new WorkoutPlan();
        plan.planName = name;
        plan.difficulty = difficulty;
        plan.durationWeeks = weeks;
        return plan;
    }
    
    /**
     * Create a plan for a specific user
     */
    public static WorkoutPlan createForUser(int userId, String name, String difficulty, int weeks) {
        WorkoutPlan plan = create(name, difficulty, weeks);
        plan.userId = userId;
        return plan;
    }
    
    /**
     * Check if this is a beginner plan
     */
    public boolean isBeginner() {
        return "beginner".equalsIgnoreCase(difficulty);
    }
    
    /**
     * Check if this is an intermediate plan
     */
    public boolean isIntermediate() {
        return "intermediate".equalsIgnoreCase(difficulty);
    }
    
    /**
     * Check if this is an advanced plan
     */
    public boolean isAdvanced() {
        return "advanced".equalsIgnoreCase(difficulty);
    }
    
    /**
     * Get a nice display name with difficulty
     */
    public String getDisplayName() {
        return planName + " (" + difficulty + ")";
    }
    
    @Override
    public String toString() {
        return planName + " (" + difficulty + ", " + durationWeeks + " weeks)";
    }
}
