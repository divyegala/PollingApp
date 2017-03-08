package shd.pollingapp;

/**
 * Created by Divye10 on 09-03-2017.
 */

public class User {
    protected String username;
    private String password;
    protected String role;
    private int isAuthenticated;

    protected User (String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.isAuthenticated = 0;
    }

    protected String getPassword() {
        return this.password;
    }

    protected int getAuthenticationStatus() {
        return this.isAuthenticated;
    }

    protected void setAuthenticationStatus(int isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }
}
