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
    private static final String KEY_START_LOCATION = "startLocation";
    private static final String KEY_END_LOCATION = "endLocation";
    private static final String KEY_DISTANCE = "distance";
    private static final String KEY_TIME = "time";
    private static final String KEY_SCORE = "score";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_ROUTES =  "CREATE TABLE " + TABLE_ROUTES +
                "("
                + KEY_ID +" INTEGER PRIMARY KEY,"
                + KEY_START_LOCATION +" TEXT,"
                + KEY_END_LOCATION +" TEXT,"
                + KEY_DISTANCE +" INTEGER,"
                + KEY_TIME +" INTEGER,"
                + KEY_SCORE +" INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // Drop older table if existed
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ROUTES);

        // Create tables again
        onCreate(sqLiteDatabase);
    }

    public void addRoute(
            Route route
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_START_LOCATION, route.getStartLocation());
        values.put(KEY_END_LOCATION, route.getEndLocation());
        values.put(KEY_DISTANCE, route.getDistance());
        values.put(KEY_TIME, route.getTime());
        values.put(KEY_SCORE, route.getScore());

        db.insert(TABLE_ROUTES, null, values);
        db.close();
    }

    public Route getRoute(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_ROUTES, new String[] {
                        KEY_ID,
                        KEY_START_LOCATION,
                        KEY_END_LOCATION,
                        KEY_DISTANCE,
                        KEY_TIME,
                        KEY_SCORE,
                }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Route route = new Route();
        route.setId(Integer.parseInt(cursor.getString(0)));
        route.setStartLocation(cursor.getString(1));
        route.setEndLocation(cursor.getString(2));
        route.setDistance(Integer.parseInt(cursor.getString(3)));
        route.setTime(Integer.parseInt(cursor.getString(4)));
        route.setScore(Integer.parseInt(cursor.getString(5)));
        // return contact
        return route;
    }

    public List<Route> getAllRoutes() {
        List<Route> routeList = new ArrayList<>();

        String select = "SELECT * FROM " + TABLE_ROUTES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                Route route = new Route();
                route.setId(Integer.parseInt(cursor.getString(0)));
                route.setStartLocation(cursor.getString(1));
                route.setEndLocation(cursor.getString(2));
                route.setDistance(Integer.parseInt(cursor.getString(3)));
                route.setTime(Integer.parseInt(cursor.getString(4)));
                route.setScore(Integer.parseInt(cursor.getString(5)));

                routeList.add(route);
            } while(cursor.moveToNext());
        }

        return routeList;
    }
}
