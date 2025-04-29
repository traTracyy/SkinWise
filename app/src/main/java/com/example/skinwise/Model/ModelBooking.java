package com.example.skinwise.Model;

public class ModelBooking {
    private String date;
    private String time;
    private String userUid,dermUid;
    private String BookingID,userName,userEmail,userProfileImg,userPhone,dermName,dermEmail,dermPhone,dermAddress,dermState,dermShopImg,donestatus;

    public ModelBooking() {
    }

    public ModelBooking(String date, String BookingID, String userPhone,String time, String userUid, String dermUid, String userName, String userEmail, String userProfileImg, String dermName, String dermEmail, String dermPhone, String dermAddress, String dermState, String dermShopImg, String donestatus) {
        this.date = date;
        this.BookingID = BookingID;
        this.time = time;
        this.userPhone = userPhone;
        this.userUid = userUid;
        this.dermUid = dermUid;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userProfileImg = userProfileImg;
        this.dermName = dermName;
        this.dermEmail = dermEmail;
        this.dermPhone = dermPhone;
        this.dermAddress = dermAddress;
        this.dermState = dermState;
        this.dermShopImg = dermShopImg;
        this.donestatus = donestatus;
    }

    public String getDonestatus() {
        return donestatus;
    }

    public void setDonestatus(String donestatus) {
        this.donestatus = donestatus;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getUserphone() {
        return userPhone;
    }

    public void setUserphone(String userphone) {
        this.userPhone = userphone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public String getDermUid() {
        return dermUid;
    }

    public void setDermUid(String dermUid) {
        this.dermUid = dermUid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }

    public String getDermName() {
        return dermName;
    }

    public void setDermName(String dermName) {
        this.dermName = dermName;
    }

    public String getDermEmail() {
        return dermEmail;
    }

    public void setDermEmail(String dermEmail) {
        this.dermEmail = dermEmail;
    }

    public String getDermPhone() {
        return dermPhone;
    }

    public void setDermPhone(String dermPhone) {
        this.dermPhone = dermPhone;
    }

    public String getDermAddress() {
        return dermAddress;
    }

    public void setDermAddress(String dermAddress) {
        this.dermAddress = dermAddress;
    }

    public String getDermState() {
        return dermState;
    }

    public void setDermState(String dermState) {
        this.dermState = dermState;
    }

    public String getDermShopImg() {
        return dermShopImg;
    }

    public void setDermShopImg(String dermShopImg) {
        this.dermShopImg = dermShopImg;
    }
}
