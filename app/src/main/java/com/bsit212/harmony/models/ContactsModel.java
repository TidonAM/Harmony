package com.bsit212.harmony.models;

public class ContactsModel {
    private String chatroomId;
    private UserModel user;
    private MessageModel lastMessage;
    public ContactsModel(String chatroomId, UserModel user, MessageModel lastMessage) {
        this.chatroomId = chatroomId;
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public String getChatroomId() {
        return chatroomId;
    }

    public void setChatroomId(String chatroomId) {
        this.chatroomId = chatroomId;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public MessageModel getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(MessageModel lastMessage) {
        this.lastMessage = lastMessage;
    }
}
