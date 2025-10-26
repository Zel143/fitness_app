package com.fittrack;

import com.fittrack.model.DatabaseManager;

/**
 * One-time setup to create all database tables
 * Run this once to initialize your database
 */
public class DatabaseSetup {
    
    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("   FitTrack Database Setup");
        System.out.println("==============================================\n");
        
        DatabaseManager dbManager = new DatabaseManager();
        
        // Test connection
        System.out.println("1. Testing database connection...");
        var conn = dbManager.connect();
        if (conn != null) {
            System.out.println("   ✓ Database connection successful!");
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("   ✗ Database connection FAILED!");
            System.out.println("\n   Please check:");
            System.out.println("   - MySQL is running");
            System.out.println("   - Database 'fittrack_db' exists");
            System.out.println("   - Username 'fittrack_admin' exists");
            System.out.println("   - Password in DatabaseManager.java is correct");
            return;
        }
        
        // Create tables
        System.out.println("\n2. Creating database tables...");
        dbManager.createTables();
        
        System.out.println("\n==============================================");
        System.out.println("   ✓ Database setup complete!");
        System.out.println("==============================================");
        System.out.println("\nYou can now run your application:");
        System.out.println("   mvn javafx:run");
    }
}
