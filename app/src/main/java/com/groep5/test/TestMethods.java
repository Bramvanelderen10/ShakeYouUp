package com.groep5.test;

import android.app.Activity;
import android.content.Context;
import android.test.AndroidTestCase;
import com.groep5.shakeyouup.DatabaseManager;
import com.groep5.shakeyouup.GPSControl;
import com.groep5.shakeyouup.GeoCoordinate;
import com.groep5.shakeyouup.Location;
import com.groep5.shakeyouup.Route;

/**
 * Created by Martijn on 16-6-2015.
 */
public class TestMethods extends AndroidTestCase {
    public GeoCoordinate amsterdam = new GeoCoordinate(52.366667, 4.9);
    public GeoCoordinate rotterdam = new GeoCoordinate(51.916667, 4.5);

    // Calculate distance between two coordinates
    public void testCalcDistance() throws Exception {
        // Setup
        Context c = getContext();
        Activity a = null;
        GPSControl GPS = new GPSControl(c, a);
        GeoCoordinate amsterdamCoordinate = new GeoCoordinate(52.366667, 4.9);
        GeoCoordinate rotterdamCoordinate = new GeoCoordinate(51.916667, 4.5);

        // Calculate distance from Amsterdam to Rotterdam
        double amToRo = GPS.calcDistance(amsterdamCoordinate,rotterdamCoordinate);
        // Calculate distance from Rotterdam to Amsterdam
        double roToAm = GPS.calcDistance(rotterdamCoordinate,amsterdamCoordinate);

        /*
         The formula used by calcDistance is the "Haversine formula".
         According to the formula, the distance between Amsterdam and Rotterdam should be 56998.72205888608 meters
          */
        double expectedDistance = 56998.72205888608;
        assertEquals(amToRo,expectedDistance);

        // Logically, the distance from Amsterdam to Rotterdam should be equal to the distance from Rotterdam to Amsterdam
        assertEquals(amToRo,roToAm);
    }

    // Add a route to the database
    public void testDatabaseRoute() throws Exception{
        // Setup
        Context c = getContext();
        Activity a = null;
        GPSControl gps = new GPSControl(c, a);
        DatabaseManager dbm = new DatabaseManager(c);
        Location l1 = new Location();
        Location l2 = new Location();
        l1.setName("Foo");
        l1.setCoordinates(amsterdam);
        l1.setId(0);
        l2.setName("bar");
        l2.setCoordinates(rotterdam);
        l2.setId(1);
        Route route = new Route();
        route.setStartLocation(l1);
        route.setEndLocation(l2);
        route.setId(0);
        route.setDistance(gps.calcDistance(l1.getCoordinates(), l2.getCoordinates()));
        route.setScore(1234);

        // Adding route to DB
        assertNotNull(dbm); // Check if DB is alive and well
        dbm.addRoute(route); // Adding

        // Checking if the route exists in the DB and has been added succesfully
        assertNotNull(dbm.getRoute(0));
    }

    // Retrieve a route from the database
    public void testDatabaseGetRoute() throws Exception{
        // Setup
        Context c = getContext();
        Activity a = null;
        GPSControl gps = new GPSControl(c, a);
        DatabaseManager dbm = new DatabaseManager(c);
        Location l1 = new Location();
        Location l2 = new Location();
        l1.setName("Foo");
        l1.setCoordinates(amsterdam);
        l1.setId(0);
        l2.setName("bar");
        l2.setCoordinates(rotterdam);
        l2.setId(1);
        Route route = new Route();
        route.setStartLocation(l1);
        route.setEndLocation(l2);
        route.setId(0);
        route.setDistance(gps.calcDistance(l1.getCoordinates(), l2.getCoordinates()));
        route.setScore(1234);

        // Adding route to DB
        assertNotNull(dbm); // Check if DB is alive and well
        dbm.addRoute(route); // Adding

        // Checking if the route exists in the DB
        Route newRoute = dbm.getRoute(0);
        assertNotNull(newRoute);
    }
}
