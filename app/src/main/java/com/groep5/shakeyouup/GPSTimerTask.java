package com.groep5.shakeyouup;

import java.util.TimerTask;

/**
 * Created by Bram on 6/16/2015.
 */
public class GPSTimerTask extends TimerTask {
    private DashboardActivity da;

    public GPSTimerTask(DashboardActivity da) {
        this.da = da;
    }

    @Override
    public void run() {
        da.addCoordinate();
    }
}
