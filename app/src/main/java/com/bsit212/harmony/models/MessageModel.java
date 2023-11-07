package com.bsit212.harmony.models;

import com.google.firebase.Timestamp;

public class MessageModel {
    private String sender;
    private String text;
    private Timestamp timestamp;

    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public MessageModel() {

    }

    public MessageModel(String sender, String text, Timestamp timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }
}
