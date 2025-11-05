 
# FitTrack - Comprehensive Fitness Tracking Application

A modern, feature-rich JavaFX desktop application for tracking fitness goals, nutrition, workouts, and progress. Built with a robust Model-View-Controller (MVC) architecture and powered by a local SQLite database.

## 🎯 Current Status: ✅ Production Ready

This is a **complete, self-contained desktop application** with all core features fully implemented and tested. The application uses a **serverless SQLite database**, eliminating the need for external database servers or complex configuration.

### Recent Improvements
- ✅ **Migrated from MySQL to SQLite** - Zero-configuration database setup
- ✅ **Fixed all data persistence issues** - Proper save/load operations across all modules
- ✅ **Implemented Command Pattern** - Undo/Redo functionality for food log entries
- ✅ **Enhanced UI/UX** - Professional Material Design-inspired interface
- ✅ **Comprehensive testing** - All CRUD operations verified and working

## ✨ Core Features

### 🔐 **User Authentication & Security**
- Secure user registration with email validation
- Password hashing using **jBCrypt** (BCrypt algorithm with salt)
- Session management across application lifecycle
- Secure login with credential verification

### 📊 **Dashboard & Navigation**
- Centralized navigation hub with quick access to all modules
- Real-time BMI calculation and display
- User greeting with personalized information
- Clean, intuitive interface design

### 👤 **Profile Management**
- Complete user profile with customizable fields:
  - Age, gender, height, weight
  - Fitness level (Beginner, Intermediate, Advanced, Professional)
- Profile data persistence across sessions
- BMI calculation and health metrics

### 🎯 **Goal Setting & Tracking**
- Create custom fitness goals with specific targets
- Set target dates and track progress
- Multiple goal types supported:
  - Weight Loss/Gain
  - Muscle Building
  - Endurance Training
  - Custom goals
- Edit and delete goals with confirmation dialogs
- Database-backed persistence

### 💪 **Workout Plan Management**
- Design comprehensive workout plans
- Track plan details:
  - Plan name and description
  - Difficulty level
  - Duration (in weeks)
  - Target goals
- Save and manage multiple workout plans
- Full CRUD operations with database integration

### 📈 **Progress Tracking**
- **Interactive Weight Chart**: Visualize weight changes over time with JavaFX LineChart
- **Statistics Dashboard**: 
  - Starting weight
  - Current weight
  - Total weight change
  - Progress trends
- Add, view, and delete weight entries
- Date-based tracking with automatic sorting
- Data persistence and reload functionality

### 🍽️ **Food Logging & Nutrition**
- Comprehensive food diary with detailed tracking:
  - Food name and portion size
  - Calories
  - Macronutrients (Protein, Carbs, Fats)
  - Meal type and date
- **Daily Totals Dashboard**: Real-time calculation of:
  - Total calories consumed
  - Total protein, carbs, and fats
  - Date-filtered summaries
- **Command Pattern Implementation**:
  - Undo/Redo functionality for food entries
  - Command history tracking
  - Non-destructive editing
- Filter by date for daily nutrition analysis

### 🔄 **Advanced Features**
- **Session Management**: Singleton pattern for user state tracking
- **Command History**: Undo/Redo pattern for reversible operations
- **Scene Switching**: Flexible navigation system supporting multiple event types
- **Toast Notifications**: User feedback for actions (success/error messages)
- **Data Validation**: Input validation across all forms
- **Confirmation Dialogs**: Prevent accidental deletions


## 🛠️ Technology Stack

### Core Technologies
- **Language**: Java 21
- **UI Framework**: JavaFX 21.0.2 (with FXML for declarative UI)
- **Database**: SQLite 3.44.1.0 (serverless, file-based)
- **Build Tool**: Apache Maven
- **Security**: jBCrypt 0.4 (industry-standard password hashing)

### Design Patterns & Architecture
- **MVC (Model-View-Controller)**: Clean separation of concerns
- **DAO (Data Access Object)**: DatabaseManager for all database operations
- **Singleton Pattern**: SessionManager, CommandHistory
- **Command Pattern**: Undo/Redo functionality
- **Observer Pattern**: JavaFX properties for reactive UI updates

### Key Libraries
- **JavaFX Controls**: Rich UI components (TableView, LineChart, DatePicker)
- **JDBC**: Standard database connectivity
- **SLF4J**: Logging framework

## 📁 Project Structure

```
fitness_app-3/
├── src/
│   └── main/
│       ├── java/com/fittrack/
│       │   ├── FitTrackApp.java              # Application entry point
│       │   ├── DatabaseSetup.java            # Database initialization utility
│       │   │
│       │   ├── model/                        # Data Layer (POJOs + DAO)
│       │   │   ├── DatabaseManager.java      # Database access & CRUD operations
│       │   │   ├── User.java                 # User entity
│       │   │   ├── Goal.java                 # Goal entity
│       │   │   ├── WorkoutPlan.java          # Workout plan entity
│       │   │   ├── PlanExercise.java         # Exercise details
│       │   │   ├── WeightHistory.java        # Weight tracking entity
│       │   │   ├── FoodLog.java              # Food entry entity
│       │   │   └── WorkoutLog.java           # Workout log entity
│       │   │
│       │   ├── controller/                   # Presentation Layer
│       │   │   ├── LoginController.java      # Login screen logic
│       │   │   ├── RegisterController.java   # Registration logic
│       │   │   ├── DashboardController.java  # Main dashboard
│       │   │   ├── ProfileController.java    # Profile management
│       │   │   ├── GoalsController.java      # Goal CRUD operations
│       │   │   ├── WorkoutPlansController.java # Workout plan management
│       │   │   ├── ProgressController.java   # Weight tracking & charts
│       │   │   └── FoodLogController.java    # Food logging with Undo/Redo
│       │   │
│       │   ├── command/                      # Command Pattern Implementation
│       │   │   ├── AddFoodLogCommand.java    # Add food entry command
│       │   │   └── DeleteFoodLogCommand.java # Delete food entry command
│       │   │
│       │   └── util/                         # Utility Classes
│       │       ├── SessionManager.java       # User session singleton
│       │       ├── SceneSwitcher.java        # Navigation utility
│       │       ├── Command.java              # Command interface
│       │       ├── CommandHistory.java       # Undo/Redo manager
│       │       └── ToastNotification.java    # User feedback system
│       │
│       └── resources/com/fittrack/view/      # View Layer (FXML)
│           ├── Login.fxml                    # Login UI
│           ├── Register.fxml                 # Registration UI
│           ├── Dashboard.fxml                # Dashboard UI
│           ├── Profile.fxml                  # Profile UI
│           ├── Goals.fxml                    # Goals management UI
│           ├── WorkoutPlans.fxml             # Workout plans UI
│           ├── Progress.fxml                 # Progress tracking UI
│           └── FoodLog.fxml                  # Food logging UI
│
├── target/                                   # Compiled classes & resources
├── pom.xml                                   # Maven configuration
├── fittrack.db                               # SQLite database (auto-generated)
├── .gitignore                                # Git ignore rules
│
└── Documentation/                            # Project documentation
    ├── README.md                             # This file
    ├── QUICKSTART.md                         # Quick start guide
    ├── SETUP_FOR_GROUPMATES.md              # Team setup instructions
    ├── DATABASE_MIGRATION_SUMMARY.md         # MySQL → SQLite migration notes
    ├── DATA_PERSISTENCE_FIXED.md            # Persistence fixes documentation
    ├── TESTING_CHECKLIST.md                 # QA testing guide
    └── LOGIC_ERRORS_ANALYSIS.md             # Known issues & fixes
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
