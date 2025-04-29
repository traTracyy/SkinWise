package com.example.skinwise.Model;

import java.util.List;

public class ModelReport {

    private String userId;
    private String description;
    private String reportId;
    private String contactInfo;
    private String dateTime;
    private List<String> images;

    public ModelReport() {
    }

    public ModelReport(String userId, String description, String reportId, String contactInfo, String dateTime, List<String> images) {
        this.userId = userId;
        this.description = description;
        this.reportId = reportId;
        this.contactInfo = contactInfo;
        this.dateTime = dateTime;
        this.images = images;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
