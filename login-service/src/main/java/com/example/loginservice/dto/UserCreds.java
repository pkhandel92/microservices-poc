package com.example.loginservice.dto;

public class UserCreds {
    private String username;
    private CharSequence password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CharSequence getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
