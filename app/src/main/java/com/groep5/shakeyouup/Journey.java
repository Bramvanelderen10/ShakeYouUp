package com.groep5.shakeyouup;

import java.util.ArrayList;
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
    private double distance;

    private List<GeoCoordinate> routeCoordinates;


    public Journey(DatabaseManager dm) {
        this.dm = dm;
        routeCoordinates = new ArrayList<>();
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
        Location startLocation = dm.getLocationByName(location[0]);
        if (startLocation == null) {
            startLocation = new Location();
            startLocation.setName(location[0]);

            double[] startCoordinates = new double[2];
            startCoordinates[0] = routeCoordinates.get(0).getLatitude();
            startCoordinates[1] = routeCoordinates.get(0).getLongitude();

            startLocation.setCoordinates(startCoordinates);

            dm.addLocation(startLocation);
        }

        Location endLocation = dm.getLocationByName(location[1]);
        if (endLocation == null) {
            endLocation = new Location();
            endLocation.setName(location[1]);

            double[] endCoordinates = new double[2];
            endCoordinates[0] = routeCoordinates.get(routeCoordinates.size() - 1).getLatitude();
            endCoordinates[1] = routeCoordinates.get(routeCoordinates.size() - 1).getLongitude();

            endLocation.setCoordinates(endCoordinates);

            dm.addLocation(endLocation);
        }

        startLocation = dm.getLocationByName(startLocation.getName());
        endLocation = dm.getLocationByName(endLocation.getName());

        List<Route> routes = dm.getAllRoutes();
        routes.size();

        Route route = new Route();

        route.setId(routes.size() + 1);
        route.setTime(getCurrentTime());

        route.setScore(rating);

        route.setDistance((int) Math.round(this.distance));

        route.setStartLocation(startLocation);
        route.setEndLocation(endLocation);

        dm.addRoute(route);

        for (GeoCoordinate coordinate : routeCoordinates) {
            RouteCoordinate routeCoordinate = new RouteCoordinate();
            routeCoordinate.setRoute(route);
            routeCoordinate.setCoordinate(coordinate);

            dm.addRouteCoordinate(routeCoordinate);
        }

        List<RouteCoordinate> test = dm.getAllRouteCoordinatesByRoute(route);
        test.size();

    }

    //TODO calculate score based on motion time and distance
    private float calculateFinalScore() {
        float[] vector = this.ms.getTotalVector();

        float movementScore = vector[0] + vector[1] + vector[2];
        finalScore = (int) movementScore;
        long timer = getCurrentTime(); //TODO Maybe do something with this..
        long distance = Math.round(this.distance) * 1000;

        //First we divide the movement score by the distance so we get the average score for each meter
        float movementPerMeter = movementScore / distance;

        rating = 1;
        //TODO fine tune numbers
        if (movementPerMeter < 10) rating = 10;
        if (movementPerMeter < 20) rating = 9;
        if (movementPerMeter < 30) rating = 8;
        if (movementPerMeter < 40) rating = 7;
        if (movementPerMeter < 50) rating = 6;
        if (movementPerMeter < 60) rating = 5;
        if (movementPerMeter < 70) rating = 4;
        if (movementPerMeter < 80) rating = 3;
        if (movementPerMeter < 90) rating = 2;

        return getRating();
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
