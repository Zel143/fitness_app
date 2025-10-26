package com.fittrack.model;

import java.time.LocalDate;

public class User {
    
    // Public Fields
    public int userId;
    public String username;
    public String email;
    public String passwordHash;
    public LocalDate createdAt;
    
    // Profile information
    public Integer age;
    public String gender;
    public Double height;
    public Double weight;
    public String fitnessLevel;
    
    // Constructors
    public User() {
        this.createdAt = LocalDate.now();
    }
    
    // Getters and Setters (REQUIRED for DatabaseManager)
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public String getFitnessLevel() { return fitnessLevel; }
    public void setFitnessLevel(String fitnessLevel) { this.fitnessLevel = fitnessLevel; }
    
    // Helper Methods
    public static User create(String username, String email) {
        User user = new User();
        user.username = username;
        user.email = email;
        return user;
    }
    
    public double calculateBMI() {
        if (height == null || weight == null || height == 0) {
            return 0.0;
        }
        double heightInMeters = height / 100.0;
        return weight / (heightInMeters * heightInMeters);
    }
    
    public String getBMICategory() {
        double bmi = calculateBMI();
        if (bmi == 0.0) return "Unknown";
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }
    
    public boolean hasCompleteProfile() {
        return age != null && gender != null && height != null && 
               weight != null && fitnessLevel != null;
    }
    
    @Override
    public String toString() {
        return username + " (" + email + ")";
    }
}