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
                
                // Get created_at timestamp
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
     * Retrieves all workout logs for a specific user.
     */
    public java.util.List<WorkoutLog> getWorkoutLogs(int userId) {
        java.util.List<WorkoutLog> logs = new java.util.ArrayList<>();
        String sql = "SELECT * FROM workout_log WHERE user_id = ? ORDER BY date DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                WorkoutLog log = new WorkoutLog();
                log.setId(rs.getInt("log_id"));
                log.setUserId(rs.getInt("user_id"));
                log.setWorkoutName(rs.getString("exercise_id")); // Map as needed
                log.setSets(rs.getInt("sets"));
                log.setReps(rs.getInt("reps"));
                log.setWeightUsed(rs.getDouble("weight_used"));
                log.setDate(rs.getObject("date", LocalDate.class));
                logs.add(log);
            }
            System.out.println("✓ Retrieved " + logs.size() + " workout logs for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("✗ Get workout logs error: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Retrieves all workout plans for a specific user.
     */
    public java.util.List<WorkoutPlan> getWorkoutPlans(int userId) {
        java.util.List<WorkoutPlan> plans = new java.util.ArrayList<>();
        String sql = "SELECT * FROM workout_plans WHERE user_id = ? ORDER BY created_at DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                WorkoutPlan plan = new WorkoutPlan();
                plan.planId = rs.getInt("plan_id");
                plan.userId = rs.getInt("user_id");
                plan.planName = rs.getString("plan_name");
                plan.description = rs.getString("description");
                plan.difficulty = rs.getString("difficulty");
                plan.durationWeeks = rs.getInt("duration_weeks");
                plans.add(plan);
            }
            System.out.println("✓ Retrieved " + plans.size() + " workout plans for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("✗ Get workout plans error: " + e.getMessage());
            e.printStackTrace();
        }
        return plans;
    }

    /**
     * Saves a new workout plan.
     */
    public boolean saveWorkoutPlan(WorkoutPlan plan) {
        String sql = "INSERT INTO workout_plans(user_id, plan_name, description, difficulty, duration_weeks) "
            + "VALUES(?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, plan.userId);
            pstmt.setString(2, plan.planName);
            pstmt.setString(3, plan.description);
            pstmt.setString(4, plan.difficulty);
            pstmt.setInt(5, plan.durationWeeks);
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        plan.planId = generatedKeys.getInt(1);
                    }
                }
            }
            
            System.out.println("✓ Workout plan saved with ID: " + plan.planId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Save workout plan error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a workout plan by its ID.
     */
    public boolean deleteWorkoutPlan(int planId) {
        String sql = "DELETE FROM workout_plans WHERE plan_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, planId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Workout plan deleted with ID: " + planId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Delete workout plan error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all weight history for a specific user.
     */
    public java.util.List<WeightHistory> getWeightHistory(int userId) {
        java.util.List<WeightHistory> history = new java.util.ArrayList<>();
        String sql = "SELECT * FROM weight_history WHERE user_id = ? ORDER BY date DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                WeightHistory entry = new WeightHistory(
                    rs.getInt("history_id"),
                    rs.getInt("user_id"),
                    rs.getDouble("weight"),
                    rs.getObject("date", LocalDate.class)
                );
                history.add(entry);
            }
            System.out.println("✓ Retrieved " + history.size() + " weight entries for user ID: " + userId);
        } catch (SQLException e) {
            System.err.println("✗ Get weight history error: " + e.getMessage());
            e.printStackTrace();
        }
        return history;
    }

    /**
     * Saves a new weight history entry.
     */
    public boolean saveWeightHistory(WeightHistory entry) {
        String sql = "INSERT INTO weight_history(user_id, weight, date) VALUES(?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entry.getUserId());
            pstmt.setDouble(2, entry.getWeight());
            pstmt.setObject(3, entry.getDate());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        entry.setId(generatedKeys.getInt(1));
                    }
                }
            }
            
            System.out.println("✓ Weight history saved with ID: " + entry.getId());
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Save weight history error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a weight history entry by its ID.
     */
    public boolean deleteWeightHistory(int historyId) {
        String sql = "DELETE FROM weight_history WHERE history_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, historyId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Weight history deleted with ID: " + historyId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Delete weight history error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Retrieves all food log entries for a specific user and date.
     */
    public java.util.List<FoodLog> getFoodLog(int userId, LocalDate date) {
        java.util.List<FoodLog> logs = new java.util.ArrayList<>();
        String sql = date == null 
            ? "SELECT * FROM food_log WHERE user_id = ? ORDER BY date DESC, food_log_id DESC"
            : "SELECT * FROM food_log WHERE user_id = ? AND date = ? ORDER BY food_log_id DESC";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            if (date != null) {
                pstmt.setObject(2, date);
            }
            
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                FoodLog log = new FoodLog(
                    rs.getInt("food_log_id"),
                    rs.getInt("user_id"),
                    rs.getString("food_name"),
                    rs.getInt("calories"),
                    rs.getDouble("protein"),
                    rs.getDouble("carbs"),
                    rs.getDouble("fats"),
                    rs.getObject("date", LocalDate.class)
                );
                logs.add(log);
            }
            System.out.println("✓ Retrieved " + logs.size() + " food log entries");
        } catch (SQLException e) {
            System.err.println("✗ Get food log error: " + e.getMessage());
            e.printStackTrace();
        }
        return logs;
    }

    /**
     * Saves a new food log entry.
     */
    public boolean saveFoodLog(FoodLog log) {
        String sql = "INSERT INTO food_log(user_id, food_library_id, food_name, calories, protein, carbs, fats, date) "
            + "VALUES(?, 1, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, log.getUserId());
            pstmt.setString(2, log.getFoodName());
            pstmt.setInt(3, log.getCalories());
            pstmt.setDouble(4, log.getProtein());
            pstmt.setDouble(5, log.getCarbs());
            pstmt.setDouble(6, log.getFats());
            pstmt.setObject(7, log.getDate());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        log.setId(generatedKeys.getInt(1));
                    }
                }
            }
            
            System.out.println("✓ Food log saved with ID: " + log.getId());
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Save food log error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a food log entry by its ID.
     */
    public boolean deleteFoodLog(int foodLogId) {
        String sql = "DELETE FROM food_log WHERE food_log_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, foodLogId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Food log deleted with ID: " + foodLogId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Delete food log error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Saves a new workout log.
     */
    public boolean saveWorkoutLog(WorkoutLog log) {
        String sql = "INSERT INTO workout_log(user_id, exercise_id, sets, reps, weight_used, date) "
            + "VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, log.getUserId());
            pstmt.setInt(2, 1); // You'll need to map exercise name to exercise_id
            pstmt.setInt(3, log.getSets());
            pstmt.setInt(4, log.getReps());
            pstmt.setDouble(5, log.getWeightUsed());
            pstmt.setObject(6, log.getDate());
            
            int rowsAffected = pstmt.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        log.setId(generatedKeys.getInt(1));
                    }
                }
            }
            
            System.out.println("✓ Workout log saved with ID: " + log.getId());
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Save workout log error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Deletes a goal by its ID.
     */
    public boolean deleteGoal(int goalId) {
        String sql = "DELETE FROM goals WHERE goal_id = ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, goalId);
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("✓ Goal deleted with ID: " + goalId);
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("✗ Delete goal error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
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