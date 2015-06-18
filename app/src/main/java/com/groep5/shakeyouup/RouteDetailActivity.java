package com.groep5.shakeyouup;

import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class RouteDetailActivity extends ActionBarActivity implements OnMapReadyCallback {

    private DatabaseManager dm;
    private List<RouteCoordinate> routeCoordinateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_route_detail);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int route_id = extras.getInt("route_id");
            dm = new DatabaseManager(this);

            Route route = dm.getRoute(route_id);
            Location start = route.getStartLocation();
            Location end = route.getEndLocation();

            TextView startView = (TextView)findViewById(R.id.startText);
            startView.setText(start.getName());

            TextView endView = (TextView)findViewById(R.id.endText);
            endView.setText(end.getName());

            TextView scoreView = (TextView)findViewById(R.id.scoreText);
            scoreView.setText(Integer.toString(route.getScore()));

            TextView distanceView = (TextView)findViewById(R.id.distanceText);
            DecimalFormat formatter = new DecimalFormat("#0.00");
            distanceView.setText(formatter.format(route.getDistance()/1000) + "km");
            //distanceView.setText(Double.toString(route.getDistance()));

            routeCoordinateList = dm.getAllRouteCoordinatesByRoute(route);


            MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
            GoogleMap map = mapFragment.getMap();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            //Adding markers on google map and adding the coordinates to the Bounds builder
            List<Marker> markers = new ArrayList<>();
            for (RouteCoordinate routeCoordinate: routeCoordinateList) {
                LatLng latLng = new LatLng(
                        routeCoordinate.getCoordinate().getLatitude(),
                        routeCoordinate.getCoordinate().getLongitude()
                );

                markers.add(map.addMarker(
                        new MarkerOptions().position(latLng)
                ));

                builder.include(latLng);
            }

            LatLngBounds bounds = builder.build();
            //Google map gets adjusted on the bounds that have been build based on the markers
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 200,200, 10);
            map.moveCamera(cu);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_detail, menu);
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
    public void onMapReady(GoogleMap googleMap) {

    }
}
