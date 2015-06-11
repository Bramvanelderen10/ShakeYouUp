package com.groep5.shakeyouup;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Timer;


public class DashboardActivity extends ActionBarActivity {

    private Journey journey =  null;
    private Timer timer;
    private GPSControl GPS = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        GPS = new GPSControl(this, this);
        // Check availability of play services
        try {
            if (checkPlayServices()) {
                // Building the GoogleApi client
                GPS.buildGoogleApiClient();
                GPS.createLocationRequest();
            }
        }
        catch(Exception e){
            Log.e(GPS.TAG,e.toString());
        }

        View stop = findViewById(R.id.stop);
        stop.setVisibility(View.GONE);

        findViewById(R.id.startLocationText).setVisibility(View.GONE);
        findViewById(R.id.startLocationView).setVisibility(View.GONE);
        findViewById(R.id.endLocationView).setVisibility(View.GONE);
        findViewById(R.id.endLocationText).setVisibility(View.GONE);
        findViewById(R.id.save).setVisibility(View.GONE);
        findViewById(R.id.share).setVisibility(View.GONE);
        findViewById(R.id.scoreLabel).setVisibility(View.GONE);
        findViewById(R.id.ratingText).setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /*
     * Boolean used to check up with the Google Play services, required for GPS functionality.
     * It'd be pretty neat if we could move this over to GPSControl entirely
     * but at the moment, doing that causes activity issues during the initial startup :(
     */
    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        GPS.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    public void start(View v) {
        //Reset journey
        this.journey = null;
        this.journey = new Journey(new DatabaseManager(this));
        this.journey.setMotionSensor(
                new MotionSensor(
                        (SensorManager) getSystemService(Context.SENSOR_SERVICE),
                        (WindowManager) this.getSystemService(Context.WINDOW_SERVICE)
                ));
        this.journey.start();
        this.timer = new Timer();
        timer.schedule(new DashboardTimerTask(this), 0, 500);

        findViewById(R.id.currentScore).setVisibility(View.VISIBLE);
        findViewById(R.id.start).setVisibility(View.GONE);

        findViewById(R.id.stop).setVisibility(View.VISIBLE);
        findViewById(R.id.startLocationText).setVisibility(View.GONE);
        findViewById(R.id.startLocationView).setVisibility(View.GONE);
        findViewById(R.id.endLocationView).setVisibility(View.GONE);
        findViewById(R.id.endLocationText).setVisibility(View.GONE);
        findViewById(R.id.save).setVisibility(View.GONE);
        findViewById(R.id.finalScore).setVisibility(View.GONE);
        findViewById(R.id.ratingText).setVisibility(View.GONE);
        findViewById(R.id.scoreLabel).setVisibility(View.GONE);
        findViewById(R.id.share).setVisibility(View.GONE);

        TextView startLocationText = (TextView)findViewById(R.id.startLocationText);
        startLocationText.setText("");
        TextView endLocationText = (TextView)findViewById(R.id.endLocationText);
        endLocationText.setText("");

    }

    public void stop(View v) {
        if (this.journey == null) return;

        timer.cancel();
        timer = null;

        journey.stop();
        float score = journey.getFinalScore();
        float time = journey.getCurrentTime();
        int rating = journey.getRating();

        TextView finalScoreView = (TextView) findViewById(R.id.finalScore);
        TextView timeView = (TextView) findViewById(R.id.time);
        TextView ratingView = (TextView) findViewById(R.id.ratingText);
        timeView.setText(Float.toString(time));
        finalScoreView.setText(Float.toString(score));
        ratingView.setText(Integer.toString(rating));


        findViewById(R.id.stop).setVisibility(View.GONE);
        findViewById(R.id.currentScore).setVisibility(View.GONE);
        findViewById(R.id.start).setVisibility(View.VISIBLE);
        findViewById(R.id.startLocationText).setVisibility(View.VISIBLE);
        findViewById(R.id.startLocationView).setVisibility(View.VISIBLE);
        findViewById(R.id.endLocationView).setVisibility(View.VISIBLE);
        findViewById(R.id.endLocationText).setVisibility(View.VISIBLE);
        findViewById(R.id.save).setVisibility(View.VISIBLE);
        findViewById(R.id.finalScore).setVisibility(View.VISIBLE);
        findViewById(R.id.scoreLabel).setVisibility(View.VISIBLE);
        findViewById(R.id.ratingText).setVisibility(View.VISIBLE);
    }

    public void updateDashboard() {
        //This method usually runs outside the main threads but to access views we need to get back in the UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float currentScore = journey.updateScore();

                long timeRunning = journey.getCurrentTime();

                TextView scoreView = (TextView) findViewById(R.id.currentScore);
                scoreView.setText(Float.toString(currentScore));

                TextView timeView = (TextView) findViewById(R.id.time);
                timeView.setText(Long.toString(timeRunning));
            }
        });
    }

    public void save(View v) {

        TextView startLocationView = (TextView)findViewById(R.id.startLocationText);
        TextView endLocationView = (TextView)findViewById(R.id.endLocationText);

        String[] location = new String[2];
        location[0] = startLocationView.getText().toString();
        location[1] = endLocationView.getText().toString();

        journey.save(location);

        findViewById(R.id.share).setVisibility(View.VISIBLE);
    }

    public void viewRoutes(View v) {

    }

    //TODO: actually implement this method somewhere
    public void onClickShare (View v)
    {
        TextView startLocationText = (TextView)findViewById(R.id.startLocationText);
        TextView endLocationText = (TextView)findViewById(R.id.endLocationText);
        TextView scoreView = (TextView)findViewById(R.id.ratingText);

        String startLocation = startLocationText.getText().toString();
        String endLocation = endLocationText.getText().toString();
        String score = scoreView.getText().toString();

        Log.i("Share", "User shared his score: " + score );
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "De reis van " + startLocation + " naar " + endLocation + " heeft als cijfer een " + score);
        startActivity(Intent.createChooser(shareIntent, "Share with:"));
    }


    @Override
    protected void onStart() {
        super.onStart();
        GPS.apiConnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GPS.checkServices();
    }

    @Override
    protected void onStop() {
        super.onStop();
        GPS.stopServices();
    }

    @Override
    protected void onPause() {
        super.onPause();
        GPS.stopLocationUpdates();
    }
}
