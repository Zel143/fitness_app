package com.fittrack.command;

import com.fittrack.model.DatabaseManager;
import com.fittrack.model.FoodLog;
import com.fittrack.util.Command;

import javafx.collections.ObservableList;

/**
 * Command to delete a food log entry
 * Supports undo/redo functionality by preserving the original position
 */
public class DeleteFoodLogCommand implements Command {
    private final DatabaseManager dbManager;
    private final FoodLog foodLog;
    private final ObservableList<FoodLog> foodLogList;
    private final int originalIndex;
    
    public DeleteFoodLogCommand(DatabaseManager dbManager, FoodLog foodLog, ObservableList<FoodLog> foodLogList) {
        this.dbManager = dbManager;
        this.foodLog = foodLog;
        this.foodLogList = foodLogList;
        this.originalIndex = foodLogList.indexOf(foodLog);
    }
    
    @Override
    public boolean execute() {
        boolean success = dbManager.deleteFoodLog(foodLog.getId());
        if (success) {
            foodLogList.remove(foodLog);
        }
        return success;
    }
    
    @Override
    public boolean undo() {
        // Re-save to database (will get new ID)
        boolean success = dbManager.saveFoodLog(foodLog);
        if (success) {
            // Re-insert at original position if possible
            if (originalIndex >= 0 && originalIndex <= foodLogList.size()) {
                foodLogList.add(originalIndex, foodLog);
            } else {
                foodLogList.add(foodLog);
            }
        }
        return success;
    }
    
    @Override
    public String getDescription() {
        return "Delete food: " + foodLog.getFoodName();
    }
}
