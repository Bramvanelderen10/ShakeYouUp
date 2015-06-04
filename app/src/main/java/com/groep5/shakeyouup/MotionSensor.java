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

    public MotionSensor(SensorManager sm, WindowManager wm) {

        this.sensorManager = sm;
        this.windowManager = wm;
        this.aSensor = this.sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        //Register sensor in the manager DUNNO WHAT THE DELAY IS
        this.sensorManager.registerListener(this, this.aSensor, SensorManager.SENSOR_DELAY_NORMAL);

        this.totalVector = new float[3];
        this.totalVector[0] = 0;
        this.totalVector[1] = 0;
        this.totalVector[2] = 0;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float[] vector = new float[3];

        vector[0] = event.values[0];
        vector[1] = event.values[1];
        vector[2] = event.values[2];

        vector = this.recalculateVector(vector);

        this.totalVector[0] += (vector[0] > 1.2)? vector[0]: 0;
        this.totalVector[1] += (vector[1] > 1.2)? vector[1]: 0;
        this.totalVector[2] += (vector[2] > 1.0)? vector[2]: 0;

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

    public float[] getTotalVector() {
        return totalVector;
    }

    public void setTotalVector(float[] totalVector) {
        this.totalVector = totalVector;
    }
}
