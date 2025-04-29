package com.example.skinwise.Model;

public class ModelHistory {
    private String uid, date,time, imageURL,SL1,SL2,SL3,SL4,SL5,SL6,SL7,SL8,lesionType,hisuid,AnatomicalSite,Gender,Age;

    public ModelHistory() {
    }

    public ModelHistory(String uid, String date, String time, String imageURL, String SL1, String SL2, String SL3, String SL4, String SL5, String SL6, String SL7, String SL8, String lesionType, String hisuid, String anatomicalSite, String gender, String age) {
        this.uid = uid;
        this.date = date;
        this.time = time;
        this.imageURL = imageURL;
        this.SL1 = SL1;
        this.SL2 = SL2;
        this.SL3 = SL3;
        this.SL4 = SL4;
        this.SL5 = SL5;
        this.SL6 = SL6;
        this.SL7 = SL7;
        this.SL8 = SL8;
        this.lesionType = lesionType;
        this.hisuid = hisuid;
        this.AnatomicalSite = anatomicalSite;
        this.Gender = gender;
        this.Age = age;
    }

    public String getAnatomicalSite() {
        return AnatomicalSite;
    }

    public void setAnatomicalSite(String anatomicalSite) {
        AnatomicalSite = anatomicalSite;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSL1() {
        return SL1;
    }

    public void setSL1(String SL1) {
        this.SL1 = SL1;
    }

    public String getSL2() {
        return SL2;
    }

    public void setSL2(String SL2) {
        this.SL2 = SL2;
    }

    public String getSL3() {
        return SL3;
    }

    public void setSL3(String SL3) {
        this.SL3 = SL3;
    }

    public String getSL4() {
        return SL4;
    }

    public void setSL4(String SL4) {
        this.SL4 = SL4;
    }

    public String getSL5() {
        return SL5;
    }

    public void setSL5(String SL5) {
        this.SL5 = SL5;
    }

    public String getSL6() {
        return SL6;
    }

    public void setSL6(String SL6) {
        this.SL6 = SL6;
    }

    public String getSL7() {
        return SL7;
    }

    public void setSL7(String SL7) {
        this.SL7 = SL7;
    }

    public String getSL8() {
        return SL8;
    }

    public void setSL8(String SL8) {
        this.SL8 = SL8;
    }

    public String getLesionType() {
        return lesionType;
    }

    public void setLesionType(String lesionType) {
        this.lesionType = lesionType;
    }

    public String getHisuid() {
        return hisuid;
    }

    public void setHisuid(String hisuid) {
        this.hisuid = hisuid;
    }
}
