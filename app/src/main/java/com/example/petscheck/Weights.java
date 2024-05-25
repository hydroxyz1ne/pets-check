package com.example.petscheck;

public class Weight {
    private String dateTime;
    private String date;
    private String petName;

    private int weight;

    public Weight() {
    }

    public Weight(String dateTime, String date, String petName, int weight) {
        this.dateTime = dateTime;
        this.date = date;
        this.petName = petName;
        this.weight = weight;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDate() {
        return date;
    }

    public String getPetName() {
        return petName;
    }

    public int getWeight() {
        return weight;
    }
}
