package com.groep5.shakeyouup;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.WindowManager;

/**
 * Created by Bram on 6/4/2015.
 */
public class MotionSensor implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor aSensor;
    private Activity activity;

    public MotionSensor(Activity activity) {
        this.activity = activity;

        sensorManager = (SensorManager) this.activity.getSystemService(Context.SENSOR_SERVICE);
        aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        //Register sensor in the manager DUNNO WHAT THE DELAY IS
        sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] vector = new float[3];

        vector[0] = event.values[0];
        vector[1] = event.values[1];
        vector[2] = event.values[2];

        vector = this.recalculateVector(vector);

        DashboardActivity DA = (DashboardActivity)this.activity;

        DA.adjustScore(vector);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private float[] recalculateVector(float[] vector) {

        WindowManager windowMgr =
                (WindowManager)this.activity.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowMgr.getDefaultDisplay().getRotation();

        float[] screenVec = new float[3];
        final int axisSwap[][] = {
                {  1,  -1,  0,  1  },     // ROTATION_0
                {-1,  -1,  1,  0  },     // ROTATION_90
                {-1,    1,  0,  1  },     // ROTATION_180
                {  1,    1,  1,  0  }  }; // ROTATION_270

        final int[] as = axisSwap[rotation];

        screenVec[0]  =  (float)as[0] * vector[ as[2] ];
        screenVec[1]  =  (float)as[1] * vector[ as[3] ];
        screenVec[2]  =  vector[2];

        return screenVec;
    }
}
