package com.bsit212.harmony;

public class ItemData {
    private String username;
    private String email;

    public ItemData(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }
}