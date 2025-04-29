package com.example.skinwise.Model;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ModelDerm {
    private String uid, address,email,name,phnum,password, approve,shopname, state,timestamp,ShopImg,shopOpen,role,online;

    public ModelDerm() {
    }

    public ModelDerm(String uid, String address, String email, String name, String phnum, String password, String approve,  String shopname, String state,String online, String timestamp, String ShopImg, String shopOpen, String role) {
        this.uid = uid;
        this.address = address;
        this.email = email;
        this.name = name;
        this.phnum = phnum;
        this.password = password;
        this.shopname = shopname;
        this.online = online;
        this.state = state;
        this.timestamp = timestamp;
        this.ShopImg = ShopImg;
        this.shopOpen = shopOpen;
        this.role = role;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getApprove() {
        return approve;
    }

    public void setApprove(String approve) {
        this.approve = approve;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
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

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
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
        return ShopImg;
    }

    public void setProfileImage(String profileImage) {
        this.ShopImg = profileImage;
    }

    public String getShopOpen() {
        return shopOpen;
    }

    public void setShopOpen(String shopOpen) {
        this.shopOpen = shopOpen;
    }

}
