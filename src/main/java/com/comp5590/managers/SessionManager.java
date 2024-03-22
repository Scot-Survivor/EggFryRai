package com.comp5590.managers;

/* Session manager stores a boolean field to indicate whether or not the user is autheticated.
 * To be used in conjunction with a ScreenManager event listener (on screen change) and the decorator that will go before every DB call, to ensure user is authenticated before changing screens or fetching data from the DB.
 */

public class SessionManager {

    // Singleton instance
    public static SessionManager INSTANCE;

    // Boolean to indicate whether or not the user is authenticated
    private boolean authenticated;

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

    // Get the authenticated status of the user
    public boolean isAuthenticated() {
        return this.authenticated;
    }

    // Set the authenticated status of the user
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    // Unauthenticate the user after a certain amount of time
    public void unauthenticateAfter(int hours) {
        // TODO: Implement this
        // set sleep, then set authenticated to false
        // try {
        // Thread.sleep(hours * 60 * 60 * 1000);
        // this.authenticated = false;
        // } catch (InterruptedException e) {
        // e.printStackTrace();
        // }

        
        }
    }

