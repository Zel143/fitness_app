# FitTrack - Fitness Tracking Application

A comprehensive JavaFX desktop application for tracking fitness goals, nutrition, workouts, and progress. Built with MVC architecture and powered by SQLite.

## 🎯 Status: Production Ready

Complete, self-contained desktop application with zero-configuration SQLite database.

## 🚀 Quick Start

```powershell
mvn clean install
mvn javafx:run
```

**Full setup instructions:** See [SETUP.md](SETUP.md)

## ✨ Features

### Core Functionality
- **User Authentication** - Secure registration/login with BCrypt
- **Profile Management** - Track age, height, weight, BMI, fitness level
- **Goal Setting** - Create and track fitness goals with target dates
- **Workout Plans** - Design workout schedules and log daily exercises
- **Food Log** - Track nutrition with undo/redo support
- **Progress Tracking** - Weight history with interactive charts

### Technical Highlights
- MVC architecture with clean separation
- SQLite database (zero configuration)
- SLF4J structured logging
- Command Pattern for undo/redo
- Session management with Singleton pattern
- Comprehensive JUnit + Mockito tests (95% passing)

## 🏗️ Architecture

```
src/main/java/com/fittrack/
├── FitTrackApp.java          # Application entry
├── controller/               # UI Controllers
├── model/                    # Data models + DatabaseManager
├── util/                     # SessionManager, SceneSwitcher
└── command/                  # Command pattern implementations

src/main/resources/com/fittrack/view/
└── *.fxml                    # JavaFX UI definitions
```

## 📚 Tech Stack

- Java 21
- JavaFX 21.0.2
- SQLite 3.44.1.0
- Maven
- SLF4J 2.0.9
- BCrypt 0.4
- JUnit 5 + Mockito

## 📖 Documentation

- **[SETUP.md](SETUP.md)** - Complete setup and usage guide
- **[docs/PRESENTATION_GUIDE.md](docs/PRESENTATION_GUIDE.md)** - Presentation walkthrough
- **[docs/MENTOR_PROTOCOL.md](docs/MENTOR_PROTOCOL.md)** - Development protocol
- **[AI_USAGE.md](AI_USAGE.md)** - AI usage transparency

## 🧪 Testing

```powershell
mvn test
```

**Coverage:** 38/40 tests passing (95%)

## 🐛 Troubleshooting

### Database Locked
Close all app instances and DB browsers, then restart.

### Missing Tables
Delete `fittrack.db` and run:
```powershell
mvn exec:java -Dexec.mainClass="com.fittrack.DatabaseSetup"
```

### JavaFX Missing
```powershell
mvn clean install -U
```

## 📄 License

Educational project - FitTrack Fitness Tracking Application
