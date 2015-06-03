package com.groep5.shakeyouup;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DashboardActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager SensorManager;
    private Sensor sensor;
    private float x = 0;
    private float y = 0;
    private float z = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        //Register sensor in the manager DUNNO WHAT THE DELAY IS
        SensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
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

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.values[0] > 10) this.x = event.values[0];
        if (event.values[1] > 10) this.y = event.values[1];
        if (event.values[2] > 10) this.z = event.values[2];

        TextView xField = (TextView)findViewById(R.id.xField);
        xField.setText(Float.toString(this.x));

        TextView yField = (TextView)findViewById(R.id.yField);
        yField.setText(Float.toString(this.y));

        TextView zField = (TextView)findViewById(R.id.zField);
        zField.setText(Float.toString(this.z));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    protected void onResume() {
        super.onResume();
        SensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        SensorManager.unregisterListener(this);
    }
}
