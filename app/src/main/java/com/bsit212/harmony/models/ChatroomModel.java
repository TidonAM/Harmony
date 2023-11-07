package com.bsit212.harmony.models;

import java.util.List;

public class ChatroomModel {
    private List<String> users;

    public ChatroomModel() {
        // Required for Firestore
    }

    public ChatroomModel(List<String> users) {
        this.users = users;
    }

    public List<String> getUsers() {
        return users;
    }
}
