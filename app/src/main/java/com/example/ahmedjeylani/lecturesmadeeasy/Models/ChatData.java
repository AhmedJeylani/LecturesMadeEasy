package com.example.ahmedjeylani.lecturesmadeeasy.Models;

public class ChatData {
    private String message,username,uniqueID, messageTime, chatLikes, senderID;

    public ChatData() {}

    public ChatData(String uniqueID,String username,String message,String messageTime,String chatLikes, String senderID) {

        this.message = message;
        this.username = username;
        this.uniqueID = uniqueID;
        this.messageTime = messageTime;
        this.chatLikes = chatLikes;
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void setUniqueID(String uniqueID) {
        this.uniqueID = uniqueID;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getChatLikes() {
        return chatLikes;
    }

    public void setChatLikes(String chatLikes) {
        this.chatLikes = chatLikes;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }
}
