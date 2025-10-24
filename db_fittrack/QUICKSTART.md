# Quick Start Guide - FitTrack Development

## 🎯 You Can Work on Your Project RIGHT NOW!

No database connection needed! Here's how to be productive immediately.

---

## Step 1: Verify Your Setup

Open a terminal in the project folder and run:

```powershell
mvn --version
java -version
```

You should see:
- Maven 3.6+
- Java 11+

---

## Step 2: Install Dependencies

```powershell
mvn clean install
```

This downloads JavaFX and sets up everything you need.

---

## Step 3: Run the Application

```powershell
mvn javafx:run
```

The Login screen should appear!

**Test Credentials:**
- Username: `test`
- Password: `test`

---

## Step 4: Explore the Application

1. **Login** with test/test
2. **Navigate to Profile** and add your info
3. **Check the Dashboard** - it calculates your BMI!
4. **Try registering** a new account (it validates but doesn't save)

---

## Step 5: Start Building!

### Option A: Edit FXML Files in Scene Builder

1. **Download Scene Builder:** https://gluonhq.com/products/scene-builder/
2. **Open any .fxml file** from `src/main/resources/com/fittrack/view/`
3. **Drag and drop** buttons, text fields, labels
4. **Save** and run the app again to see changes

### Option B: Create New Screens

**Example: Create a Goals screen**

1. **Create the FXML:**
   - File: `src/main/resources/com/fittrack/view/Goals.fxml`
   - Copy from `Dashboard.fxml` and modify

2. **Create the Controller:**
   - File: `src/main/java/com/fittrack/controller/GoalsController.java`
   - Copy from `DashboardController.java` and modify

3. **Link them together:**
   - In `Goals.fxml`, set: `fx:controller="com.fittrack.controller.GoalsController"`

4. **Add navigation:**
   - In `DashboardController.java`, uncomment the Goals button code

---

## Step 6: Understanding the Mock System

Every controller has two sections:

### Mock Logic (ACTIVE NOW)
```java
// --- THIS IS YOUR MOCK LOGIC ---
if (username.equals("test") && password.equals("test")) {
    User fakeUser = new User();
    fakeUser.setUsername("test");
    SessionManager.getInstance().setLoggedInUser(fakeUser);
    SceneSwitcher.switchScene(event, "Dashboard.fxml");
}
```

### Real Logic (COMMENTED OUT)
```java
// --- THIS IS YOUR REAL LOGIC (Commented out) ---
/*
User user = dbManager.login(username, password);
if (user != null) {
    SessionManager.getInstance().setLoggedInUser(user);
    SceneSwitcher.switchScene(event, "Dashboard.fxml");
}
*/
```

**When your database works:** Just swap them!

---

## Step 7: Test Your Work

After making changes:

```powershell
mvn clean javafx:run
```

Watch the console for messages:
- `✓` = Success
- `✗` = Error
- `ℹ` = Info

---

## 📝 Your To-Do List (No Database Needed!)

### Week 1: Build the UI
- [ ] Customize Login.fxml colors and fonts
- [ ] Add more fields to Profile.fxml
- [ ] Create Goals.fxml
- [ ] Create WorkoutPlans.fxml
- [ ] Create a Progress.fxml screen

### Week 2: Add Controllers
- [ ] GoalsController.java with mock goals
- [ ] WorkoutPlansController.java with mock plans
- [ ] ProgressController.java with mock progress data

### Week 3: Polish
- [ ] Add CSS styling (styles.css)
- [ ] Add input validation
- [ ] Add confirmation dialogs
- [ ] Add icons and images

### Week 4: Prepare for Database
- [ ] Test all screen navigation
- [ ] Verify all forms validate correctly
- [ ] Document what data each screen needs
- [ ] Write down all the database queries you'll need

---

## 🎨 Customization Ideas

### Add a Color Theme

Create: `src/main/resources/com/fittrack/view/styles.css`

```css
.root {
    -fx-background-color: #2c3e50;
}

.primary-button {
    -fx-background-color: #3498db;
    -fx-text-fill: white;
    -fx-font-weight: bold;
}

.primary-button:hover {
    -fx-background-color: #2980b9;
}
```

Then in `FitTrackApp.java`, uncomment:
```java
scene.getStylesheets().add(getClass().getResource("/com/fittrack/view/styles.css").toExternalForm());
```

### Add More Mock Users

In `LoginController.java`:
```java
// Add more test users
if (username.equals("test") && password.equals("test")) {
    // ... existing code
} else if (username.equals("admin") && password.equals("admin123")) {
    User adminUser = new User();
    adminUser.setUsername("admin");
    // ... set up admin user
}
```

---

## 🐛 Troubleshooting

### "Package javafx does not exist"
```powershell
mvn clean install
```

### "Cannot find FXML file"
Check that files are in: `src/main/resources/com/fittrack/view/`

### "Scene Builder can't open file"
Make sure the .fxml file is valid XML (check for syntax errors)

### "Application won't run"
```powershell
mvn clean
mvn compile
mvn javafx:run
```

---

## 💡 Pro Tips

1. **Save Often:** Run `mvn javafx:run` frequently to test changes
2. **Check Console:** Helpful debug messages are printed there
3. **Use Scene Builder:** 10x faster than editing FXML by hand
4. **SessionManager is Your Friend:** Access the logged-in user from anywhere
5. **Print Debug Info:** Use `System.out.println()` liberally

---

## 📞 Need Help?

- Check `README.md` for detailed documentation
- Look at existing controllers for examples
- Console messages will guide you
- All your model classes are ready to use!

---

**You're all set! Start building! 🚀**
