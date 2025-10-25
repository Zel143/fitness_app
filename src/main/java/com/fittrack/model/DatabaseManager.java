package com.fittrack.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.mindrot.jbcrypt.BCrypt;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/fittrack_db";
    private static final String DB_USER = "fittrack_admin"; // Use the new user you created
    private static final String DB_PASSWORD = "your_app_password"; // Use the password for fittrack_admin

    /**
     * Establishes a connection to the MySQL database.
     * @return a Connection object or null if connection fails.
     */
    public Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates all necessary tables if they do not already exist.
     */
    public void createTables() {
        String[] tablesSQL = {
            // 1. users Table
            "CREATE TABLE IF NOT EXISTS users (id INT AUTO_INCREMENT PRIMARY KEY, username VARCHAR(50) NOT NULL UNIQUE, email VARCHAR(100) NOT NULL UNIQUE, hashed_password VARCHAR(255) NOT NULL, height_cm DECIMAL(5,2), created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP) ENGINE=InnoDB;",
            // 2. exercises Table
            "CREATE TABLE IF NOT EXISTS exercises (id INT AUTO_INCREMENT PRIMARY KEY, exercise_name VARCHAR(100) NOT NULL UNIQUE, muscle_group VARCHAR(50), exercise_type ENUM('strength', 'cardio') DEFAULT 'strength' NOT NULL) ENGINE=InnoDB;",
            // 3. food_library Table
            "CREATE TABLE IF NOT EXISTS food_library (id INT AUTO_INCREMENT PRIMARY KEY, food_name VARCHAR(100) NOT NULL, serving_size_g DECIMAL(6,2), calories DECIMAL(6,2), protein DOUBLE, carbs DOUBLE, fats DOUBLE, created_by_user_id INT NOT NULL, CONSTRAINT fk_food_library_user FOREIGN KEY (created_by_user_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB;",
            // 4. goals Table
            "CREATE TABLE IF NOT EXISTS goals (id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, goal_type VARCHAR(50) NOT NULL, target_value DECIMAL(10,2), target_date DATE, is_active TINYINT(1) DEFAULT 1, CONSTRAINT fk_goals_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB;",
            // 5. workout_plans Table
            "CREATE TABLE IF NOT EXISTS workout_plans (id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, goal_id INT NOT NULL, plan_name VARCHAR(100) NOT NULL, CONSTRAINT fk_workout_plans_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, CONSTRAINT fk_workout_plans_goal FOREIGN KEY (goal_id) REFERENCES goals(id) ON DELETE CASCADE) ENGINE=InnoDB;",
            // 6. plan_exercises Table
            "CREATE TABLE IF NOT EXISTS plan_exercises (id INT AUTO_INCREMENT PRIMARY KEY, plan_id INT NOT NULL, exercise_id INT NOT NULL, target_sets INT, target_reps INT, CONSTRAINT fk_plan_exercises_plan FOREIGN KEY (plan_id) REFERENCES workout_plans(id) ON DELETE CASCADE, CONSTRAINT fk_plan_exercises_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE RESTRICT) ENGINE=InnoDB;",
            // 7. weight_history Table
            "CREATE TABLE IF NOT EXISTS weight_history (id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, weight DOUBLE NOT NULL, date DATE NOT NULL, CONSTRAINT fk_weight_history_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE) ENGINE=InnoDB;",
            // 8. workout_log Table
            "CREATE TABLE IF NOT EXISTS workout_log (id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, exercise_id INT NOT NULL, sets INT, reps INT, weight_used DOUBLE, duration_minutes DECIMAL(6,2), distance_km DECIMAL(6,2), date DATE NOT NULL, CONSTRAINT fk_workout_log_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, CONSTRAINT fk_workout_log_exercise FOREIGN KEY (exercise_id) REFERENCES exercises(id) ON DELETE RESTRICT) ENGINE=InnoDB;",
            // 9. food_log Table
            "CREATE TABLE IF NOT EXISTS food_log (id INT AUTO_INCREMENT PRIMARY KEY, user_id INT NOT NULL, food_library_id INT NOT NULL, food_name VARCHAR(100) NOT NULL, calories INT NOT NULL, protein DOUBLE, carbs DOUBLE, fats DOUBLE, date DATE NOT NULL, CONSTRAINT fk_food_log_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE, CONSTRAINT fk_food_log_library FOREIGN KEY (food_library_id) REFERENCES food_library(id) ON DELETE CASCADE) ENGINE=InnoDB;"
        };

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            System.out.println("Checking and creating tables...");
            for (String sql : tablesSQL) {
                stmt.execute(sql);
            }
            System.out.println("Tables are ready.");
        } catch (SQLException e) {
            System.err.println("Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Registers a new user in the database with a hashed password.
     * @param user The User object with registration data.
     * @param password The plain-text password.
     * @return true if registration is successful, false otherwise.
     */
    public boolean register(User user, String password) {
        String sql = "INSERT INTO users(username, email, hashed_password) VALUES(?,?,?)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.username);
            pstmt.setString(2, user.email);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user by checking their password against the stored hash.
     * @param username The username to log in with.
     * @param password The plain-text password to check.
     * @return A User object if login is successful, null otherwise.
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("hashed_password");
                if (BCrypt.checkpw(password, storedHash)) {
                    // Password is correct, create and return User object
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.username = rs.getString("username");
                    user.email = rs.getString("email");
                    user.setHeightCm(rs.getDouble("height_cm"));
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("Login error: " + e.getMessage());
        }
        return null; // Return null if user not found or password incorrect
    }
}
