# FitTrack Development TODO List

## ✅ Completed

- [x] Project structure setup
- [x] Model classes (User, Goal, WorkoutPlan, PlanExercise)
- [x] Login screen with mock authentication
- [x] Registration screen with validation
- [x] User profile screen
- [x] Dashboard screen
- [x] SessionManager singleton
- [x] SceneSwitcher utility
- [x] Basic navigation between screens
- [x] BMI calculation in dashboard
- [x] Maven configuration with JavaFX

---

## 🚀 Ready to Build (No Database Needed)

### High Priority - UI Screens

- [ ] **Goals Screen** (`Goals.fxml` + `GoalsController.java`)
  - [ ] TableView to display goals
  - [ ] Add Goal button
  - [ ] Edit Goal functionality
  - [ ] Delete Goal with confirmation
  - [ ] Mock goals data (List<Goal>)
  
- [ ] **Workout Plans Screen** (`WorkoutPlans.fxml` + `WorkoutPlansController.java`)
  - [ ] ListView of workout plans
  - [ ] Plan details view
  - [ ] Create new plan form
  - [ ] Mock workout plans data
  
- [ ] **Progress Tracking Screen** (`Progress.fxml` + `ProgressController.java`)
  - [ ] Chart showing weight over time
  - [ ] Log new workout entry
  - [ ] View workout history
  - [ ] Mock progress data

- [ ] **Exercise Library Screen** (`Exercises.fxml` + `ExercisesController.java`)
  - [ ] Search exercises
  - [ ] Filter by muscle group
  - [ ] Exercise details view
  - [ ] Mock exercise database

### Medium Priority - Enhancements

- [ ] **CSS Styling**
  - [ ] Create `styles.css`
  - [ ] Define color scheme
  - [ ] Style buttons (primary, secondary, danger)
  - [ ] Add hover effects
  - [ ] Responsive font sizes
  
- [ ] **Form Validation Improvements**
  - [ ] Email format validation (regex)
  - [ ] Password strength indicator
  - [ ] Real-time validation feedback
  - [ ] Disable submit until valid
  
- [ ] **User Experience**
  - [ ] Add confirmation dialogs (logout, delete)
  - [ ] Add loading indicators
  - [ ] Add success/error toast notifications
  - [ ] Keyboard shortcuts (Enter to submit)
  
- [ ] **Profile Enhancements**
  - [ ] Add profile picture (file chooser)
  - [ ] Add more fitness metrics
  - [ ] Show BMI category (underweight/normal/overweight)
  - [ ] Add goal weight field

### Low Priority - Polish

- [ ] **Visual Design**
  - [ ] Add app icon
  - [ ] Add images/icons to buttons
  - [ ] Add background images
  - [ ] Create app logo
  
- [ ] **Additional Features**
  - [ ] Settings screen
  - [ ] Help/About screen
  - [ ] Export data to CSV
  - [ ] Print workout plans
  
- [ ] **Data Models**
  - [ ] Create Exercise.java
  - [ ] Create ProgressLog.java
  - [ ] Create Measurement.java
  - [ ] Create Nutrition.java

---

## 🗄️ Future: Database Integration

### When MySQL is Working

- [ ] **Database Setup**
  - [ ] Create DatabaseManager.java
  - [ ] Create connection pool
  - [ ] Create all tables (users, goals, workout_plans, etc.)
  - [ ] Add sample data
  
- [ ] **Replace Mock Logic**
  - [ ] LoginController - real authentication
  - [ ] RegisterController - insert new users
  - [ ] ProfileController - update user profile
  - [ ] GoalsController - CRUD operations
  - [ ] WorkoutPlansController - CRUD operations
  
- [ ] **Security**
  - [ ] Add PasswordHasher utility (BCrypt)
  - [ ] Hash all passwords
  - [ ] Add input sanitization (SQL injection prevention)
  - [ ] Add session timeout
  
- [ ] **Data Persistence**
  - [ ] Save goals to database
  - [ ] Save workout plans to database
  - [ ] Save progress logs to database
  - [ ] Load user data on login

---

## 📚 Learning Goals

### JavaFX Concepts to Explore

- [ ] TableView and ObservableList
- [ ] Charts (LineChart, BarChart, PieChart)
- [ ] File choosers (for images)
- [ ] Custom dialogs
- [ ] CSS pseudo-classes
- [ ] FXML includes (reusable components)
- [ ] Animations and transitions
- [ ] Multithreading (for database calls)

### Java Concepts to Practice

- [ ] Streams and Lambda expressions
- [ ] Date/Time API (LocalDate, LocalDateTime)
- [ ] Exception handling
- [ ] Collections (List, Map, Set)
- [ ] Singleton pattern (already done!)
- [ ] Factory pattern (for creating mock data)

---

## 🎯 Weekly Milestones

### Week 1: Foundation (DONE! ✅)
- [x] Setup project structure
- [x] Create all model classes
- [x] Build login/register/profile/dashboard
- [x] Implement navigation

### Week 2: Core Features
- [ ] Build Goals screen
- [ ] Build Workout Plans screen
- [ ] Add CSS styling
- [ ] Add form validation

### Week 3: Advanced Features
- [ ] Build Progress tracking
- [ ] Add charts
- [ ] Build Exercise library
- [ ] Add images and icons

### Week 4: Polish & Testing
- [ ] Add all dialogs and confirmations
- [ ] Test all user flows
- [ ] Fix bugs
- [ ] Write documentation

### Week 5+: Database Ready
- [ ] Create DatabaseManager
- [ ] Replace all mock logic
- [ ] Test with real data
- [ ] Deploy application

---

## 🐛 Known Issues / Future Fixes

- [ ] No password recovery feature
- [ ] No "Remember Me" checkbox
- [ ] No user settings/preferences
- [ ] No data export feature
- [ ] No multi-language support
- [ ] Window size not saved between sessions

---

## 💡 Feature Ideas (Brainstorm)

### Cool Features to Add Later
- [ ] Social features (share workouts with friends)
- [ ] Workout timer
- [ ] Exercise video tutorials (links)
- [ ] Nutrition tracking
- [ ] Calorie calculator
- [ ] Rest day suggestions
- [ ] Achievement badges
- [ ] Workout statistics dashboard
- [ ] Custom exercise creator
- [ ] Workout plan templates

### Integration Ideas
- [ ] Export to PDF
- [ ] Email workout plans
- [ ] Sync with fitness trackers (Fitbit, etc.)
- [ ] Mobile app companion
- [ ] Web version

---

## 📝 Notes

### Things to Remember
- Always use `SessionManager` to get current user
- Always use `SceneSwitcher` to change screens
- Mock data should be in controllers (for now)
- Check console for debug messages (✓ ✗ ℹ ⚠)

### Code Style
- Use meaningful variable names
- Add JavaDoc comments to public methods
- Keep methods under 30 lines when possible
- Use helper methods for repeated code

### Testing Checklist
- [ ] Test with empty inputs
- [ ] Test with invalid inputs (negative numbers, etc.)
- [ ] Test navigation from every screen
- [ ] Test logout from every screen
- [ ] Test with different user profiles

---

## 🎓 Resources

### Tutorials to Check Out
- JavaFX TableView tutorial
- JavaFX Charts tutorial
- CSS for JavaFX
- Scene Builder advanced features
- JavaFX Dialogs and Alerts

### Documentation Links
- [JavaFX Documentation](https://openjfx.io/)
- [Scene Builder Guide](https://docs.oracle.com/javafx/scenebuilder/)
- [Maven Docs](https://maven.apache.org/guides/)
- [Java 21 API](https://docs.oracle.com/en/java/javase/21/docs/api/)

---

**Last Updated:** October 24, 2025

**Current Status:** ✅ Core application complete and working with mock data!

**Next Priority:** Build Goals screen and add CSS styling
