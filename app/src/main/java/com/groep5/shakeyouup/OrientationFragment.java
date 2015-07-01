package com.groep5.shakeyouup;

import android.app.Fragment;
import android.os.Bundle;

public class OrientationFragment extends Fragment {

    private Journey journey;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Journey getJourney() {
        return this.journey;
    }
}
