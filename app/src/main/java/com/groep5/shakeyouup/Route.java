package com.groep5.shakeyouup;

/**
 * Created by Bram on 6/4/2015.
 */
public class Route {
    private int score;

    public MotionSensor getMs() {
        return ms;
    }

    public void setMs(MotionSensor ms) {
        this.ms = ms;
    }

    private MotionSensor ms;

    public Route() {

}

    //TODO calculate score based on motion time and distance
    public void calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();



        this.score = 10;
    }



    //TODO Save route in database
    public void saveRoute() {

    }
}
