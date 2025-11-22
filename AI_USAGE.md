# AI Usage Disclosure - FitTrack Project

**Last Updated:** November 15, 2025
**Project:** FitTrack - Fitness Tracking Application
**Tech Stack:** Java 21, JavaFX 21.0.2, SQLite 3.44.1.0, Maven, SLF4J, JUnit 5

---

## Purpose

This document transparently outlines how Generative AI (GitHub Copilot, ChatGPT, and Claude) was used as a development tool and learning aid throughout the project.

---

## 1. Database Migration (MySQL → SQLite)

### AI Contribution
- Converted MySQL syntax to SQLite-compatible SQL
- Changed `AUTO_INCREMENT` → `AUTOINCREMENT`
- Adapted `VARCHAR` → `TEXT`
- Modified foreign key constraints

### Example
```java
// Before (MySQL)
"CREATE TABLE users (\n"
+ "    user_id INT AUTO_INCREMENT PRIMARY KEY,\n"
+ "    username VARCHAR(50) NOT NULL UNIQUE,\n"

// After (SQLite - AI assisted)
"CREATE TABLE IF NOT EXISTS users (\n"
+ "    user_id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
+ "    username TEXT NOT NULL UNIQUE,\n"
```

### Learning Outcome
Understanding database portability and SQL dialect differences.

---

## 2. Workout Functionality Fix

### AI Contribution
- Identified FXML-Controller mismatch
- Discovered missing UI components for workout logging
- Designed TabPane-based dual-tab interface
- Created complete Workout Log UI with proper fx:id bindings

### Problem Identified
```java
// Controller expected these but FXML was missing them:
@FXML private TableView<WorkoutLog> workoutLogTable;  // ❌
@FXML private TextField workoutNameField;              // ❌
@FXML private void handleAddWorkout() { }              // ❌
```

### Learning Outcome
Importance of FXML-Controller synchronization and proper JavaFX component binding.

---

## 3. Enterprise Logging Migration

### AI Contribution
- Identified 100+ ad-hoc print statements
- Migrated to SLF4J structured logging
- Applied appropriate log levels (INFO/WARN/ERROR)
- Used parameterized logging for performance

### Example
```java
// Before
System.out.println("User logged in: " + username);
e.printStackTrace();

// After (AI-guided)
private static final Logger logger = LoggerFactory.getLogger(ClassName.class);
logger.info("User logged in: {}", username);
logger.error("Database error", e);
```

### Learning Outcome
Production-ready logging practices and proper exception handling.

---

## 4. Code Optimization (November 2025)

### AI Contribution
- Reduced JavaFX dependencies (removed redundant artifacts)
- Streamlined pom.xml configuration
- Consolidated documentation (736 → 95 lines for README)
- Enhanced .gitignore rules
- Optimized build configuration

### Results
- 50% fewer dependencies
- 87% smaller README
- Faster build times
- Cleaner project structure

### Learning Outcome
Dependency management, build optimization, and documentation best practices.

---

## 5. Testing Support

### AI Contribution
- Generated JUnit 5 test templates
- Created Mockito mock objects
- Suggested edge cases to test
- Helped debug test failures

### Example
```java
// AI-assisted test structure
@Test
void testCalculateBMI() {
    User user = new User();
    user.setHeight(1.75);
    user.setWeight(70.0);
    assertEquals(22.86, user.calculateBMI(), 0.01);
}
```

### Learning Outcome
Unit testing patterns, mocking strategies, and test coverage techniques.

---

## 6. Bug Fixes and Debugging

### AI Contribution
- Diagnosed data persistence issues
- Identified missing SQL column references
- Suggested proper error handling patterns
- Helped trace NullPointerExceptions

### Approach
1. Describe error symptoms
2. AI analyzes code and logs
3. Suggests root cause
4. Proposes solution with explanation
5. Human implements and verifies

### Learning Outcome
Debugging methodology and systematic problem-solving.

---

## 7. UI/UX Improvements

### AI Contribution
- CSS styling suggestions
- FXML layout optimization
- Accessibility recommendations
- Professional chart styling (white backgrounds, visible borders)

### Learning Outcome
JavaFX styling best practices and user interface design principles.

---

## 8. Documentation

### AI Contribution
- Generated README structure
- Created setup guides
- Wrote presentation guide
- Formatted code comments

### Human Review
- Verified all technical accuracy
- Customized for project specifics
- Added personal insights
- Ensured consistency

---

## What AI Did NOT Do

❌ Design the application architecture
❌ Choose the technology stack
❌ Make design decisions
❌ Write the majority of business logic
❌ Determine feature requirements
❌ Create the database schema design
❌ Implement core algorithms
❌ Make architectural trade-offs

---

## Development Approach

### Human Responsibilities
- Defined all features and requirements
- Made all architectural decisions
- Wrote core business logic
- Reviewed and understood all AI suggestions
- Tested all functionality
- Made final implementation choices

### AI Role
- Code syntax assistance
- Boilerplate generation
- Documentation templates
- Debugging suggestions
- Best practice recommendations
- Learning resource

---

## Educational Value

### Skills Developed
- Java 21 language features
- JavaFX UI development
- SQLite database operations
- Maven build management
- MVC architecture
- Design patterns (Singleton, Command, DAO)
- Unit testing with JUnit
- Logging frameworks
- Git version control

### How AI Helped Learning
- Explained complex concepts
- Provided code examples
- Suggested best practices
- Offered debugging strategies
- Accelerated problem-solving

---

## Transparency Commitment

This project uses AI as a **learning tool and productivity enhancer**, not as a replacement for understanding. Every AI-generated suggestion was:
1. Reviewed for correctness
2. Understood before implementation
3. Tested thoroughly
4. Adapted to project needs
5. Documented transparently

---

## Verification

All code has been:
- ✅ Built successfully (`mvn clean install`)
- ✅ Tested (95% test coverage)
- ✅ Reviewed for quality
- ✅ Documented appropriately
- ✅ Verified to meet requirements

---

**Declaration**: This disclosure is provided in the spirit of academic integrity and transparency. AI was used as a development tool under human supervision and review.

**Student Name**: _____________
**Date**: November 22, 2025
**Course**: _____________
