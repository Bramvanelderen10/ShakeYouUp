package com.groep5.shakeyouup;

import java.util.List;

/**
 * Created by Bram on 6/4/2015.
 */
public class Journey {
    private MotionSensor ms;
    private DatabaseManager dm;
    private float startTime;
    private int finalScore;

    public Journey(DatabaseManager dm) {
        this.dm = dm;
    }

    public boolean start() {
        java.util.Date date = new java.util.Date();
        startTime = date.getTime();

        return true;
    }

    public boolean stop() {
        finalScore = (int) calculateFinalScore();

        return true;
    }

    //TODO calculate score based on motion time and distance
    private float calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();

        return vector[0] + vector[1] + vector[2];
    }

    public int getFinalScore() {

        return finalScore;
    }

    public float getCurrentScore() {
        float[] vector = this.ms.getTotalVector();

        return vector[0] + vector[1] + vector[2];
    }

    public float getCurrentTime() {
        java.util.Date date = new java.util.Date();
        float currentTime = date.getTime();

        return currentTime - startTime;
    }

    public void setMotionSensor(MotionSensor ms) {
        this.ms = ms;
    }

    public void save(String[] location) {
        Route route = new Route();
        route.setStartLocation(location[0]);
        route.setEndLocation(location[1]);

        route.setTime((int)getCurrentTime());

        route.setScore(finalScore);

        dm.addRoute(route);

        List<Route> routes = dm.getAllRoutes();
        routes.size();
    }
}
