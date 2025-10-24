package com.fittrack.model;

/**
 * PlanExercise - Links exercises to workout plans
 * 
 * Simple usage:
 *   PlanExercise ex = PlanExercise.create("Bench Press", "chest", 3, 10, 1);
 *   ex.notes = "Focus on form!";
 */
public class PlanExercise {
    
    // === Public Fields - Access directly! ===
    public int planExerciseId;
    public int planId;
    public String exerciseName;
    public String muscleGroup;    // "chest", "back", "legs", "shoulders", "arms", "core", "cardio"
    public Integer sets;
    public Integer reps;
    public Integer duration;      // in seconds (for cardio/timed exercises)
    public String notes;
    public int dayOfWeek;         // 1 = Monday, 7 = Sunday
    
    // === Constructors ===
    
    // Create empty exercise
    public PlanExercise() {
    }
    
    // === Helper Methods ===
    
    /**
     * Quick way to create a strength exercise (with sets/reps)
     * Example: PlanExercise ex = PlanExercise.create("Squats", "legs", 4, 12, 1);
     */
    public static PlanExercise create(String exerciseName, String muscleGroup, 
                                      int sets, int reps, int dayOfWeek) {
        PlanExercise exercise = new PlanExercise();
        exercise.exerciseName = exerciseName;
        exercise.muscleGroup = muscleGroup;
        exercise.sets = sets;
        exercise.reps = reps;
        exercise.dayOfWeek = dayOfWeek;
        return exercise;
    }
    
    /**
     * Create a cardio exercise (with duration in seconds)
     * Example: PlanExercise ex = PlanExercise.createCardio("Running", 1800, 1);
     */
    public static PlanExercise createCardio(String exerciseName, int durationSeconds, int dayOfWeek) {
        PlanExercise exercise = new PlanExercise();
        exercise.exerciseName = exerciseName;
        exercise.muscleGroup = "cardio";
        exercise.duration = durationSeconds;
        exercise.dayOfWeek = dayOfWeek;
        return exercise;
    }
    
    /**
     * Check if this is a cardio exercise
     */
    public boolean isCardio() {
        return "cardio".equalsIgnoreCase(muscleGroup);
    }
    
    /**
     * Check if this is a strength exercise (has sets/reps)
     */
    public boolean isStrength() {
        return sets != null && reps != null;
    }
    
    /**
     * Get day name (Monday, Tuesday, etc.)
     */
    public String getDayName() {
        String[] days = {"", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        if (dayOfWeek < 1 || dayOfWeek > 7) return "Unknown";
        return days[dayOfWeek];
    }
    
    /**
     * Get duration in minutes (for cardio exercises)
     */
    public int getDurationMinutes() {
        if (duration == null) return 0;
        return duration / 60;
    }
    
    /**
     * Get a nice display string
     */
    public String getDisplayText() {
        if (isCardio()) {
            return exerciseName + " - " + getDurationMinutes() + " min (" + getDayName() + ")";
        } else {
            return exerciseName + " - " + sets + "x" + reps + " (" + getDayName() + ")";
        }
    }
    
    @Override
    public String toString() {
        return getDisplayText();
    }
}
