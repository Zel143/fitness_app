Here is the updated `README.md` file, reflecting the project's current state with full database integration and expanded features.

-----

# FitTrack - Fitness Tracking Application

A JavaFX-based fitness tracking application with a Model-View-Controller (MVC) architecture, fully integrated with a MySQL database.

## 🎯 Current Status: ✅ Fully Operational

This project is a complete desktop application that connects to a MySQL database to provide a comprehensive fitness tracking experience. All features listed are fully implemented.

## ✨ Features

  * **Secure User Authentication**: Register new accounts and log in securely. Passwords are hashed using **jBCrypt**.
  * **Main Dashboard**: Central navigation hub to access all application modules.
  * **User Profile Management**: Set and update personal details including age, gender, height, weight, and fitness level.
  * **Goal Setting**: Create, view, and delete custom fitness goals (e.g., "Weight Loss", "Run Distance") with target values and dates.
  * **Workout Plan Management**: Design, save, and manage custom workout plans with details like difficulty, duration, and descriptions.
  * **Progress Tracking**: Log weight entries and visualize progress over time with an interactive line chart. Includes statistics on starting weight, current weight, and total change.
  * **Food Logging**: Track daily food intake, including calories and macronutrients (protein, carbs, fats). A dashboard panel shows daily totals for the selected date.
  * **Session Management**: A central `SessionManager` tracks the logged-in user's state across the application.

## 🛠️ Technology Stack

  * **Core**: Java 21
  * **Framework**: JavaFX 21.0.2 (for UI)
  * **Database**: MySQL 8.0.33
  * **Build**: Apache Maven
  * **Security**: jBCrypt 0.4 (for password hashing)

## 📁 Project Structure

```
fittrack/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── fittrack/
│       │           ├── FitTrackApp.java          # Main application entry point
│       │           ├── DatabaseSetup.java        # Utility to create tables
│       │           ├── model/                    # Data models (POJOs)
│       │           │   ├── User.java
│       │           │   ├── Goal.java
│       │           │   ├── WorkoutPlan.java
│       │           │   ├── FoodLog.java
│       │           │   ├── WeightHistory.java
│       │           │   └── ...
│       │           ├── controller/               # UI Controllers
│       │           │   ├── LoginController.java
│       │           │   ├── RegisterController.java
│       │           │   ├── ProfileController.java
│       │           │   ├── DashboardController.java
│       │           │   ├── GoalsController.java
│       │           │   ├── FoodLogController.java
│       │           │   ├── ProgressController.java
│       │           │   └── WorkoutPlansController.java
│       │           └── util/                     # Utility classes
│       │               ├── SessionManager.java   # Manages logged-in user
│       │               └── SceneSwitcher.java    # Switches between screens
│       └── resources/
│           └── com/
│               └── fittrack/
│                   └── view/                     # FXML UI files
│                       ├── Login.fxml
│                       ├── Register.fxml
│                       ├── Profile.fxml
│                       ├── Dashboard.fxml
│                       ├── Goals.fxml
│                       ├── FoodLog.fxml
│                       ├── Progress.fxml
│                       └── WorkoutPlans.fxml
└── pom.xml                                       # Maven configuration
```

## 🚀 How to Run

### 1\. Prerequisites

  * Java 21 (or higher)
  * Apache Maven
  * A running MySQL server

### 2\. Database Setup

Before running the application, you must set up the MySQL database.

1.  **Connect to MySQL** using your preferred client.

2.  **Create the database**:

    ```sql
    CREATE DATABASE fittrack_db;
    ```

3.  **Create the user and grant privileges**:

    ```sql
    CREATE USER 'fittrack_admin'@'localhost' IDENTIFIED BY 'mySQL';
    GRANT ALL PRIVILEGES ON fittrack_db.* TO 'fittrack_admin'@'localhost';
    FLUSH PRIVILEGES;
    ```

    *Note: The user and password (`fittrack_admin`, `mySQL`) are defined in `DatabaseManager.java`. You can change them there if needed.*

4.  **Create Tables**:

      * The easiest way is to run the `src/main/java/com/fittrack/DatabaseSetup.java` file. Run it as a Java application (via your IDE or command line) once. It will connect to the database and execute all `CREATE TABLE` statements.

### 3\. Running the Application

1.  **Install dependencies**:
    ```bash
    mvn clean install
    ```
2.  **Run the application**:
    ```bash
    mvn javafx:run
    ```

The application will start, presenting you with the Login screen. You can now register a new account and begin using the app.

## 🐛 Common Issues

### Issue: `java.sql.SQLException: Access denied for user...`

**Solution:** Ensure the username and password in `DatabaseManager.java` match the user you created in MySQL. Also, verify you granted privileges to `fittrack_db`.

### Issue: `java.sql.SQLException: Unknown database 'fittrack_db'`

**Solution:** You forgot to run `CREATE DATABASE fittrack_db;` in MySQL before starting the app.

### Issue: `java.sql.SQLSyntaxErrorException: Table 'fittrack_db.users' doesn't exist`

**Solution:** You did not run the `DatabaseSetup.java` file to create the tables after setting up the database.

### Issue: JavaFX classes not found

**Solution:** Run `mvn clean install` to download all dependencies as defined in `pom.xml`.
