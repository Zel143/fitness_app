package com.fittrack.model;

import java.time.LocalDate;

/**
 * WorkoutPlan model class representing the workout_plans table
 * This POJO holds workout plan information
 */
public class WorkoutPlan {
    
    private int planId;
    private int userId;
    private String planName;
    private String description;
    private String difficulty; // beginner, intermediate, advanced
    private int durationWeeks;
    private LocalDate createdAt;
    
    // Constructors
    public WorkoutPlan() {
        this.createdAt = LocalDate.now();
    }
    
    public WorkoutPlan(int userId, String planName, String description, String difficulty, int durationWeeks) {
        this();
        this.userId = userId;
        this.planName = planName;
        this.description = description;
        this.difficulty = difficulty;
        this.durationWeeks = durationWeeks;
    }
    
    // Getters and Setters
    public int getPlanId() {
        return planId;
    }
    
    public void setPlanId(int planId) {
        this.planId = planId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getPlanName() {
        return planName;
    }
    
    public void setPlanName(String planName) {
        this.planName = planName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getDifficulty() {
        return difficulty;
    }
    
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    public int getDurationWeeks() {
        return durationWeeks;
    }
    
    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "WorkoutPlan{" +
                "planId=" + planId +
                ", userId=" + userId +
                ", planName='" + planName + '\'' +
                ", description='" + description + '\'' +
                ", difficulty='" + difficulty + '\'' +
                ", durationWeeks=" + durationWeeks +
                '}';
    }
}
