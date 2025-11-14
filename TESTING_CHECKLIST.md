# 🧪 Testing Checklist - FitTrack Application

## ✅ Complete Testing Guide

Test each feature to ensure data saves, displays, and persists correctly!

---

## 1️⃣ Profile Management

- [ ] **Update Profile:**
  1. Click "My Profile"
  2. Enter Age: `25`
  3. Select Gender: `Male`
  4. Enter Height: `175` cm
  5. Enter Weight: `70` kg
  6. Select Fitness Level: `Intermediate`
  7. Click "Save"
  8. **Expected:** "✓ Profile updated" message
  9. **Console:** Should show "✓ Profile updated for user: ranzel_j"

- [ ] **Verify Profile Persists:**
  1. Logout
  2. Login again
  3. Go to Profile
  4. **Expected:** All values still there

---

## 2️⃣ Track Progress (Weight History)

- [ ] **Add Weight Entry:**
  1. Click "Track Progress"
  2. Enter Weight: `70.5`
  3. Select Date: Today
  4. Click "Add Entry"
  5. **Expected:** Entry appears in table
  6. **Expected:** Chart updates with new point
  7. **Expected:** Stats update (Current Weight: 70.5 kg)
  8. **Console:** "✓ Weight history saved with ID: X"

- [ ] **Add Multiple Entries:**
  1. Add weight `71` for yesterday
  2. Add weight `69.5` for 2 days ago
  3. **Expected:** All 3 entries in table
  4. **Expected:** Chart shows trend line
  5. **Expected:** Stats show weight change

- [ ] **Delete Weight Entry:**
  1. Select an entry in the table
  2. Click "Delete Entry"
  3. Confirm deletion
  4. **Expected:** Entry removed from table
  5. **Expected:** Chart updates
  6. **Console:** "✓ Weight entry deleted from database with ID: X"

- [ ] **Verify Persistence:**
  1. Close application
  2. Restart application
  3. Login
  4. Go to "Track Progress"
  5. **Expected:** All weight entries still there
  6. **Expected:** Chart displays correctly

---

## 3️⃣ Set Goals

- [ ] **Add Goal:**
  1. Click "Set Goals"
  2. Select Goal Type: `Weight Loss`
  3. Enter Target Value: `65`
  4. Select Unit: `kg`
  5. Select Target Date: 30 days from now
  6. Click "Add Goal"
  7. **Expected:** Goal appears in table
  8. **Expected:** Status shows "active"
  9. **Console:** "✓ Goal saved to database with ID: X"

- [ ] **Add Different Goals:**
  1. Add goal: `Muscle Gain`, `75 kg`, 60 days from now
  2. Add goal: `Run Distance`, `5 km`, 14 days from now
  3. **Expected:** All goals in table

- [ ] **Delete Goal:**
  1. Select a goal
  2. Click "Delete Goal"
  3. Confirm deletion
  4. **Expected:** Goal removed from table
  5. **Console:** "✓ Goal deleted from database with ID: X"

- [ ] **Verify Persistence:**
  1. Close and restart app
  2. Login and check Goals
  3. **Expected:** All goals still there

---

## 4️⃣ Food Log

- [ ] **Add Food Entry:**
  1. Click "Food Log"
  2. Enter Food Name: `Chicken Breast`
  3. Enter Calories: `165`
  4. Enter Protein: `31`
  5. Enter Carbs: `0`
  6. Enter Fats: `3.6`
  7. Click "Add Food"
  8. **Expected:** Food appears in table
  9. **Expected:** Daily Totals update
  10. **Console:** "✓ Food entry saved to database with ID: X"

- [ ] **Add Multiple Foods:**
  1. Add: `Rice`, `200 calories`, `4g protein`, `44g carbs`, `0.4g fat`
  2. Add: `Banana`, `105 calories`, `1.3g protein`, `27g carbs`, `0.4g fat`
  3. **Expected:** Daily Totals = 470 calories, 36.3g protein, 71g carbs, 4.4g fat

- [ ] **Delete Food Entry:**
  1. Select a food entry
  2. Click "Delete Food"
  3. Confirm deletion
  4. **Expected:** Entry removed
  5. **Expected:** Daily totals recalculate
  6. **Console:** "✓ Food entry deleted from database with ID: X"

- [ ] **Verify Persistence:**
  1. Close and restart app
  2. Check Food Log
  3. **Expected:** All food entries still there

---

## 5️⃣ Workout Plans

- [ ] **Add Workout Plan:**
  1. Click "Workout Plans"
  2. Enter Plan Name: `Full Body Strength`
  3. Enter Description: `3x per week full body workout`
  4. Select Difficulty: `Intermediate`
  5. Enter Duration: `8` weeks
  6. Click "Save Plan"
  7. **Expected:** Plan appears in list
  8. **Console:** "✓ Workout plan saved with ID: X"

- [ ] **Add Multiple Plans:**
  1. Add: `Cardio HIIT`, `High intensity cardio`, `Advanced`, `4 weeks`
  2. Add: `Beginner Routine`, `Full body for beginners`, `Beginner`, `12 weeks`
  3. **Expected:** All plans in list

- [ ] **Delete Workout Plan:**
  1. Select a plan
  2. Click "Delete Plan"
  3. Confirm deletion
  4. **Expected:** Plan removed
  5. **Console:** "✓ Workout plan deleted with ID: X"

- [ ] **Verify Persistence:**
  1. Close and restart app
  2. Check Workout Plans
  3. **Expected:** All plans still there

---

## 6️⃣ Dashboard

- [ ] **Check Dashboard Stats:**
  1. After adding profile data, go to Dashboard
  2. **Expected:** Shows correct BMI calculation
  3. **Expected:** Shows age, height, weight, fitness level
  4. **Expected:** All navigation buttons work

---

## 🔍 Verification Methods

### Console Output Checklist:
When you perform operations, look for these messages:

**Successful Saves:**
```
✓ Profile updated for user: ranzel_j (rows affected: 1)
✓ Goal saved to database with ID: 1
✓ Workout plan saved with ID: 1
✓ Weight history saved with ID: 1
✓ Food entry saved to database with ID: 1
```

**Successful Deletes:**
```
✓ Goal deleted from database with ID: 1
✓ Workout plan deleted with ID: 1
✓ Weight entry deleted from database with ID: 1
✓ Food entry deleted from database with ID: 1
```

**Database Connections:**
```
✓ Database connected: fittrack.db
```

### Database Verification:
```powershell
mvn compile exec:java -D"exec.mainClass"="com.fittrack.VerifyData"
```

Should show all your data organized by tables.

---

## ❌ What to Watch For

### Issues to Report:
- ❌ "Failed to save" error messages
- ❌ Data disappears after app restart
- ❌ Duplicate entries in tables
- ❌ Delete doesn't work
- ❌ NULL pointer exceptions

### Expected Behavior:
- ✅ All saves show success messages
- ✅ Data reloads from database
- ✅ No duplicate entries
- ✅ Deletes work immediately
- ✅ No console errors

---

## 📊 Final Checklist

After completing all tests above:

- [ ] All features save data successfully
- [ ] All features load data from database
- [ ] All features delete data correctly
- [ ] Data persists after app restart
- [ ] No console errors
- [ ] Database file size > 57 KB (shows data is stored)
- [ ] All CRUD operations work (Create, Read, Update, Delete)

---

**If all checkboxes are ✅, your FitTrack application is production-ready!** 🎉

**Next:** Start using the app for real fitness tracking!
