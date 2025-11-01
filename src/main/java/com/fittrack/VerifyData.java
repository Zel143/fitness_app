package com.fittrack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.fittrack.model.DatabaseManager;

/**
 * Verify that data is being saved to the database
 */
public class VerifyData {
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   FitTrack Data Verification");
        System.out.println("==============================================\n");
        
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.connect();
        
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                
                // Check users
                System.out.println("1. USERS:");
                ResultSet rs = stmt.executeQuery("SELECT user_id, username, email, age, gender, height, weight, fitness_level FROM users");
                int userCount = 0;
                while (rs.next()) {
                    userCount++;
                    System.out.println("   User ID: " + rs.getInt("user_id"));
                    System.out.println("   Username: " + rs.getString("username"));
                    System.out.println("   Email: " + rs.getString("email"));
                    System.out.println("   Age: " + rs.getObject("age"));
                    System.out.println("   Gender: " + rs.getString("gender"));
                    System.out.println("   Height: " + rs.getObject("height"));
                    System.out.println("   Weight: " + rs.getObject("weight"));
                    System.out.println("   Fitness Level: " + rs.getString("fitness_level"));
                    System.out.println();
                }
                System.out.println("   Total users: " + userCount + "\n");
                
                // Check goals
                System.out.println("2. GOALS:");
                rs = stmt.executeQuery("SELECT * FROM goals");
                int goalCount = 0;
                while (rs.next()) {
                    goalCount++;
                    System.out.println("   Goal ID: " + rs.getInt("goal_id"));
                    System.out.println("   User ID: " + rs.getInt("user_id"));
                    System.out.println("   Type: " + rs.getString("goal_type"));
                    System.out.println("   Target: " + rs.getDouble("target_value") + " " + rs.getString("target_unit"));
                    System.out.println("   Target Date: " + rs.getDate("target_date"));
                    System.out.println("   Status: " + rs.getString("status"));
                    System.out.println();
                }
                System.out.println("   Total goals: " + goalCount + "\n");
                
                // Check workout plans
                System.out.println("3. WORKOUT PLANS:");
                rs = stmt.executeQuery("SELECT * FROM workout_plans");
                int planCount = 0;
                while (rs.next()) {
                    planCount++;
                    System.out.println("   Plan ID: " + rs.getInt("plan_id"));
                    System.out.println("   User ID: " + rs.getInt("user_id"));
                    System.out.println("   Name: " + rs.getString("plan_name"));
                    System.out.println("   Difficulty: " + rs.getString("difficulty"));
                    System.out.println();
                }
                System.out.println("   Total workout plans: " + planCount + "\n");
                
                // Check weight history
                System.out.println("4. WEIGHT HISTORY:");
                rs = stmt.executeQuery("SELECT * FROM weight_history");
                int weightCount = 0;
                while (rs.next()) {
                    weightCount++;
                    System.out.println("   User ID: " + rs.getInt("user_id"));
                    System.out.println("   Weight: " + rs.getDouble("weight") + " kg");
                    System.out.println("   Date: " + rs.getDate("date"));
                    System.out.println();
                }
                System.out.println("   Total weight entries: " + weightCount + "\n");
                
                // Check food log
                System.out.println("5. FOOD LOG:");
                rs = stmt.executeQuery("SELECT * FROM food_log");
                int foodCount = 0;
                while (rs.next()) {
                    foodCount++;
                    System.out.println("   User ID: " + rs.getInt("user_id"));
                    System.out.println("   Food: " + rs.getString("food_name"));
                    System.out.println("   Calories: " + rs.getInt("calories"));
                    System.out.println("   Date: " + rs.getDate("date"));
                    System.out.println();
                }
                System.out.println("   Total food entries: " + foodCount + "\n");
                
                rs.close();
                stmt.close();
                conn.close();
                
                System.out.println("==============================================");
                System.out.println("   ✓ Verification complete!");
                System.out.println("==============================================");
                
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("✗ Database connection failed!");
        }
    }
}
