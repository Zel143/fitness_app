package com.fittrack.command;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.FoodLog;
import com.fittrack.util.Command;

import javafx.collections.ObservableList;

/**
 * Command to add a food log entry
 * Supports undo/redo functionality
 */
public class AddFoodLogCommand implements Command {
    private final DatabaseManager dbManager;
    private final FoodLog foodLog;
    private final ObservableList<FoodLog> foodLogList;
    
    public AddFoodLogCommand(DatabaseManager dbManager, FoodLog foodLog, ObservableList<FoodLog> foodLogList) {
        this.dbManager = dbManager;
        this.foodLog = foodLog;
        this.foodLogList = foodLogList;
    }
    
    @Override
    public boolean execute() {
        boolean success = dbManager.saveFoodLog(foodLog);
        if (success) {
            // Add to UI list only if not already present
            if (!foodLogList.contains(foodLog)) {
                foodLogList.add(0, foodLog); // Add at top (newest first)
            }
        }
        return success;
    }
    
    @Override
    public boolean undo() {
        boolean success = dbManager.deleteFoodLog(foodLog.getId());
        if (success) {
            foodLogList.remove(foodLog);
        }
        return success;
    }
    
    @Override
    public String getDescription() {
        return "Add food: " + foodLog.getFoodName();
    }
}
