package com.example.weatherapp.models;

public class WeekWeatherModel {

    private String date;
    private int maxTemp;
    private int minTemp;
    private String icon;
    private String condition;

    public WeekWeatherModel(String date, int maxTemp, int minTemp, String icon, String condition) {
        this.date = date;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.icon = icon;
        this.condition = condition;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
