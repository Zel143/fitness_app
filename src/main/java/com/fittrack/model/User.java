package com.fittrack.model;

import java.time.LocalDate;

/**
 * User - Stores user account and profile information
 * 
 * Simple usage:
 *   User user = User.create("john", "john@email.com");
 *   user.age = 25;
 *   user.height = 175.0;
 *   user.weight = 70.0;
 */
public class User {
    
    // === Public Fields - Access directly! ===
    public int userId;
    public String username;
    public String email;
    public String passwordHash;
    public LocalDate createdAt;
    
    // Profile information
    public Integer age;
    public String gender;          // "Male", "Female", "Other"
    public Double height;          // in cm
    public Double weight;          // in kg
    public String fitnessLevel;    // "beginner", "intermediate", "advanced"
    
    // === Constructors ===
    
    // Create empty user with today's date
    public User() {
        this.createdAt = LocalDate.now();
    }
    
    // === Helper Methods ===
    
    /**
     * Quick way to create a user
     * Example: User user = User.create("john", "john@email.com");
     */
    public static User create(String username, String email) {
        User user = new User();
        user.username = username;
        user.email = email;
        return user;
    }
    
    /**
     * Create a user with password hash
     */
    public static User createWithPassword(String username, String email, String passwordHash) {
        User user = create(username, email);
        user.passwordHash = passwordHash;
        return user;
    }
    
    /**
     * Calculate BMI (Body Mass Index)
     * Formula: weight (kg) / (height (m) * height (m))
     * Returns 0.0 if height or weight not set
     */
    public double calculateBMI() {
        if (height == null || weight == null || height == 0) {
            return 0.0;
        }
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }
    
    /**
     * Get BMI category
     * Returns: "Underweight", "Normal", "Overweight", or "Obese"
     */
    public String getBMICategory() {
        double bmi = calculateBMI();
        if (bmi == 0.0) return "Unknown";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }
    
    /**
     * Check if profile is complete
     */
    public boolean hasCompleteProfile() {
        return age != null && gender != null && height != null && 
               weight != null && fitnessLevel != null;
    }
    
    /**
     * Check if user is a beginner
     */
    public boolean isBeginner() {
        return "beginner".equalsIgnoreCase(fitnessLevel);
    }
    
    @Override
    public String toString() {
        return username + " (" + email + ")";
    }
}
