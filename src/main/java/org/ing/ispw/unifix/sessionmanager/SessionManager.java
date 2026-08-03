package org.ing.ispw.unifix.sessionmanager;

import org.ing.ispw.unifix.bean.UserBean;

public class SessionManager {
    private static SessionManager instance;
    private UserBean currentUser;

    private SessionManager() {}

    // Istanziazione Lazy thread-safe
    public static synchronized SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public UserBean getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserBean currentUser) {
        this.currentUser = currentUser;
    }

    public void clearSession() {
        this.currentUser = null;
    }
}
