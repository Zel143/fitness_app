package com.fittrack.model;

/**
 * PlanExercise model class representing the plan_exercises table
 * This POJO links exercises to workout plans
 */
public class PlanExercise {
    
    private int planExerciseId;
    private int planId;
    private String exerciseName;
    private String muscleGroup; // chest, back, legs, shoulders, arms, core, cardio
    private Integer sets;
    private Integer reps;
    private Integer duration; // in seconds (for cardio/timed exercises)
    private String notes;
    private int dayOfWeek; // 1 = Monday, 7 = Sunday
    
    // Constructors
    public PlanExercise() {
    }
    
    public PlanExercise(int planId, String exerciseName, String muscleGroup, 
                       Integer sets, Integer reps, int dayOfWeek) {
        this.planId = planId;
        this.exerciseName = exerciseName;
        this.muscleGroup = muscleGroup;
        this.sets = sets;
        this.reps = reps;
        this.dayOfWeek = dayOfWeek;
    }
    
    // Getters and Setters
    public int getPlanExerciseId() {
        return planExerciseId;
    }
    
    public void setPlanExerciseId(int planExerciseId) {
        this.planExerciseId = planExerciseId;
    }
    
    public int getPlanId() {
        return planId;
    }
    
    public void setPlanId(int planId) {
        this.planId = planId;
    }
    
    public String getExerciseName() {
        return exerciseName;
    }
    
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    
    public String getMuscleGroup() {
        return muscleGroup;
    }
    
    public void setMuscleGroup(String muscleGroup) {
        this.muscleGroup = muscleGroup;
    }
    
    public Integer getSets() {
        return sets;
    }
    
    public void setSets(Integer sets) {
        this.sets = sets;
    }
    
    public Integer getReps() {
        return reps;
    }
    
    public void setReps(Integer reps) {
        this.reps = reps;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public int getDayOfWeek() {
        return dayOfWeek;
    }
    
    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }
    
    @Override
    public String toString() {
        return "PlanExercise{" +
                "planExerciseId=" + planExerciseId +
                ", planId=" + planId +
                ", exerciseName='" + exerciseName + '\'' +
                ", muscleGroup='" + muscleGroup + '\'' +
                ", sets=" + sets +
                ", reps=" + reps +
                ", duration=" + duration +
                ", dayOfWeek=" + dayOfWeek +
                '}';
    }
}
