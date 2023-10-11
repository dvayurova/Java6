package edu.school21.models;

public class User {
    private Long id;
    private String login;
    private String password;
    private boolean authenticationStatus;

    public User() {
    }

    public User(Long id, String login, String password, boolean authenticationStatus) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.authenticationStatus = authenticationStatus;
    }

    public Long getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public boolean isAuthenticationStatus() {
        return authenticationStatus;
    }

    public void setAuthenticationStatus(boolean authenticationStatus) {
        this.authenticationStatus = authenticationStatus;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
