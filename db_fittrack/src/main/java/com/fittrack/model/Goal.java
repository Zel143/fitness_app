package com.fittrack.model;

import java.time.LocalDate;

/**
 * Goal model class representing the goals table
 * This POJO holds user fitness goals
 */
public class Goal {
    
    private int goalId;
    private int userId;
    private String goalType; // weight_loss, muscle_gain, endurance, flexibility
    private Double targetValue;
    private String targetUnit; // kg, minutes, reps, etc.
    private LocalDate targetDate;
    private LocalDate createdAt;
    private String status; // active, completed, abandoned
    
    // Constructors
    public Goal() {
        this.createdAt = LocalDate.now();
        this.status = "active";
    }
    
    public Goal(int userId, String goalType, Double targetValue, String targetUnit, LocalDate targetDate) {
        this();
        this.userId = userId;
        this.goalType = goalType;
        this.targetValue = targetValue;
        this.targetUnit = targetUnit;
        this.targetDate = targetDate;
    }
    
    // Getters and Setters
    public int getGoalId() {
        return goalId;
    }
    
    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public String getGoalType() {
        return goalType;
    }
    
    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }
    
    public Double getTargetValue() {
        return targetValue;
    }
    
    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }
    
    public String getTargetUnit() {
        return targetUnit;
    }
    
    public void setTargetUnit(String targetUnit) {
        this.targetUnit = targetUnit;
    }
    
    public LocalDate getTargetDate() {
        return targetDate;
    }
    
    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
    
    public LocalDate getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    @Override
    public String toString() {
        return "Goal{" +
                "goalId=" + goalId +
                ", userId=" + userId +
                ", goalType='" + goalType + '\'' +
                ", targetValue=" + targetValue +
                ", targetUnit='" + targetUnit + '\'' +
                ", targetDate=" + targetDate +
                ", status='" + status + '\'' +
                '}';
    }
}
