package com.groep5.shakeyouup;

import android.app.Fragment;
import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrientationFragment extends Fragment {

//    private DashboardActivity dashboardActivity;
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

    // this line and all code beyond it can probably be deleted

//    public OrientationFragment() {
        // Required empty public constructor
//    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        TextView textView = new TextView(getActivity());
//        textView.setText(R.string.hello_blank_fragment);
//        return textView;
//    }
}
