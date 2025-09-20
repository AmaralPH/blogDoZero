package com.studytracker.auth;

public class AuthResponse {

    private String token;
    private String username;
    private String displayName;

    public AuthResponse(String token, String username, String displayName) {
        this.token = token;
        this.username = username;
        this.displayName = displayName;
    }

    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }
}
