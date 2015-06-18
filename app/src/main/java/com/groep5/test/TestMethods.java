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
         The formula used by calcDistance is the "Haversine formula" and goes as follows:

         a = sin(??/2) + cos ?1 ? cos ?2 ? sin(??/2)
         c = 2 ? atan2( ?a, ?(1?a) )
         d = R ? c

         where 	? is latitude, ? is longitude, R is earths radius (mean radius = 6,371km)

         According to the formula, this means that the distance between Amsterdam and Rotterdam should be 56998.72205888608 meters
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

        // Checking if the route exists in the DB
        assertNotNull(dbm.getRoute(0));
    }
}
