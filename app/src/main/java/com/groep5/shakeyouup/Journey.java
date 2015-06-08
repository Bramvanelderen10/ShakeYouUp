package com.groep5.shakeyouup;

/**
 * Created by Bram on 6/4/2015.
 */
public class Journey {
    private MotionSensor ms;

    private DatabaseManager dm;

    private float startTime;

    public Journey(DatabaseManager dm) {
        this.dm = dm;

    }

    public boolean start() {
        java.util.Date date = new java.util.Date();
        startTime = date.getTime();

        return true;
    }

    public boolean stop() {
        Route route = new Route();
        route.setStartLocation("den haag");
        route.setEndLocation("rotterdam");

        route.setTime((int)getCurrentTime());

        route.setScore((int) calculateFinalScore());

        dm.addRoute(route);

        return true;
    }

    //TODO calculate score based on motion time and distance
    public float calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();

        return vector[0] + vector[1] + vector[2];
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
}
