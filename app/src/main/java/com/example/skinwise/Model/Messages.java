package com.example.skinwise.Model;

public class Messages {

    String message;
    String senderId;
    long timestamp;
    String currenttime;
    String senderProfileImageUrl;


    public Messages() {
    }

    public Messages(String message, String senderId, long timestamp, String currenttime, String senderProfileImageUrl) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
        this.currenttime = currenttime;
        this.senderProfileImageUrl = senderProfileImageUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getCurrenttime() {
        return currenttime;
    }

    public void setCurrenttime(String currenttime) {
        this.currenttime = currenttime;
    }

    public String getSenderProfileImageUrl() {
        return senderProfileImageUrl;
    }

    public void setSenderProfileImageUrl(String senderProfileImageUrl) {
        this.senderProfileImageUrl = senderProfileImageUrl;
    }
}


//
//    public Messages(String message, String senderId, long timestamp, String currenttime) {
//        this.message = message;
//        this.senderId = senderId;
//        this.timestamp = timestamp;
//        this.currenttime = currenttime;
//    }
//
//    public String getMessage() {
//        return message;
//    }
//
//    public void setMessage(String message) {
//        this.message = message;
//    }
//
//    public String getSenderId() {
//        return senderId;
//    }
//
//    public void setSenderId(String senderId) {
//        this.senderId = senderId;
//    }
//
//    public long getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(long timestamp) {
//        this.timestamp = timestamp;
//    }
//
//    public String getCurrenttime() {
//        return currenttime;
//    }
//
//    public void setCurrenttime(String currenttime) {
//        this.currenttime = currenttime;
//    }
//}
//
