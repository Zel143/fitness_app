# FitTrack - Fitness Tracking Application

A JavaFX-based fitness tracking application with a Model-View-Controller (MVC) architecture.

## 🎯 Current Status: Working WITHOUT Database

**Good News!** You can build and test your entire application right now without needing the MySQL database connection. This project uses mock data for development and testing.

## 📁 Project Structure

```
db_fittrack/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── fittrack/
│       │           ├── FitTrackApp.java          # Main application entry point
│       │           ├── model/                    # Data models (POJOs)
│       │           │   ├── User.java
│       │           │   ├── Goal.java
│       │           │   ├── WorkoutPlan.java
│       │           │   └── PlanExercise.java
│       │           ├── controller/               # UI Controllers
│       │           │   ├── LoginController.java
│       │           │   ├── RegisterController.java
│       │           │   ├── ProfileController.java
│       │           │   └── DashboardController.java
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
│                       └── Dashboard.fxml
└── pom.xml                                       # Maven configuration
```

## 🚀 How to Run

### Prerequisites
- Java 21 or higher (LTS)
- Maven 3.6 or higher

### Running the Application

1. **Using Maven:**
   ```bash
   mvn clean javafx:run
   ```

2. **Or compile and run:**
   ```bash
   mvn clean compile
   mvn javafx:run
   ```

### Test Login Credentials
- **Username:** `test`
- **Password:** `test`

## ✨ Features (Currently Working)

### ✅ What You Can Do NOW (No Database Needed)

1. **Login Screen** - Test navigation and UI
2. **Registration Screen** - Validate form inputs
3. **User Profile** - Edit age, gender, height, weight, fitness level
4. **Dashboard** - View navigation and user stats (BMI calculation)
5. **Session Management** - User state persists across screens
6. **Scene Switching** - Smooth navigation between all screens

### 🔧 What's Using Mock Data

All controllers use **mock logic** instead of database calls:
- Login uses hardcoded credentials (`test/test`)
- Registration validates inputs but doesn't save to database
- Profile updates work in-memory only
- All user data is stored in the `SessionManager` singleton

## 📝 How the Mock System Works

### Example: LoginController

```java
// THIS IS MOCK LOGIC - Works without database
if (username.equals("test") && password.equals("test")) {
    User fakeUser = new User();
    fakeUser.setUsername("test");
    fakeUser.setEmail("test@fittrack.com");
    SessionManager.getInstance().setLoggedInUser(fakeUser);
    SceneSwitcher.switchScene(event, "Dashboard.fxml");
}

// THIS IS REAL LOGIC - Currently commented out
/*
User user = dbManager.login(username, password);
if (user != null) {
    SessionManager.getInstance().setLoggedInUser(user);
    SceneSwitcher.switchScene(event, "Dashboard.fxml");
}
*/
```

## 🎨 Editing FXML Files

You can edit FXML files in two ways:

1. **Scene Builder** (Recommended)
   - Download from: https://gluonhq.com/products/scene-builder/
   - Open any `.fxml` file in Scene Builder
   - Drag and drop components visually

2. **Text Editor**
   - Open `.fxml` files directly in VS Code
   - Edit XML to modify the UI

## 🛠️ Next Steps

### What You Can Build Right Now

1. **Additional FXML Views:**
   - `Goals.fxml` - View and create fitness goals
   - `WorkoutPlans.fxml` - Browse workout plans
   - `Progress.fxml` - Track workout progress

2. **Additional Controllers:**
   - `GoalsController.java`
   - `WorkoutPlansController.java`
   - `ProgressController.java`

3. **CSS Styling:**
   - Create `src/main/resources/com/fittrack/view/styles.css`
   - Customize colors, fonts, and layouts

4. **More Models:**
   - `Exercise.java`
   - `ProgressLog.java`
   - `Measurement.java`

### When Database is Ready

Simply uncomment the real logic in each controller and comment out the mock logic:

```java
// Comment out this:
// if (username.equals("test") && password.equals("test")) { ... }

// Uncomment this:
User user = dbManager.login(username, password);
if (user != null) {
    SessionManager.getInstance().setLoggedInUser(user);
    SceneSwitcher.switchScene(event, "Dashboard.fxml");
}
```

## 📚 Learning Resources

- **JavaFX Documentation:** https://openjfx.io/
- **FXML Guide:** https://docs.oracle.com/javafx/2/fxml_get_started/jfxpub-fxml_get_started.htm
- **Scene Builder:** https://gluonhq.com/products/scene-builder/

## 🐛 Common Issues

### Issue: JavaFX classes not found
**Solution:** Run `mvn clean install` to download dependencies

### Issue: Can't run application
**Solution:** Make sure you're using Java 21+ and run `mvn javafx:run`

### Issue: FXML file not loading
**Solution:** Check that the file path in the controller matches the actual file location

## 💡 Tips

1. **Test Everything Without Database:** You can complete 90% of your UI work right now
2. **Use Scene Builder:** It's much faster than editing FXML by hand
3. **Check the Console:** All mock operations print helpful messages with ✓ and ✗ symbols
4. **SessionManager is Global:** The logged-in user is available from any controller

## 📞 Support

If you encounter issues:
1. Check the console output for error messages
2. Verify your Java and Maven versions
3. Make sure all FXML files are in the correct location
4. Try `mvn clean install` to refresh dependencies

---

**Happy Coding! 🎉** You can now build your entire UI without waiting for the database to work!
