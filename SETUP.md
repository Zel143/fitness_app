# FitTrack - Setup & Quick Start Guide

## 🚀 Quick Start (5 Minutes)

### Prerequisites
- **Java 21+**
- **Maven 3.6+**

### Installation

```powershell
# 1. Install dependencies
mvn clean install

# 2. Run the application
mvn javafx:run
```

### First Use
- **Register** a new account, or
- **Login** with existing credentials (if database exists)

---

## 📦 Project Structure

```
fitness_app/
├── pom.xml                    # Maven configuration
├── fittrack.db               # SQLite database (auto-created)
├── src/main/
│   ├── java/com/fittrack/
│   │   ├── FitTrackApp.java          # Application entry
│   │   ├── controller/               # UI Controllers
│   │   ├── model/                    # Data models + DatabaseManager
│   │   ├── util/                     # SessionManager, SceneSwitcher
│   │   └── command/                  # Command pattern (undo/redo)
│   └── resources/com/fittrack/view/  # FXML files + CSS
└── src/test/                 # JUnit tests
```

---

## 🔧 Development Commands

```powershell
# Clean build
mvn clean compile

# Run application
mvn javafx:run

# Run tests
mvn test

# Create executable JAR
mvn clean package
# Output: target/fittrack-1.0-SNAPSHOT.jar
```

---

## ✨ Features

### Core Functionality
- **User Authentication** - Secure registration/login with BCrypt password hashing
- **Profile Management** - Track age, height, weight, fitness level, BMI
- **Goal Setting** - Create and track fitness goals with target dates
- **Workout Plans** - Design workout schedules and log daily exercises
- **Food Log** - Track nutrition with undo/redo support (Command Pattern)
- **Progress Tracking** - Weight history with interactive charts

### Technical Features
- **MVC Architecture** - Clean separation of concerns
- **SQLite Database** - Zero-configuration, portable
- **SLF4J Logging** - Enterprise-grade structured logging
- **JavaFX UI** - Modern desktop interface with FXML
- **Session Management** - Singleton pattern for user state
- **Command Pattern** - Undo/redo for food log operations

---

## 🗄️ Database

**Location:** `fittrack.db` (auto-created in project root)

**Tables:**
- users
- goals
- workout_plans
- plan_exercises
- workout_logs
- weight_history
- food_log

**Reset Database:**
```powershell
# Delete and reinitialize
Remove-Item fittrack.db
mvn exec:java -Dexec.mainClass="com.fittrack.DatabaseSetup"
```

---

## 🧪 Testing

```powershell
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=UserTest
mvn test -Dtest=GoalTest
mvn test -Dtest=DatabaseManagerTest
```

**Test Coverage:** 38/40 tests passing (95%)

---

## 🐛 Troubleshooting

### Database Connection Failed
```powershell
mvn exec:java -Dexec.mainClass="com.fittrack.DatabaseSetup"
```

### JavaFX Components Missing
```powershell
mvn clean install -U
```

### Java/Maven Not Found
- **Java:** Download from [Adoptium](https://adoptium.net/)
- **Maven:** Download from [Apache Maven](https://maven.apache.org/download.cgi)

---

## 📚 Tech Stack

- **Java 21** - Core language
- **JavaFX 21.0.2** - Desktop UI framework
- **SQLite 3.44.1.0** - Embedded database
- **Maven** - Build tool
- **SLF4J 2.0.9** - Logging framework
- **BCrypt 0.4** - Password hashing
- **JUnit 5** - Testing framework
- **Mockito 5.7.0** - Mocking framework

---

## 📖 Documentation

- **[PRESENTATION_GUIDE.md](docs/PRESENTATION_GUIDE.md)** - Presentation walkthrough
- **[MENTOR_PROTOCOL.md](docs/MENTOR_PROTOCOL.md)** - Development protocol
- **[AI_USAGE.md](AI_USAGE.md)** - AI usage transparency

---

## 🎯 Next Steps

1. **Explore the UI** - Navigate through all screens
2. **Add Data** - Create profile, goals, workout plans
3. **Test Features** - Log food, track progress, view charts
4. **Customize** - Modify FXML files or CSS styling
5. **Extend** - Add new features or controllers

---

## 📄 License

Educational project - FitTrack Fitness Tracking Application
