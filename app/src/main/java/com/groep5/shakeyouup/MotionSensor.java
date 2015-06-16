package com.groep5.shakeyouup;

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
    private WindowManager windowManager;

    private float[] totalVector;
    private float[] eventVector;

    public MotionSensor(SensorManager sm, WindowManager wm) {

        sensorManager = sm;
        windowManager = wm;
        aSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        //Register sensor in the manager DUNNO WHAT THE DELAY IS
        sensorManager.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_NORMAL);

        totalVector = new float[3];
        totalVector[0] = 0;
        totalVector[1] = 0;
        totalVector[2] = 0;

        eventVector = new float[3];
        eventVector[0] = 0;
        eventVector[1] = 0;
        eventVector[2] = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] vector = new float[3];

        vector[0] = event.values[0];
        vector[1] = event.values[1];
        vector[2] = event.values[2];

        vector = this.recalculateVector(vector);

        eventVector[0] = (vector[0] > 1.2)? vector[0]: 0;
        eventVector[1] = (vector[1] > 1.2)? vector[1]: 0;
        eventVector[2] = (vector[2] > 1.0)? vector[2]: 0;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private float[] recalculateVector(float[] vector) {

        int rotation = this.windowManager.getDefaultDisplay().getRotation();

        float[] screenVec = new float[3];
        final int axisSwap[][] = {
                {  1,  -1,  0,  1  },     // ROTATION_0
                {-1,  -1,  1,  0  },     // ROTATION_90
                {-1,    1,  0,  1  },     // ROTATION_180
                {  1,    1,  1,  0  }  }; // ROTATION_270

        final int[] as = axisSwap[rotation];

        screenVec[0]  =  Math.abs((float) as[0] * vector[as[2]]);
        screenVec[1]  =  Math.abs((float)as[1] * vector[ as[3] ]);
        screenVec[2]  =  Math.abs(vector[2]);

        return screenVec;
    }

    //Update score based on sensor values
    public float[] updateScore() {

        //Bigger movement adds more points
        for (int i = 0; i < 3; i++) {
            if (eventVector[i] > 1.2 && eventVector[i] < 5) totalVector[i] += 1;
            if (eventVector[i] >= 5 && eventVector[i] < 20) totalVector[i] += 2;
            if (eventVector[i] >= 20) totalVector[i] += 4;
        }

        return totalVector;
    }

    public float[] getTotalVector() {
        return totalVector;
    }
}
