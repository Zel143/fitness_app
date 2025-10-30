package com.fittrack;

import com.fittrack.model.DatabaseManager;

public class TestConnection {
    public static void main(String[] args) {
        DatabaseManager db = new DatabaseManager();
        var conn = db.connect();
        
        if (conn != null) {
            System.out.println("✓ Database connection successful!");
            db.createTables();
        } else {
            System.out.println("✗ Connection failed!");
        }
    }
}