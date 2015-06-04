package com.groep5.shakeyouup;

import java.security.Timestamp;

/**
 * Created by Bram on 6/4/2015.
 */
public class Route {
    private int score;

    private MotionSensor ms;

    private float startTime;

    public Route() {
        java.util.Date date = new java.util.Date();
        startTime = date.getTime();
    }

    //TODO calculate score based on motion time and distance
    public void calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();

        this.score = 10;
    }



    //TODO Save route in database
    public void saveRoute() {

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


    public MotionSensor getMs() {
        return ms;
    }

    public void setMotionSensor(MotionSensor ms) {
        this.ms = ms;
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }
}
