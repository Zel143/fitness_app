package com.fittrack.model;

import java.time.LocalDate;
import java.util.Objects;

public class WeightHistory {
    private int id;
    private int userId;
    private double weight;
    private LocalDate date;

    public WeightHistory() {}

    public WeightHistory(int userId, double weight, LocalDate date) {
        this.userId = userId;
        this.weight = weight;
        this.date = date;
    }

    public WeightHistory(int id, int userId, double weight, LocalDate date) {
        this.id = id;
        this.userId = userId;
        this.weight = weight;
        this.date = date;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) {
        if (weight < 0) throw new IllegalArgumentException("weight must be non-negative");
        this.weight = weight;
    }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    @Override
    public String toString() {
        return "WeightHistory{id=" + id + ", userId=" + userId + ", weight=" + weight + ", date=" + date + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WeightHistory)) return false;
        WeightHistory that = (WeightHistory) o;
        return id == that.id &&
               userId == that.userId &&
               Double.compare(that.weight, weight) == 0 &&
               Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, weight, date);
    }
}