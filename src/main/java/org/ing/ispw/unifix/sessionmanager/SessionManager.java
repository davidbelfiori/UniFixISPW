package org.ing.ispw.unifix.sessionmanager;

import org.ing.ispw.unifix.bean.UserBean;


public class SessionManager {
    private static SessionManager instance;
    private UserBean  currentUser;
    private SessionManager() {}

    //Instanziazione Lazy
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }
    public UserBean getCurrentUser() { return currentUser; }
    public void setCurrentUser(UserBean currentUser) { this.currentUser = currentUser; }
}
