package com.bsit212.harmony;

class ChatroomModel {
    private String user1;
    private String user2;

    public ChatroomModel() {
        // Required for Firestore
    }

    public ChatroomModel(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }
}
