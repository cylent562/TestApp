package com.example.david.testapp;

/**
 * Created by David on 6/18/2017.
 */

public class Collection {
    private String name = "";
    private int imageRarity = -1;
    private int imageResource = -1;
    private int rarity = -1;
    private int id = -1;

    public Collection(int id, String name, int imageResource, int rarity, int imageRarity) {
        this.id = id;
        this.name = name;
        this.imageResource = imageResource;
        this.rarity = rarity;
        this.imageRarity = imageRarity;
    }

    public String getName(){ return this.name;}
    public int getRarityImage(){return this.imageRarity;}
    public int getImageResource() {return this.imageResource;}
    public int getRarity(){return this.rarity;}
    public int getId() {return this.id;}
}
