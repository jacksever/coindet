package com.sever.coinsdetector.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Coin {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;
    public String year;
    public int priceMsk;
    public int priceSpb;

    public String country;
    public String material;
    public String edge;

    public String urlAvers;
    public String urlRevers;

    public double weight;
    public double diameter;
    public double thickness;
}
