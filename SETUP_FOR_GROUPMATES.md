# 🚀 FitTrack Setup Guide for Team Members

## ✅ Quick Start (5 Minutes)

### Prerequisites
You need:
- **Java 21** (or higher)
- **Maven 3.6+** (or higher)
- **Git** (to clone the repository)

---

## 📥 Step 1: Clone the Repository

```bash
# Clone the project
git clone <your-repo-url>
cd fitness_app
```

---

## 📦 Step 2: Install Dependencies

```bash
# This downloads JavaFX, SQLite JDBC driver, and BCrypt
mvn clean install
```

**Expected output:**
```
[INFO] BUILD SUCCESS
[INFO] Total time: 10-30 seconds
```

---

## 🗄️ Step 3: Database Setup

**Good news:** The database is already configured!

### Option A: Use Shared Database (Recommended for Team)
If `fittrack.db` exists in the project folder:
- ✅ You'll have the same data as everyone else
- ✅ Includes test user: `ranzel_j`
- ✅ No setup needed!

### Option B: Create Your Own Database
If `fittrack.db` doesn't exist (not committed to Git):

```bash
# Run database setup
mvn exec:java -Dexec.mainClass="com.fittrack.DatabaseSetup"
```

**Expected output:**
```
==============================================
   FitTrack Database Setup
==============================================

1. Testing database connection...
   ✓ Database connection successful!

2. Creating database tables...
   ✓ All 9 tables created successfully!

==============================================
   ✓ Database setup complete!
==============================================
```

---

## ▶️ Step 4: Run the Application

```bash
mvn javafx:run
```

**Expected:** Login screen appears!

### Test Login Credentials
- **Username:** `ranzel_j` (if using shared database)
- **Password:** (ask team for password)

OR

- **Register** a new account by clicking "Register here"

---

## 🧪 Step 5: Verify Everything Works

1. **Login** or **Register**
2. **Update Profile** - Add age, height, weight
3. **Track Progress** - Add a weight entry
4. **Set Goals** - Create a fitness goal
5. **Food Log** - Log a meal
6. **Workout Plans** - Create a workout plan

**Check console output for:**
```
✓ Database connected: fittrack.db
✓ Profile updated in database!
✓ Weight history saved with ID: 1
✓ Goal saved to database with ID: 1
```

---

## 🔧 Troubleshooting

### Issue: "Database connection failed"
**Fix:**
```bash
# Make sure you're in the project root folder
cd fitness_app

# Re-run setup
mvn exec:java -Dexec.mainClass="com.fittrack.DatabaseSetup"
```

### Issue: "JavaFX components missing"
**Fix:**
```bash
# Clean and reinstall dependencies
mvn clean install -U
```

### Issue: "java.lang.ClassNotFoundException: org.sqlite.JDBC"
**Fix:**
```bash
# Update dependencies
mvn dependency:resolve
mvn clean install
```

### Issue: "Maven not found"
**Fix (Windows):**
1. Download Maven: https://maven.apache.org/download.cgi
2. Extract to `C:\Program Files\Apache\Maven`
3. Add to PATH: `C:\Program Files\Apache\Maven\bin`
4. Restart terminal and run `mvn --version`

**Fix (Mac):**
```bash
brew install maven
```

**Fix (Linux):**
```bash
sudo apt install maven  # Ubuntu/Debian
sudo yum install maven  # RHEL/CentOS
```

### Issue: "Java version mismatch"
**Fix:**
1. Download Java 21: https://adoptium.net/
2. Set `JAVA_HOME` environment variable
3. Run `java -version` to verify

---

## 📂 Project Structure

```
fitness_app/
├── fittrack.db                    ← SQLite database file (auto-created)
├── pom.xml                        ← Maven configuration
├── src/
│   └── main/
│       ├── java/com/fittrack/
│       │   ├── FitTrackApp.java           ← Main application
│       │   ├── controller/                ← UI Controllers
│       │   │   ├── LoginController.java
│       │   │   ├── DashboardController.java
│       │   │   ├── ProfileController.java
│       │   │   ├── ProgressController.java
│       │   │   ├── GoalsController.java
│       │   │   ├── FoodLogController.java
│       │   │   └── WorkoutPlansController.java
│       │   ├── model/                     ← Data Models
│       │   │   ├── DatabaseManager.java   ← Database operations
│       │   │   ├── User.java
│       │   │   ├── Goal.java
│       │   │   ├── WeightHistory.java
│       │   │   ├── FoodLog.java
│       │   │   └── WorkoutPlan.java
│       │   └── util/                      ← Utilities
│       │       ├── SessionManager.java
│       │       └── SceneSwitcher.java
│       └── resources/com/fittrack/view/   ← FXML UI files
│           ├── Login.fxml
│           ├── Dashboard.fxml
│           ├── Profile.fxml
│           ├── Progress.fxml
│           ├── Goals.fxml
│           ├── FoodLog.fxml
│           └── WorkoutPlans.fxml
└── target/                        ← Compiled files (auto-generated)
```

---

## 🗄️ Database Information

### Database Type
- **SQLite** - File-based database (no server needed!)
- **Location:** `fittrack.db` in project root folder
- **Size:** ~100KB-10MB (grows with data)

### Database Tables
1. `users` - User accounts and profiles
2. `goals` - Fitness goals
3. `workout_plans` - Workout plans
4. `plan_exercises` - Exercises in workout plans
5. `exercises` - Exercise library
6. `workout_log` - Workout history
7. `weight_history` - Weight tracking
8. `food_library` - Food database
9. `food_log` - Daily food intake

### View Database Contents
Use **DB Browser for SQLite** (free):
1. Download: https://sqlitebrowser.org/
2. Open `fittrack.db` in the project folder
3. Browse all tables and data

---

## 🤝 Working as a Team

### Sharing Database Data
The `fittrack.db` file is **committed to Git** so everyone has the same data.

**When you pull changes:**
```bash
git pull origin main
```

You'll get the latest database with all team data!

**When you add test data:**
```bash
# Add your changes
git add fittrack.db
git commit -m "Added test data: 5 users, 10 goals"
git push
```

### Individual Development
If you want your **own separate database** for testing:

```bash
# Rename shared database
mv fittrack.db fittrack_shared.db

# Create your own
mvn exec:java -Dexec.mainClass="com.fittrack.DatabaseSetup"

# Now you have fittrack.db (yours) and fittrack_shared.db (team's)
```

---

## ✅ Platform Compatibility

This project works on:
- ✅ **Windows 10/11**
- ✅ **macOS** (Intel & Apple Silicon)
- ✅ **Linux** (Ubuntu, Fedora, etc.)

SQLite automatically handles:
- Different file paths
- Different operating systems
- Different Java versions (11+)

---

## 📞 Need Help?

1. **Check console output** for error messages (look for ✗ symbols)
2. **Read error messages carefully** - they usually tell you what's wrong
3. **Ask the team** in your group chat
4. **Check existing issues** in the Git repository
5. **Create a new issue** with:
   - Your operating system
   - Java version (`java -version`)
   - Maven version (`mvn --version`)
   - Full error message from console
   - What you were trying to do

---

## 🎯 Next Steps

Once everything works:
1. **Explore the code** in `src/main/java/com/fittrack/`
2. **Read documentation:**
   - `LOGIC_ERRORS_ANALYSIS.md` - Known issues and fixes
   - `DATA_PERSISTENCE_FIXED.md` - Database implementation details
   - `TESTING_CHECKLIST.md` - How to test all features
3. **Start developing!**

---

**Happy Coding! 🚀**
