 
Here is the corrected `README.md` file, updated to reflect that the project runs on **SQLite**.

-----

# FitTrack - Fitness Tracking Application

A JavaFX-based fitness tracking application with a Model-View-Controller (MVC) architecture, using a local SQLite database.

## 🎯 Current Status: ✅ Fully Operational

This project is a complete, self-contained desktop application. It uses a **serverless SQLite database**, meaning no external database setup is required. All features listed are fully implemented.

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
  * **Database**: **SQLite** (Serverless, file-based)
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
│       │           ├── TestConnection.java       # Utility to create tables
│       │           ├── model/                    # Data models (POJOs)
│       │           ├── controller/               # UI Controllers
│       │           └── util/                     # Utility classes
│       └── resources/
│           └── com/
│               └── fittrack/
│                   └── view/                     # FXML UI files
├── pom.xml                                       # Maven configuration
└── fittrack.db                                   # <-- SQLite DB file (created at runtime)
```

## 🚀 How to Run

### 1\. Prerequisites

  * Java 21 (or higher)
  * Apache Maven

### 2\. Database Setup

**No database setup is required\!**

SQLite is a serverless, file-based database. The database file (e.g., `fittrack.db`) and all necessary tables will be created **automatically** in the project's root directory the first time you run the application.

The `TestConnection.java` or `DatabaseSetup.java` file can be run once manually if needed, but the application is likely configured to initialize the database on its own.

### 3\. Running the Application

1.  **Install dependencies** (Make sure your `pom.xml` lists `sqlite-jdbc` instead of `mysql-connector-java`):
    ```bash
    mvn clean install
    ```
2.  **Run the application**:
    ```bash
    mvn javafx:run
    ```

The application will start, presenting you with the Login screen. You can now register a new account and begin using the app.

## 🐛 Common Issues

### Issue: `java.sql.SQLException: database file is locked`

**Solution:** This means another process is using the `fittrack.db` file. Close any other running instances of the app or any database browser tools (like DB Browser for SQLite) that might have the file open.

### Issue: `java.sql.SQLException: no such table: users`

**Solution:** The database file (`fittrack.db`) was created, but the tables were not. This can happen if the app was closed during its very first initialization. Delete the `fittrack.db` file and restart the application to allow it to be created properly.

### Issue: JavaFX classes not found

**Solution:** Run `mvn clean install` to download all dependencies as defined in `pom.xml`.
