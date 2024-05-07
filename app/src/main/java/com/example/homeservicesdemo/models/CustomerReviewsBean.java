package com.example.homeservicesdemo.models;

public class CustomerReviewsBean {
    private String name;
    private String description;
    private String rating;
    private String date;
    public CustomerReviewsBean(String name, String description, String rating, String date) {
        this.name = name;
        this.description = description;
        this.rating = rating;
        this.date = date;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
