package com.example.david.testapp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 6/11/2017.
 */

public class Data {

    private int id;
    private String name;
    private String boon;
    private String bane;
    private int rarity;


    public Data(int id, String name, int rarity, String boon, String bane) {
        this.id = id;
        this.name = name;
        this.boon = boon;
        this.bane = bane;
        this.rarity = rarity;
    }

    public String getName(){ return this.name;}
    public int getRarity(){return this.rarity;}
    public int getId() { return this.id;}
    public String getBoon() {return this.boon;}
    public String getBane() {return this.bane;}
}