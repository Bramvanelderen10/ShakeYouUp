package com.groep5.shakeyouup;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.DecimalFormat;

public class GPSControl implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    // LogCat tag
    public static final String TAG = GPSControl.class.getSimpleName();

    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private Context c = null;
    private Activity a = null;

    private Location mLastLocation;

    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters
    public final static int RADIUS = 6371000; //Earth's radius in meters
    public GeoCoordinate startDistance = new GeoCoordinate(0,0);
    public GeoCoordinate endDistance = new GeoCoordinate(0,0);

    public GPSControl(Context c, Activity a){
        try{
            this.c = c;
            this.a = a;
        }
        catch(Exception e)
        {
            Log.e(TAG,e.toString());
        }
    }

    public void apiConnect(){
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    public void checkServices(){
        checkPlayServices();
        // Resuming the periodic location updates
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
            Log.i(TAG, "Resuming updates");
        }
    }

    public void stopServices() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
            Log.i(TAG, "Discontinue updates");
        }
    }

    public GeoCoordinate getEndDistance() {
        return endDistance;
    }

    public void setEndDistance(GeoCoordinate endDistance) {
        this.endDistance = endDistance;
    }

    public GeoCoordinate getStartDistance() {
        return startDistance;
    }

    public void setStartDistance(GeoCoordinate startDistance) {
        this.startDistance = startDistance;
    }

    /**
     * Method to calculate distance between two sets of geographic coordinates
     * returns distance in meters
     */
    public double calcDistance(GeoCoordinate start, GeoCoordinate end) // x = latitude, y = longitude
    {
        double x1 = start.getLatitude();
        double x2 = end.getLatitude();
        double y1 = start.getLongitude();
        double y2 = end.getLongitude();

        double phi1 = Math.toRadians(x1);
        double phi2 = Math.toRadians(x2);
        double deltaPhi = Math.toRadians(x2-x1);
        double deltaLambda = Math.toRadians(y2-y1);

        double a = Math.sin(deltaPhi/2) * Math.sin(deltaPhi/2) +
                Math.cos(phi1) * Math.cos(phi2) *
                        Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        double distance = RADIUS * c;

        return distance;
    }

    public String getDistance(){
        String s = "";
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        try {
            if (mLastLocation != null) {
                double distance = calcDistance(startDistance,endDistance);
                DecimalFormat formatter = new DecimalFormat("#0.00");
                s = formatter.format(distance / 1000) + "KM";

            } else {
                s = c.getString(R.string.lbl_gps_error);
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, e.toString());
            s = "Error while calculating distance";
        }
        finally{
            return s;
        }
    }

    /**
     * Method to toggle periodic location updates
     * */
    public boolean togglePeriodicLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            mRequestingLocationUpdates = true;
            // Starting the location updates
            startLocationUpdates();
            Log.d(TAG, "Periodic location updates started!");
            return true;
        } else {
            mRequestingLocationUpdates = false;
            // Stopping the location updates
            stopLocationUpdates();
            Log.d(TAG, "Periodic location updates stopped!");
            return false;
        }
    }

    /**
     * Creating google api client object
     * */
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(c)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    /**
     * Creating location request object
     * */
    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Method to verify google play services on the device
     * */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(c);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, a,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(c.getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                a.finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Starting the location updates
     * */
    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);

    }

    /**
     * Stopping location updates
     */
    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    /**
     * Google api callback methods
     */
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }

    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayLocation();

        if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;

        Toast.makeText(c, "Location changed!",
                Toast.LENGTH_SHORT).show();

        // Displaying the new location on UI
        displayLocation();
    }

    public String displayLocation(){
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            return latitude + ", " + longitude;

        } else {
            return c.getString(R.string.lbl_gps_error);
        }
    }

}
