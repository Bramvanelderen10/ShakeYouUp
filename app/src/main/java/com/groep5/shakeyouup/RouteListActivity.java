package com.groep5.shakeyouup;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class RouteListActivity extends ActionBarActivity {

    private ArrayList<HashMap<String, String>> list;
    private DatabaseManager dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_route_list);

        dm = new DatabaseManager(this);
        List<Route> routes = dm.getAllRoutes();
        list = new ArrayList<>();

        HashMap<String,String> title = new HashMap<String, String>();
        title.put("1", "id");
        title.put("2", "start");
        title.put("3", "end");
        title.put("4", "score");
        title.put("5", "distance");
        title.put("6", "time");
        list.add(title);

        for (Route route : routes) {

            String id = Integer.toString(route.getId());
            String distance = String.valueOf(route.getDistance());
            String score = String.valueOf(route.getScore());
            String time = String.valueOf(route.getTime());
            String start = route.getStartLocation().getName();
            String end = route.getEndLocation().getName();

            HashMap<String,String> temp = new HashMap<String, String>();
            temp.put("1", id);
            temp.put("2", start);
            temp.put("3", end);
            temp.put("4", score);
            temp.put("5", distance);
            temp.put("6", time);
            list.add(temp);
        }
        RouteListAdapter routeListAdapter = new RouteListAdapter(this, list);
        ListView routeListView = (ListView)findViewById(R.id.RouteListView);
        routeListView.setAdapter(routeListAdapter);

        routeListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id)
            {
                if (position != 0) {
                    HashMap<String, String> hash = list.get(position);
                    hash.size();
                    int routeId = Integer.parseInt(hash.get("1"));
                    viewRouteDetails(routeId);
                }
            }
        });
    }

    private void viewRouteDetails(int id) {

        Intent intent = new Intent(this, RouteDetailActivity.class);
        intent.putExtra("route_id", id);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_route_list, menu);
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
}
