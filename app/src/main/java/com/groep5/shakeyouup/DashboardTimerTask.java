package com.groep5.shakeyouup;

import java.util.TimerTask;

/**
 * Created by Bram on 6/4/2015.
 */
public class DashboardTimerTask extends TimerTask {
    private DashboardActivity da;

    public DashboardTimerTask(DashboardActivity da) {
        this.da = da;
    }

    @Override
    public void run() {
        this.da.updateDashboard();
    }
}
