package com.groep5.shakeyouup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bram on 6/4/2015.
 */
public class Journey {
    public void setFinalScore(int finalScore) {
        this.finalScore = finalScore;
    }

    private MotionSensor ms;
    private DatabaseManager dm;
    private long startTime;
    private int rating;
    private int finalScore;
    private double distance;
    private List<RouteMovement> routeMovements;

    private List<GeoCoordinate> routeCoordinates;


    public Journey(DatabaseManager dm) {
        this.dm = dm;
        routeCoordinates = new ArrayList<>();
        routeMovements = new ArrayList<>();
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
        Location startLocation =  new Location();

        startLocation.setName(location[0]);

        GeoCoordinate startCoordinates = routeCoordinates.get(0);

        startLocation.setCoordinates(startCoordinates);

        dm.addLocation(startLocation);


        Location endLocation = new Location();
        endLocation.setName(location[1]);

        GeoCoordinate endCoordinates = routeCoordinates.get(routeCoordinates.size() - 1);

        endLocation.setCoordinates(endCoordinates);

        dm.addLocation(endLocation);

        startLocation = dm.getLocationByName(startLocation.getName());
        endLocation = dm.getLocationByName(endLocation.getName());

        List<Route> routes = dm.getAllRoutes();
        routes.size();

        Route route = new Route();
        route.setId(routes.size() + 1);
        route.setTime(getCurrentTime());
        route.setScore(rating);
        route.setDistance(Math.round(this.distance));
        route.setStartLocation(startLocation);
        route.setEndLocation(endLocation);

        dm.addRoute(route);

        for (GeoCoordinate coordinate : routeCoordinates) {
            RouteCoordinate routeCoordinate = new RouteCoordinate();
            routeCoordinate.setRoute(route);
            routeCoordinate.setCoordinate(coordinate);

            dm.addRouteCoordinate(routeCoordinate);
        }

        for (RouteMovement routeMovement : routeMovements) {
            routeMovement.setRoute(route);
            dm.addRouteMovement(routeMovement);
        }
    }

    private float calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();

        float movementScore = vector[0] + vector[1] + vector[2];
        finalScore = (int) movementScore;
        long distance = Math.round(this.distance) * 1000;

        //First we divide the movement score by the distance so we get the average score for each meter
        float movementPerMeter = movementScore / distance;

        rating = 1;
        //TODO fine tune numbers
        if (movementPerMeter < 10) rating = 10;
        if (movementPerMeter < 20 && movementPerMeter >= 10) rating = 9;
        if (movementPerMeter < 30 && movementPerMeter >= 20) rating = 8;
        if (movementPerMeter < 40 && movementPerMeter >= 30) rating = 7;
        if (movementPerMeter < 50 && movementPerMeter >= 40) rating = 6;
        if (movementPerMeter < 60 && movementPerMeter >= 50) rating = 5;
        if (movementPerMeter < 70 && movementPerMeter >= 60) rating = 4;
        if (movementPerMeter < 80 && movementPerMeter >= 70) rating = 3;
        if (movementPerMeter < 90 && movementPerMeter >= 80) rating = 2;

        return rating;
    }

    public int getRating() {

        return rating;
    }

    public float updateScore() {
        float[] vector = this.ms.updateScore();

        return vector[0] + vector[1] + vector[2];
    }

    public void updateMovement() {
        float[] vector = this.ms.updateScore();
        double movement = vector[0] + vector[1] + vector[2];

        RouteMovement routeMovement = new RouteMovement();
        routeMovement.setMovement(movement);
        routeMovement.setTime(getCurrentTime());
        routeMovements.add(routeMovement);
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void addCoordinate(GeoCoordinate coordinate) {
        routeCoordinates.add(coordinate);
    }

    public GeoCoordinate[] getStartEndCoordinates() {
        GeoCoordinate[] geoCoordinates = new GeoCoordinate[2];
        geoCoordinates[0] = routeCoordinates.get(0);
        geoCoordinates[1] = routeCoordinates.get(routeCoordinates.size() - 1);

        return geoCoordinates;
    }
}
