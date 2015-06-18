package com.groep5.shakeyouup;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.List;


public class CompareActivity extends ActionBarActivity {

    DatabaseManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_compare);

        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);
        dm = new DatabaseManager(this);

        List<Route> routes = dm.getAllRoutes();

        List<String> items = new ArrayList<>();
        for (Route route : routes) {
            String id = String.valueOf(route.getId());
            String startLocation = route.getStartLocation().getName();
            String endLocation = route.getEndLocation().getName();

            items.add(id + " " + startLocation + " - " + endLocation);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);

        spinner.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_compare, menu);
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

    public void compare(View v) {
        Spinner spinner = (Spinner)findViewById(R.id.spinner);
        Spinner spinner2 = (Spinner)findViewById(R.id.spinner2);

        int route_id1 = spinner.getSelectedItemPosition() + 1;
        Route route1 = dm.getRoute(route_id1);


        TextView start1 = (TextView)findViewById(R.id.startText1);
        TextView end1 = (TextView)findViewById(R.id.endtext1);
        TextView distance1 = (TextView)findViewById(R.id.distanceText1);
        TextView time1 = (TextView)findViewById(R.id.timeText1);
        TextView score1 = (TextView)findViewById(R.id.scoreText1);

        start1.setText(route1.getStartLocation().getName());
        end1.setText(route1.getEndLocation().getName());
        distance1.setText(Double.toString(route1.getDistance()));
        time1.setText(Long.toString(route1.getTime()));
        score1.setText(Integer.toString(route1.getScore()));

        int route_id2 = spinner2.getSelectedItemPosition() + 1;
        Route route2 = dm.getRoute(route_id2);

        TextView start2 = (TextView)findViewById(R.id.startText2);
        TextView end2 = (TextView)findViewById(R.id.endText2);
        TextView distance2 = (TextView)findViewById(R.id.distanceText2);
        TextView time2 = (TextView)findViewById(R.id.timeText2);
        TextView score2 = (TextView)findViewById(R.id.scoreText2);

        start2.setText(route2.getStartLocation().getName());
        end2.setText(route2.getEndLocation().getName());
        distance2.setText(Double.toString(route2.getDistance()));
        time2.setText(Long.toString(route2.getTime()));
        score2.setText(Integer.toString(route2.getScore()));


        GraphView graphView = (GraphView)findViewById(R.id.graph);

        List<RouteMovement> routeMovements = dm.getAllRouteMovementByRoute(route1);
        DataPoint[] dataPoints = new DataPoint[routeMovements.size()];

        int i = 0;
        for (RouteMovement routeMovement : routeMovements) {

            dataPoints[i] = new DataPoint(routeMovement.getTime(), routeMovement.getMovement());
            i++;
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dataPoints);
        series.setColor(Color.BLUE);

        List<RouteMovement> routeMovements2 = dm.getAllRouteMovementByRoute(route2);
        DataPoint[] dataPoints2 = new DataPoint[routeMovements2.size()];

        int i2 = 0;
        for (RouteMovement routeMovement : routeMovements2) {

            dataPoints2[i2] = new DataPoint(routeMovement.getTime(), routeMovement.getMovement());
            i2++;
        }

        LineGraphSeries<DataPoint> series2 = new LineGraphSeries<DataPoint>(dataPoints2);
        series2.setColor(Color.CYAN);
        graphView.addSeries(series);
        graphView.addSeries(series2);


    }
}
