package com.groep5.shakeyouup;

import java.util.List;

/**
 * Created by Bram on 6/4/2015.
 */
public class Journey {
    private MotionSensor ms;
    private DatabaseManager dm;
    private long startTime;
    private int rating;

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
        calculateFinalScore();

        return true;
    }

    public void save(String[] location) {
        Route route = new Route();
        route.setStartLocation(location[0]);
        route.setEndLocation(location[1]);

        route.setTime(getCurrentTime());

        route.setScore(rating);

        dm.addRoute(route);

        List<Route> routes = dm.getAllRoutes();
        routes.size();
    }

    //TODO calculate score based on motion time and distance
    private float calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();

        float movementScore = vector[0] + vector[1] + vector[2];
        finalScore = (int) movementScore;
        long timer = getCurrentTime(); //TODO Maybe do something with this..
        long distance = 1*1000; //Replace 1 with methode from gps class

        //First we divide the movement score by the distance so we get the average score for each meter
        float movementPerMeter = movementScore / distance;

        rating = 1;
        if (movementPerMeter < 10) rating = 10;
        if (movementPerMeter < 20) rating = 9;
        if (movementPerMeter < 30) rating = 8;
        if (movementPerMeter < 40) rating = 10;
        if (movementPerMeter < 50) rating = 10;
        if (movementPerMeter < 60) rating = 10;
        if (movementPerMeter < 70) rating = 10;
        if (movementPerMeter < 80) rating = 10;
        if (movementPerMeter < 90) rating = 10;
        if (movementPerMeter < 100) rating = 10;

        return rating;
    }

    public int getRating() {

        return rating;
    }

    public float updateScore() {
        float[] vector = this.ms.updateScore();

        return vector[0] + vector[1] + vector[2];
    }







    public void setMotionSensor(MotionSensor ms) {
        this.ms = ms;
    }

    public long getCurrentTime() {
        java.util.Date date = new java.util.Date();
        long currentTime = date.getTime();
        long timer = (currentTime - startTime)/1000;
        return timer;
    }

    public int getFinalScore() {
        return finalScore;
    }
}
