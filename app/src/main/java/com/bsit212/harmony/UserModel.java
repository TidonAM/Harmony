package com.bsit212.harmony;

import com.google.firebase.Timestamp;

public class UserModel {
    private String username;
    private String email;
    private Timestamp timestampCreated;

    public UserModel(String username, String email, Timestamp timestampCreated) {
        this.username = username;
        this.email = email;
        this.timestampCreated = timestampCreated;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getTimestampCreated() {
        return timestampCreated;
    }

    public void setTimestampCreated(Timestamp timestampCreated) {
        this.timestampCreated = timestampCreated;
    }
}
