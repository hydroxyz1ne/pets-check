package com.example.petscheck;

import java.util.ArrayList;
import java.util.List;

public class Pets {
    public String id, name, age, weight, breed, visit, imageId,qrcode;
    public ArrayList<Toilet> toilets;


    public Pets() {
    }

    public Pets(String id, String name, String age, String weight, String breed, String visit, String imageId, ArrayList<Toilet> toilets,String qrcode) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.breed = breed;
        this.visit = visit;
        this.imageId = imageId;
        this.toilets = toilets;
        this.qrcode = qrcode;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getAge() {
        return age;
    }

    public String getWeight() {
        return weight;
    }

    public String getVisit() {
        return visit;
    }
    public String getImageId() {
        return imageId;
    }
    public List<Toilet> getToilets() { return toilets; }
    public String getQrcode() {return qrcode;}
}