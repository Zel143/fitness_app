package com.fittrack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import com.fittrack.model.DatabaseManager;

public class CheckUsers {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        Connection conn = db.connect();
        
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT user_id, username, email FROM users");
                
                System.out.println("=== Users in Database ===");
                int count = 0;
                while (rs.next()) {
                    System.out.println("ID: " + rs.getInt("user_id") + 
                                     " | Username: " + rs.getString("username") + 
                                     " | Email: " + rs.getString("email"));
                    count++;
                }
                
                if (count == 0) {
                    System.out.println("No users found in database.");
                    System.out.println("\nPlease register a new account first!");
                } else {
                    System.out.println("\nTotal users: " + count);
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("Database connection failed!");
        }
    }
}
