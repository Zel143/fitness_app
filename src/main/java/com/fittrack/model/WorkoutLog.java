package com.fittrack.model;

import java.time.LocalDate;

public class WorkoutLog {
    private int id;
    private int userId;
    private String workoutName;
    private int sets;
    private int reps;
    private double weightUsed;
    private LocalDate date;

    public WorkoutLog() {}

    public WorkoutLog(int userId, String workoutName, int sets, int reps, double weightUsed, LocalDate date) {
        this.userId = userId;
        this.workoutName = workoutName;
        this.sets = sets;
        this.reps = reps;
        this.weightUsed = weightUsed;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getWorkoutName() { return workoutName; }
    public void setWorkoutName(String workoutName) { this.workoutName = workoutName; }

    public int getSets() { return sets; }
    public void setSets(int sets) { this.sets = sets; }

    public int getReps() { return reps; }
    public void setReps(int reps) { this.reps = reps; }

    public double getWeightUsed() { return weightUsed; }
    public void setWeightUsed(double weightUsed) { this.weightUsed = weightUsed; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
}
