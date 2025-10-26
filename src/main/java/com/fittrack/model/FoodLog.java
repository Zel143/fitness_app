package com.fittrack.model;

import java.time.LocalDate;
import java.util.Objects;

public class FoodLog {
    private int id;
    private int userId;
    private String foodName;
    private int calories;
    private double protein;
    private double carbs;
    private double fats;
    private LocalDate date;

    public FoodLog() {}

    public FoodLog(int userId, String foodName, int calories, double protein, double carbs, double fats, LocalDate date) {
        if (foodName == null) throw new IllegalArgumentException("foodName must not be null");
        if (calories < 0) throw new IllegalArgumentException("calories must be non-negative");
        if (protein < 0 || carbs < 0 || fats < 0) throw new IllegalArgumentException("macros must be non-negative");
        if (date == null) throw new IllegalArgumentException("date must not be null");

        this.userId = userId;
        this.foodName = foodName;
        this.calories = calories;
        this.protein = protein;
        this.carbs = carbs;
        this.fats = fats;
        this.date = date;
    }

    public FoodLog(int id, int userId, String foodName, int calories, double protein, double carbs, double fats, LocalDate date) {
        this(userId, foodName, calories, protein, carbs, fats, date);
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) {
        if (foodName == null) throw new IllegalArgumentException("foodName must not be null");
        this.foodName = foodName;
    }

    public int getCalories() { return calories; }
    public void setCalories(int calories) {
        if (calories < 0) throw new IllegalArgumentException("calories must be non-negative");
        this.calories = calories;
    }

    public double getProtein() { return protein; }
    public void setProtein(double protein) {
        if (protein < 0) throw new IllegalArgumentException("protein must be non-negative");
        this.protein = protein;
    }

    public double getCarbs() { return carbs; }
    public void setCarbs(double carbs) {
        if (carbs < 0) throw new IllegalArgumentException("carbs must be non-negative");
        this.carbs = carbs;
    }

    public double getFats() { return fats; }
    public void setFats(double fats) {
        if (fats < 0) throw new IllegalArgumentException("fats must be non-negative");
        this.fats = fats;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) {
        if (date == null) throw new IllegalArgumentException("date must not be null");
        this.date = date;
    }

    @Override
    public String toString() {
        return "FoodLog{id=" + id + ", userId=" + userId + ", foodName='" + foodName + '\'' +
               ", calories=" + calories + ", protein=" + protein + ", carbs=" + carbs + ", fats=" + fats +
               ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FoodLog)) return false;
        FoodLog foodLog = (FoodLog) o;
        return id == foodLog.id &&
               userId == foodLog.userId &&
               calories == foodLog.calories &&
               Double.compare(foodLog.protein, protein) == 0 &&
               Double.compare(foodLog.carbs, carbs) == 0 &&
               Double.compare(foodLog.fats, fats) == 0 &&
               Objects.equals(foodName, foodLog.foodName) &&
               Objects.equals(date, foodLog.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, foodName, calories, protein, carbs, fats, date);
    }
}