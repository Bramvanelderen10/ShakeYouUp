package com.groep5.shakeyouup;

/**
 * Created by Bram on 6/18/2015.
 */
public class RouteMovement {
    private int id;
    private double movement;
    private long time;
    private Route route;

    public RouteMovement() {}

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

    public double getMovement() {
        return movement;
    }

    public void setMovement(double movement) {
        this.movement = movement;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
