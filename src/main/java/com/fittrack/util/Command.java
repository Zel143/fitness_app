package com.fittrack.util;

/**
 * Command interface for undo/redo operations
 * Implements the Command Pattern for reversible actions
 */
public interface Command {
    /**
     * Execute the command (perform the action)
     * @return true if successful, false otherwise
     */
    boolean execute();
    
    /**
     * Undo the command (reverse the action)
     * @return true if successful, false otherwise
     */
    boolean undo();
    
    /**
     * Get a human-readable description of the command
     * Used for displaying in undo/redo buttons
     * @return description string
     */
    String getDescription();
}
