package com.groep5.shakeyouup;

import java.util.Locale;

/**
 * Created by Bram on 6/11/2015.
 */
public class Location {
    private int id;
    private String name;
    private double[] coordinates;

    public Location(){};

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }
}
