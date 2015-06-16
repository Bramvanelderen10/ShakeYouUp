package com.groep5.shakeyouup;

/**
 * Created by Bram on 6/16/2015.
 */
public class RouteCoordinate {
    private int id;
    private GeoCoordinate coordinate;
    private Route route;

    public RouteCoordinate(){};

    public GeoCoordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(GeoCoordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
