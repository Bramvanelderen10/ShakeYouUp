package com.groep5.shakeyouup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bram on 6/7/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SYU"; //ShakeYouUp

    private static final String TABLE_ROUTES = "route";
    private static final String KEY_ID = "id";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_TIME = "time";
    private static final String KEY_SCORE = "score";
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";

    private static final String TABLE_LOCATIONS = "location";
    private static final String KEY_L_ID = "id";
    private static final String KEY_L_NAME = "name";
    private static final String KEY_L_COORDINATE_X = "coordinate_x";
    private static final String KEY_L_COORDINATE_Y = "coordinate_y";

    private static final String TABLE_ROUTE_COORDINATES = "route_coordinate";
    private static final String KEY_R_ID = "id";
    private static final String KEY_R_COORDINATE_X = "coordinate_x";
    private static final String KEY_R_COORDINATE_Y = "coordinate_Y";
    private static final String KEY_R_ROUTE_ID = "route_id";

    private static final String TABLE_ROUTE_MOVEMENT = "route_movement";
    private static final String KEY_M_ID = "id";
    private static final String KEY_M_MOVEMENT = "movement";
    private static final String KEY_M_TIME = "time";
    private static final String KEY_M_ROUTE_ID = "route_id";


    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ROUTES =  "CREATE TABLE " + TABLE_ROUTES +
                "("
                + KEY_ID +" INTEGER PRIMARY KEY,"
                + KEY_DISTANCE +" INTEGER,"
                + KEY_TIME +" INTEGER,"
                + KEY_SCORE +" INTEGER,"
                + KEY_START +" INTEGER,"
                + KEY_END + " INTEGER,"
                + " FOREIGN KEY(" + KEY_START + ") REFERENCES " + TABLE_LOCATIONS + "(id),"
                + " FOREIGN KEY(" + KEY_END + ") REFERENCES " + TABLE_LOCATIONS + "(id)"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTES);

        String CREATE_TABLE_LOCATIONS =  "CREATE TABLE " + TABLE_LOCATIONS +
                "("
                + KEY_L_ID +" INTEGER PRIMARY KEY,"
                + KEY_L_NAME +" TEXT,"
                + KEY_L_COORDINATE_X +" INTEGER,"
                + KEY_L_COORDINATE_Y +" INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE_LOCATIONS);

        String CREATE_TABLE_ROUTE_COORDINATES =  "CREATE TABLE " + TABLE_ROUTE_COORDINATES +
                "("
                + KEY_R_ID +" INTEGER PRIMARY KEY,"
                + KEY_R_COORDINATE_X +" INTEGER,"
                + KEY_R_COORDINATE_Y +" INTEGER,"
                + KEY_R_ROUTE_ID +" INTEGER NOT NULL,"
                + " FOREIGN KEY(" + KEY_R_ROUTE_ID + ") REFERENCES " + TABLE_ROUTES + "(id)"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTE_COORDINATES);

        String CREATE_TABLE_ROUTE_MOVEMENT =  "CREATE TABLE " + TABLE_ROUTE_MOVEMENT +
                "("
                + KEY_M_ID +" INTEGER PRIMARY KEY,"
                + KEY_M_MOVEMENT +" INTEGER,"
                + KEY_M_TIME +" INTEGER,"
                + KEY_M_ROUTE_ID +" INTEGER NOT NULL,"
                + " FOREIGN KEY(" + KEY_M_ROUTE_ID + ") REFERENCES " + TABLE_ROUTES + "(id)"
                + ")";
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTE_MOVEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_COORDINATES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTE_MOVEMENT);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addRoute(
            Route route
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, route.getId());
        values.put(KEY_DISTANCE, route.getDistance());
        values.put(KEY_TIME, route.getTime());
        values.put(KEY_SCORE, route.getScore());
        values.put(KEY_START, route.getStartLocation().getId());
        values.put(KEY_END, route.getEndLocation().getId());

        db.insert(TABLE_ROUTES, null, values);
        db.close();
    }

    public Route getRoute(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ROUTES, new String[] {
                        KEY_ID,
                        KEY_DISTANCE,
                        KEY_TIME,
                        KEY_SCORE,
                        KEY_START,
                        KEY_END
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Route route = new Route();
        route.setId(Integer.parseInt(cursor.getString(0)));
        route.setDistance(Double.parseDouble(cursor.getString(1)));
        route.setTime(Integer.parseInt(cursor.getString(2)));
        route.setScore(Integer.parseInt(cursor.getString(3)));

        Location start = this.getLocation(Integer.parseInt(cursor.getString(4)));
        route.setStartLocation(start);

        Location end = this.getLocation(Integer.parseInt(cursor.getString(5)));
        route.setEndLocation(end);
        // return contact
        return route;
    }

    public List<Route> getAllRoutes() {
        List<Route> routeList = new ArrayList<>();

        String select = "SELECT * FROM " + TABLE_ROUTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Route route = new Route();
                route.setId(Integer.parseInt(cursor.getString(0)));
                route.setDistance(Double.parseDouble(cursor.getString(1)));
                route.setTime(Integer.parseInt(cursor.getString(2)));
                route.setScore(Integer.parseInt(cursor.getString(3)));

                Location start = this.getLocation(Integer.parseInt(cursor.getString(4)));
                route.setStartLocation(start);

                Location end = this.getLocation(Integer.parseInt(cursor.getString(5)));
                route.setEndLocation(end);

                routeList.add(route);
            } while(cursor.moveToNext());
        }

        return routeList;
    }

    public void addLocation(
            Location location
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_L_NAME, location.getName());
        values.put(KEY_L_COORDINATE_X, location.getCoordinates().getLatitude());
        values.put(KEY_L_COORDINATE_Y, location.getCoordinates().getLongitude());

        db.insert(TABLE_LOCATIONS, null, values);
        db.close();
    }

    public Location getLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_LOCATIONS, new String[]{
                        KEY_L_ID,
                        KEY_L_NAME,
                        KEY_L_COORDINATE_X,
                        KEY_L_COORDINATE_Y,
                }, KEY_L_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        Location location = null;

        if (cursor != null && cursor.moveToFirst()) {
            location = new Location();
            location.setId(Integer.parseInt(cursor.getString(0)));
            location.setName(cursor.getString(1));
            double lat = Double.parseDouble(cursor.getString(2));
            double lon = Double.parseDouble(cursor.getString(3));
            GeoCoordinate coordinates = new GeoCoordinate(lat,lon);
            location.setCoordinates(coordinates);
        }

        return location;
    }

    public Location getLocationByName(String name) {
        Location location = null;

        String select = "SELECT * FROM " + TABLE_LOCATIONS + " l WHERE l.name = '" + name + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);


        if (cursor != null && cursor.moveToFirst()) {
            do {
                location = new Location();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setName(cursor.getString(1));
                double lat = Double.parseDouble(cursor.getString(2));
                double lon = Double.parseDouble(cursor.getString(3));
                GeoCoordinate coordinates = new GeoCoordinate(lat,lon);
                location.setCoordinates(coordinates);
            } while(cursor.moveToNext());
        }
        return location;
    }

    public List<Location> getAllLocations() {
        List<Location> locationList = new ArrayList<>();

        String select = "SELECT * FROM " + TABLE_LOCATIONS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Location location = new Location();
                location.setId(Integer.parseInt(cursor.getString(0)));
                location.setName(cursor.getString(1));
                double lat = Double.parseDouble(cursor.getString(2));
                double lon = Double.parseDouble(cursor.getString(3));
                GeoCoordinate coordinates = new GeoCoordinate(lat,lon);
                location.setCoordinates(coordinates);

                locationList.add(location);
            } while(cursor.moveToNext());
        }

        return locationList;
    }

    public void addRouteCoordinate(
            RouteCoordinate routeCoordinate
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_R_COORDINATE_X, routeCoordinate.getCoordinate().getLatitude());
        values.put(KEY_R_COORDINATE_Y, routeCoordinate.getCoordinate().getLongitude());
        values.put(KEY_R_ROUTE_ID, routeCoordinate.getRoute().getId());

        db.insert(TABLE_ROUTE_COORDINATES, null, values);
        db.close();
    }

    public RouteCoordinate getRouteCoordinate(int id) {
        RouteCoordinate routeCoordinate = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ROUTE_COORDINATES, new String[] {
                        KEY_R_ID,
                        KEY_R_COORDINATE_X,
                        KEY_R_COORDINATE_Y,
                        KEY_R_ROUTE_ID
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            routeCoordinate = new RouteCoordinate();
            routeCoordinate.setId(Integer.parseInt(cursor.getString(0)));

            GeoCoordinate geoCoordinate = new GeoCoordinate(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
            routeCoordinate.setCoordinate(geoCoordinate);

            routeCoordinate.setRoute(getRoute(Integer.parseInt(cursor.getString(3))));
        }

        return routeCoordinate;
    }

    public List<RouteCoordinate> getAllRouteCoordinatesByRoute(Route route) {
        List<RouteCoordinate> routeCoordinates = new ArrayList<>();

        String select = "SELECT * FROM " + TABLE_ROUTE_COORDINATES + " WHERE " + KEY_R_ROUTE_ID + "=" + route.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                RouteCoordinate routeCoordinate = new RouteCoordinate();
                routeCoordinate.setId(Integer.parseInt(cursor.getString(0)));

                GeoCoordinate geoCoordinate = new GeoCoordinate(Double.parseDouble(cursor.getString(1)), Double.parseDouble(cursor.getString(2)));
                routeCoordinate.setCoordinate(geoCoordinate);

                routeCoordinate.setRoute(getRoute(Integer.parseInt(cursor.getString(3))));
                routeCoordinates.add(routeCoordinate);
            } while(cursor.moveToNext());
        }

        return routeCoordinates;
    }

    public void addRouteMovement(
            RouteMovement routeMovement
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_M_MOVEMENT, routeMovement.getMovement());
        values.put(KEY_M_TIME, routeMovement.getTime());
        values.put(KEY_M_ROUTE_ID, routeMovement.getRoute().getId());

        db.insert(TABLE_ROUTE_MOVEMENT, null, values);
        db.close();
    }

    public RouteMovement getRouteMovement(int id) {
        RouteMovement routeMovement = null;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ROUTE_MOVEMENT, new String[] {
                        KEY_M_ID,
                        KEY_M_MOVEMENT,
                        KEY_M_TIME,
                        KEY_M_ROUTE_ID
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            routeMovement = new RouteMovement();
            routeMovement.setId(Integer.parseInt(cursor.getString(0)));
            routeMovement.setMovement(Double.parseDouble(cursor.getString(1)));
            routeMovement.setTime(Long.parseLong(cursor.getString(2)));
            routeMovement.setRoute(getRoute(Integer.parseInt(cursor.getString(3))));
        }

        return routeMovement;
    }

    public List<RouteMovement> getAllRouteMovementByRoute(Route route) {
        List<RouteMovement> routeMovements = new ArrayList<>();

        String select = "SELECT * FROM " + TABLE_ROUTE_MOVEMENT + " WHERE " + KEY_M_ROUTE_ID + "=" + route.getId();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                RouteMovement routeMovement = new RouteMovement();

                routeMovement = new RouteMovement();
                routeMovement.setId(Integer.parseInt(cursor.getString(0)));
                routeMovement.setMovement(Double.parseDouble(cursor.getString(1)));
                routeMovement.setTime(Long.parseLong(cursor.getString(2)));
                routeMovement.setRoute(getRoute(Integer.parseInt(cursor.getString(3))));

                routeMovements.add(routeMovement);
            } while(cursor.moveToNext());
        }

        return routeMovements;
    }
}
