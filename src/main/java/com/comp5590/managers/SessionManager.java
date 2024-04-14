package com.comp5590.managers;

import com.comp5590.database.entities.User;
import lombok.Getter;
import lombok.Setter;

/* Session manager stores a boolean field to indicate whether or not the user is autheticated.
 * To be used in conjunction with a ScreenManager event listener (on screen change) and the decorator that will go before every DB call, to ensure user is authenticated before changing screens or fetching data from the DB.
 */

public class SessionManager {

    // Singleton instance
    public static SessionManager INSTANCE;

    // Boolean to indicate whether or not the user is authenticated
    private boolean authenticated;

    @Getter
    @Setter
    private User currentUser;

    /**
     * State message for transitory screens
     */
    @Getter
    @Setter
    private String stateMessage;

    // Private constructor
    private SessionManager() {
        this.authenticated = false; // set to false by default, on program start, as user is not logged in...
    }

    // Get the instance of the SessionManager
    public static SessionManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SessionManager();
        }
        return INSTANCE;
    }

    /**
     * Check if the user is authenticated
     * @return boolean
     */
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    /**
     * Authenticate the user
     * @param user The user to authenticate
     */
    public void authenticate(User user) {
        // set the current user
        this.currentUser = user;
        // set user as authenticated
        this.authenticated = true;
        // run the unauthenticateAfter method
        unauthenticateAfter(2);
    }

    /**
     * Unauthenticate the user
     */
    public void unauthenticate() {
        // unset the current user
        this.currentUser = null;
        // set authenticated to false
        this.authenticated = false;
    }

    /**
     * Unauthenticate the user after a certain amount of time
     * @param hours The amount of time to wait before unauthenticating the user
     */
    public void unauthenticateAfter(int hours) {
        // open new thread and run the unauthenticate method
        new Thread(() -> {
            try {
                Thread.sleep(hours * 60 * 60 * 1000);
                this.unauthenticate();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })
            .start();
    }

    /**
     * Get the full name of the current user
     * @return The full name of the current user
     */
    public String getFullName() {
        return this.currentUser.getFirstName() + " " + this.currentUser.getSurName();
    }

    /**
     * Get whether the current user has 2fa enabled
     * @return boolean
     */
    public boolean has2FAEnabled() {
        return this.currentUser.getAuthenticationDetails().isTwoFactorEnabled();
    }
}
