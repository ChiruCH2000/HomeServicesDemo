package com.example.homeservicesdemo.models;
public class DayDateModel {
    private String day;
    private String date;
    private boolean selected;

    public DayDateModel(String day, String date) {
        this.day = day;
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public String getDate() {
        return date;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
