package com.fittrack.util;

import java.util.Stack;

/**
 * Singleton class to manage command history for undo/redo operations
 * Maintains two stacks: one for undo, one for redo
 */
public class CommandHistory {
    private static CommandHistory instance;
    private final Stack<Command> undoStack = new Stack<>();
    private final Stack<Command> redoStack = new Stack<>();
    private final int MAX_HISTORY = 50; // Limit history size to prevent memory issues
    
    private CommandHistory() {}
    
    /**
     * Get the singleton instance
     */
    public static CommandHistory getInstance() {
        if (instance == null) {
            instance = new CommandHistory();
        }
        return instance;
    }
    
    /**
     * Execute a command and add it to history
     * Clears the redo stack since new action invalidates future states
     * @param command The command to execute
     * @return true if execution was successful
     */
    public boolean executeCommand(Command command) {
        boolean success = command.execute();
        if (success) {
            undoStack.push(command);
            redoStack.clear(); // Clear redo stack when new command is executed
            
            // Limit history size to prevent memory issues
            if (undoStack.size() > MAX_HISTORY) {
                undoStack.remove(0);
            }
            
            System.out.println("✓ Command executed: " + command.getDescription());
        }
        return success;
    }
    
    /**
     * Undo the last command
     * Moves the command from undo stack to redo stack
     * @return true if undo was successful
     */
    public boolean undo() {
        if (undoStack.isEmpty()) {
            System.out.println("⚠ Nothing to undo");
            return false;
        }
        
        Command command = undoStack.pop();
        boolean success = command.undo();
        
        if (success) {
            redoStack.push(command);
            System.out.println("✓ Undone: " + command.getDescription());
        } else {
            undoStack.push(command); // Put it back if undo failed
            System.err.println("✗ Failed to undo: " + command.getDescription());
        }
        
        return success;
    }
    
    /**
     * Redo the last undone command
     * Moves the command from redo stack back to undo stack
     * @return true if redo was successful
     */
    public boolean redo() {
        if (redoStack.isEmpty()) {
            System.out.println("⚠ Nothing to redo");
            return false;
        }
        
        Command command = redoStack.pop();
        boolean success = command.execute();
        
        if (success) {
            undoStack.push(command);
            System.out.println("✓ Redone: " + command.getDescription());
        } else {
            redoStack.push(command); // Put it back if redo failed
            System.err.println("✗ Failed to redo: " + command.getDescription());
        }
        
        return success;
    }
    
    /**
     * Check if undo is available
     * @return true if there are commands to undo
     */
    public boolean canUndo() {
        return !undoStack.isEmpty();
    }
    
    /**
     * Check if redo is available
     * @return true if there are commands to redo
     */
    public boolean canRedo() {
        return !redoStack.isEmpty();
    }
    
    /**
     * Clear all history (both undo and redo stacks)
     */
    public void clear() {
        undoStack.clear();
        redoStack.clear();
        System.out.println("ℹ Command history cleared");
    }
    
    /**
     * Get the description of the command that would be undone
     * @return description string, or empty if nothing to undo
     */
    public String getUndoDescription() {
        return undoStack.isEmpty() ? "" : undoStack.peek().getDescription();
    }
    
    /**
     * Get the description of the command that would be redone
     * @return description string, or empty if nothing to redo
     */
    public String getRedoDescription() {
        return redoStack.isEmpty() ? "" : redoStack.peek().getDescription();
    }
    
    /**
     * Get the current undo stack size
     * @return number of commands available for undo
     */
    public int getUndoCount() {
        return undoStack.size();
    }
    
    /**
     * Get the current redo stack size
     * @return number of commands available for redo
     */
    public int getRedoCount() {
        return redoStack.size();
    }
}
