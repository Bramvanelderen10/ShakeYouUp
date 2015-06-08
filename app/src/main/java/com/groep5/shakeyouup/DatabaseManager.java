package com.groep5.shakeyouup;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bram on 6/7/2015.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SYU"; //ShakeYouUp

    private static final String TABLE_ROUTES = "route";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
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
                + KEY_NAME +" TEXT,"
                + KEY_DISTANCE +" INTEGER,"
                + KEY_TIME +" INTEGER,"
                + KEY_SCORE +" INTEGER)";
        sqLiteDatabase.execSQL(CREATE_TABLE_ROUTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    }

    public void addRoute(
            Route route
    ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, route.getName());
        values.put(KEY_DISTANCE, route.getDistance());
        values.put(KEY_TIME, route.getTime());
        values.put(KEY_SCORE, route.getScore());

        db.insert(TABLE_ROUTES, null, values);
        db.close();
    }
}
