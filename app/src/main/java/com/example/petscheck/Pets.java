package com.example.petscheck;

public class Pets {
    public String id, name, age, weight, breed, visit, imageId;

    public Pets() {
    }

    public Pets(String id, String name, String age, String weight, String breed, String visit, String imageId) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.weight = weight;
        this.breed = breed;
        this.visit = visit;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public String getWeight() {
        return weight;
    }

    public String getBreed() {
        return breed;
    }

    public String getVisit() {
        return visit;
    }

    public String getImageId() {
        return imageId;
    }
}