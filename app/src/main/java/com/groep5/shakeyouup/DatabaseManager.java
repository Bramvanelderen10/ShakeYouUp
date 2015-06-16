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
    //TODO Implement foreign keys to location
    private static final String KEY_START = "start";
    private static final String KEY_END = "end";

    private static final String TABLE_LOCATIONS = "location";
    private static final String KEY_L_ID = "id";
    private static final String KEY_L_NAME = "name";
    private static final String KEY_L_COORDINATE_X = "coordinate_x";
    private static final String KEY_L_COORDINATE_Y = "coordinate_y";

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
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATIONS);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addRoute(
            Route route
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
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
        route.setDistance(Integer.parseInt(cursor.getString(1)));
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
                route.setDistance(Integer.parseInt(cursor.getString(1)));
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

    //TODO Make locations
    public void addLocation(
            Location location
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_L_NAME, location.getName());
        values.put(KEY_L_COORDINATE_X, location.getCoordinates()[0]);
        values.put(KEY_L_COORDINATE_Y, location.getCoordinates()[1]);

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
            double[] coordinates = new double[2];
            coordinates[0] = Double.parseDouble(cursor.getString(2));
            coordinates[1] = Double.parseDouble(cursor.getString(3));
            location.setCoordinates(coordinates);
        }

        // return contact
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
                double[] coordinates = new double[2];
                coordinates[0] = Double.parseDouble(cursor.getString(2));
                coordinates[1] = Double.parseDouble(cursor.getString(3));
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
                double[] coordinates = new double[2];
                coordinates[0] = Double.parseDouble(cursor.getString(2));
                coordinates[1] = Double.parseDouble(cursor.getString(3));
                location.setCoordinates(coordinates);

                locationList.add(location);
            } while(cursor.moveToNext());
        }

        return locationList;
    }
}
