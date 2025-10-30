package com.fittrack.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import org.mindrot.jbcrypt.BCrypt;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/fittrack_db";
    private static final String DB_USER = "fittrack_admin";
    private static final String DB_PASSWORD = "mySQL";
    
    /**
     * Establishes a connection to the MySQL database.
     */
    public Connection connect() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates all necessary tables if they do not already exist.
     */
    public void createTables() {
        String[] tablesSQL = {
            // 1. users Table
            "CREATE TABLE IF NOT EXISTS users (\n"
            + "    user_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    username VARCHAR(50) NOT NULL UNIQUE,\n"
            + "    email VARCHAR(100) NOT NULL UNIQUE,\n"
            + "    password_hash VARCHAR(255) NOT NULL,\n"
            + "    age INT,\n"
            + "    gender VARCHAR(20),\n"
            + "    height DOUBLE,\n"
            + "    weight DOUBLE,\n"
            + "    fitness_level VARCHAR(20),\n"
            + "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP\n"
            + ") ENGINE=InnoDB;",
            
            // 2. goals Table
            "CREATE TABLE IF NOT EXISTS goals (\n"
            + "    goal_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    user_id INT NOT NULL,\n"
            + "    goal_type VARCHAR(50) NOT NULL,\n"
            + "    target_value DOUBLE,\n"
            + "    target_unit VARCHAR(20),\n"
            + "    target_date DATE,\n"
            + "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
            + "    status VARCHAR(20) DEFAULT 'active',\n"
            + "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE\n"
            + ") ENGINE=InnoDB;",
            
            // 3. workout_plans Table
            "CREATE TABLE IF NOT EXISTS workout_plans (\n"
            + "    plan_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    user_id INT NOT NULL,\n"
            + "    plan_name VARCHAR(100) NOT NULL,\n"
            + "    description TEXT,\n"
            + "    difficulty VARCHAR(20),\n"
            + "    duration_weeks INT,\n"
            + "    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,\n"
            + "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE\n"
            + ") ENGINE=InnoDB;",
            
            // 4. plan_exercises Table
            "CREATE TABLE IF NOT EXISTS plan_exercises (\n"
            + "    plan_exercise_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    plan_id INT NOT NULL,\n"
            + "    exercise_name VARCHAR(100) NOT NULL,\n"
            + "    muscle_group VARCHAR(50),\n"
            + "    sets INT,\n"
            + "    reps INT,\n"
            + "    duration INT,\n"
            + "    notes TEXT,\n"
            + "    day_of_week INT,\n"
            + "    FOREIGN KEY (plan_id) REFERENCES workout_plans(plan_id) ON DELETE CASCADE\n"
            + ") ENGINE=InnoDB;",
            
            // 5. exercises Table
            "CREATE TABLE IF NOT EXISTS exercises (\n"
            + "    exercise_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    exercise_name VARCHAR(100) NOT NULL UNIQUE,\n"
            + "    muscle_group VARCHAR(50),\n"
            + "    exercise_type ENUM('strength', 'cardio') DEFAULT 'strength' NOT NULL\n"
            + ") ENGINE=InnoDB;",
            
            // 6. workout_log Table
            "CREATE TABLE IF NOT EXISTS workout_log (\n"
            + "    log_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    user_id INT NOT NULL,\n"
            + "    exercise_id INT NOT NULL,\n"
            + "    sets INT,\n"
            + "    reps INT,\n"
            + "    weight_used DOUBLE,\n"
            + "    duration_minutes DECIMAL(6,2),\n"
            + "    distance_km DECIMAL(6,2),\n"
            + "    date DATE NOT NULL,\n"
            + "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,\n"
            + "    FOREIGN KEY (exercise_id) REFERENCES exercises(exercise_id) ON DELETE RESTRICT\n"
            + ") ENGINE=InnoDB;",
            
            // 7. weight_history Table
            "CREATE TABLE IF NOT EXISTS weight_history (\n"
            + "    history_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    user_id INT NOT NULL,\n"
            + "    weight DOUBLE NOT NULL,\n"
            + "    date DATE NOT NULL,\n"
            + "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE\n"
            + ") ENGINE=InnoDB;",
            
            // 8. food_library Table
            "CREATE TABLE IF NOT EXISTS food_library (\n"
            + "    food_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    food_name VARCHAR(100) NOT NULL,\n"
            + "    serving_size_g DECIMAL(6,2),\n"
            + "    calories DECIMAL(6,2),\n"
            + "    protein DOUBLE,\n"
            + "    carbs DOUBLE,\n"
            + "    fats DOUBLE,\n"
            + "    created_by_user_id INT NOT NULL,\n"
            + "    FOREIGN KEY (created_by_user_id) REFERENCES users(user_id) ON DELETE CASCADE\n"
            + ") ENGINE=InnoDB;",
            
            // 9. food_log Table
            "CREATE TABLE IF NOT EXISTS food_log (\n"
            + "    food_log_id INT AUTO_INCREMENT PRIMARY KEY,\n"
            + "    user_id INT NOT NULL,\n"
            + "    food_library_id INT NOT NULL,\n"
            + "    food_name VARCHAR(100) NOT NULL,\n"
            + "    calories INT NOT NULL,\n"
            + "    protein DOUBLE,\n"
            + "    carbs DOUBLE,\n"
            + "    fats DOUBLE,\n"
            + "    date DATE NOT NULL,\n"
            + "    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,\n"
            + "    FOREIGN KEY (food_library_id) REFERENCES food_library(food_id) ON DELETE CASCADE\n"
            + ") ENGINE=InnoDB;"
        };

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            System.out.println("✓ Checking and creating tables...");
            for (String sql : tablesSQL) {
                stmt.execute(sql);
            }
            System.out.println("✓ All tables are ready!");
        } catch (SQLException e) {
            System.err.println("✗ Error creating tables: " + e.getMessage());
        }
    }

    /**
     * Registers a new user with a hashed password.
     */
    public boolean register(User user, String password) {
        String sql = "INSERT INTO users(username, email, password_hash) VALUES(?,?,?)";
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.username);
            pstmt.setString(2, user.email);
            pstmt.setString(3, hashedPassword);
            pstmt.executeUpdate();
            System.out.println("✓ User registered: " + user.username);
            return true;
        } catch (SQLException e) {
            System.err.println("✗ Registration error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Authenticates a user by checking their password.
     */
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");
                if (BCrypt.checkpw(password, storedHash)) {
                    // Password is correct, create User object
                    User user = new User();
                    user.userId = rs.getInt("user_id");
                    user.username = rs.getString("username");
                    user.email = rs.getString("email");
                    user.age = rs.getObject("age", Integer.class);
                    user.gender = rs.getString("gender");
                    user.height = rs.getObject("height", Double.class);
                    user.weight = rs.getObject("weight", Double.class);
                    user.fitnessLevel = rs.getString("fitness_level");
                    
                    System.out.println("✓ Login successful: " + username);
                    return user;
                }
            }
        } catch (SQLException e) {
            System.err.println("✗ Login error: " + e.getMessage());
        }
        System.out.println("✗ Login failed: Invalid credentials");
        return null;
    }

    /**
     * Updates user profile information.
     */
    public boolean updateUserProfile(User user) {
        String sql = "UPDATE users "
            + "SET age = ?, gender = ?, height = ?, weight = ?, fitness_level = ? "
            + "WHERE user_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setObject(1, user.age);
            pstmt.setString(2, user.gender);
            pstmt.setObject(3, user.height);
            pstmt.setObject(4, user.weight);
            pstmt.setString(5, user.fitnessLevel);
            pstmt.setInt(6, user.userId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Profile updated for user: " + user.username);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Update profile error: " + e.getMessage());
            return false;
        }
    }

    /**
     * Saves a new goal for a user.
     */
    public boolean saveGoal(Goal goal) {
        String sql = "INSERT INTO goals(user_id, goal_type, target_value, target_unit, target_date, status) "
            + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, goal.userId);
            pstmt.setString(2, goal.goalType);
            pstmt.setObject(3, goal.targetValue);
            pstmt.setString(4, goal.targetUnit);
            pstmt.setObject(5, goal.targetDate);
            pstmt.setString(6, goal.status);
            
            int rowsAffected = pstmt.executeUpdate();
            
            // Retrieve the generated goal_id
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        goal.goalId = generatedKeys.getInt(1);
                    }
                }
            }
            
            System.out.println("✓ Goal saved for user ID: " + goal.userId + " with goal ID: " + goal.goalId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Save goal error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all goals for a specific user.
     */
    public java.util.List<Goal> getGoals(int userId) {
        java.util.List<Goal> goals = new java.util.ArrayList<>();
        String sql = "SELECT * FROM goals WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Goal goal = new Goal();
                goal.goalId = rs.getInt("goal_id");
                goal.userId = rs.getInt("user_id");
                goal.goalType = rs.getString("goal_type");
                goal.targetValue = rs.getObject("target_value", Double.class);
                goal.targetUnit = rs.getString("target_unit");
                goal.targetDate = rs.getObject("target_date", LocalDate.class);
                goal.status = rs.getString("status");
                
                // Also get created_at if needed
                java.sql.Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    goal.createdAt = timestamp.toLocalDateTime().toLocalDate();
                }
                
                goals.add(goal);
            }
            System.out.println("✓ Retrieved " + goals.size() + " goals for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("✗ Get goals error: " + e.getMessage());
            e.printStackTrace();
        }
        return goals;
    }

    /**
     * Test database connection
     */
    public boolean testConnection() {
        try (Connection conn = connect()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection test: SUCCESS");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connection test FAILED: " + e.getMessage());
        }
        return false;
    }
}