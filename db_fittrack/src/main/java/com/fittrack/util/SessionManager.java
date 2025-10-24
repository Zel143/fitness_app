package com.fittrack.util;

import com.fittrack.model.User;

/**
 * SessionManager - Singleton class to manage the logged-in user session
 * This class maintains the current user's state throughout the application
 */
public class SessionManager {
    
    // Singleton instance
    private static SessionManager instance;
    
    // The currently logged-in user
    private User loggedInUser;
    
    // Private constructor to prevent instantiation
    private SessionManager() {
    }
    
    /**
     * Get the singleton instance of SessionManager
     * @return the SessionManager instance
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    
    /**
     * Get the currently logged-in user
     * @return the logged-in User object, or null if no user is logged in
     */
    public User getLoggedInUser() {
        return loggedInUser;
    }
    
    /**
     * Set the currently logged-in user
     * @param user the User object to set as logged in
     */
    public void setLoggedInUser(User user) {
        this.loggedInUser = user;
    }
    
    /**
     * Check if a user is currently logged in
     * @return true if a user is logged in, false otherwise
     */
    public boolean isUserLoggedIn() {
        return loggedInUser != null;
    }
    
    /**
     * Clear the current session (logout)
     */
    public void logout() {
        this.loggedInUser = null;
    }
    
    /**
     * Get the current user's ID
     * @return the user ID, or -1 if no user is logged in
     */
    public int getCurrentUserId() {
        return loggedInUser != null ? loggedInUser.getUserId() : -1;
    }
    
    /**
     * Get the current user's username
     * @return the username, or null if no user is logged in
     */
    public String getCurrentUsername() {
        return loggedInUser != null ? loggedInUser.getUsername() : null;
    }
}
