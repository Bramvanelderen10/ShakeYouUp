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

import java.util.List;
import java.util.Timer;


public class DashboardActivity extends ActionBarActivity {

    private Journey journey =  null;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        View stop = findViewById(R.id.stop);
        stop.setVisibility(View.GONE);
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
        timer.schedule(new DashboardTimerTask(this), 0, 1000);

        View stop = findViewById(R.id.stop);
        stop.setVisibility(View.VISIBLE);

        View start = findViewById(R.id.start);
        start.setVisibility(View.GONE);
    }

    public void stop(View v) {

        if (this.journey == null) return;

        timer.cancel();
        timer = null;

        journey.stop();
        float score = journey.getFinalScore();
        float time = journey.getCurrentTime();



        TextView finalScoreView = (TextView)findViewById(R.id.finalScore);
        TextView timeView = (TextView)findViewById(R.id.time);
        timeView.setText(Float.toString(time));
        finalScoreView.setText(Float.toString(score));


        View stop = findViewById(R.id.stop);
        stop.setVisibility(View.GONE);

        View start = findViewById(R.id.start);
        start.setVisibility(View.VISIBLE);
    }

    public void updateDashboard() {
        //This method usually runs outside the main threads but to access views we need to get back in the UI thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                float currentScore = journey.getCurrentScore();

                float timeRunning = journey.getCurrentTime();

                TextView scoreView = (TextView) findViewById(R.id.currentScore);
                scoreView.setText(Float.toString(currentScore));

                TextView timeView = (TextView) findViewById(R.id.time);
                timeView.setText(Float.toString(timeRunning));
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



    }

    public void viewRoutes(View v) {

    }

    //TODO: actually implement this method somewhere
    public void onClickShare (View v)
    {
        int score = 0;
        Log.i("Share", "User shared his score: " + score );
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "fubar");
        startActivity(Intent.createChooser(shareIntent, "Share with:"));
    }
}
