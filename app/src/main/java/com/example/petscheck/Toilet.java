package com.example.petscheck;

public class Toilet {
    private String dateTime;
    private String date;
    private String petName; // Имя питомца
    private float kaki; // Число походов в туалет

    public Toilet() {
    }

    public Toilet(String dateTime,String date, String petName, float kaki) {
        this.dateTime = dateTime;
        this.date = date;
        this.petName = petName;
        this.kaki = kaki;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getDate() {return date; }

    public String getPetName() {
        return petName;
    }

    public float getKaki() {
        return kaki;
    }
}



