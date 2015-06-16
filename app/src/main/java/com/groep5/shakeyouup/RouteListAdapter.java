package com.groep5.shakeyouup;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bram on 6/17/2015.
 */
public class RouteListAdapter extends BaseAdapter {

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    TextView txtFirst;
    TextView txtSecond;
    TextView txtThird;
    TextView txtFourth;
    TextView txtFifth;
    TextView txtSixth;

    public RouteListAdapter(Activity activity,ArrayList<HashMap<String, String>> list){
        super();
        this.activity=activity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = activity.getLayoutInflater();

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.route_column, null);

            txtFirst = (TextView) convertView.findViewById(R.id.id);
            txtSecond = (TextView) convertView.findViewById(R.id.startLocation);
            txtThird = (TextView) convertView.findViewById(R.id.endLocation);
            txtFourth = (TextView) convertView.findViewById(R.id.score);
            txtFifth = (TextView) convertView.findViewById(R.id.distance);
            txtSixth = (TextView) convertView.findViewById(R.id.time);

        }

        HashMap<String, String> map = list.get(position);
        txtFirst.setText(map.get("1"));
        txtSecond.setText(map.get("2"));
        txtThird.setText(map.get("3"));
        txtFourth.setText(map.get("4"));
        txtFifth.setText(map.get("5"));
        txtSixth.setText(map.get("6"));

        return convertView;
    }
}
