package com.example.skinwise.Model;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ModelAdmin {
    private String uid, email,name,phnum,password, state,timestamp,profileImage,role;

    public ModelAdmin() {
    }

    public ModelAdmin(String uid, String email, String name, String phnum, String password, String state, String timestamp, String profileImage, String role) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.phnum = phnum;
        this.password = password;
        this.state = state;
        this.timestamp = timestamp;
        this.profileImage = profileImage;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhnum() {
        return phnum;
    }

    public void setPhnum(String phnum) {
        this.phnum = phnum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
