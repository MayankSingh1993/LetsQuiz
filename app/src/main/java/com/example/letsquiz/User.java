package com.example.letsquiz;

public class User {
    public String user, username,email;
    private String userId;

    public User() {
    }

    public User(String user, String username, String email) {
        this.user = user;
        this.username = username;
        this.email = email;
    }

    public String getUser() {
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void getUserId(String userId ) {
        this.userId = userId;
    }
}
