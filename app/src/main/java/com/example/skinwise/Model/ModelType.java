package com.example.skinwise.Model;

public class ModelType {
    String imageURL,overview,symptoms,Cause,copyright,name;

    public ModelType() {
    }

    public ModelType(String imageURL, String overview, String symptoms, String cause, String copyright, String name) {
        this.imageURL = imageURL;
        this.overview = overview;
        this.symptoms = symptoms;
        this.Cause = cause;
        this.copyright = copyright;
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getCause() {
        return Cause;
    }

    public void setCause(String cause) {
        Cause = cause;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
