package com.karmatech.zonedin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


public class MyAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] maintitle;
    private final Boolean[] switchedOn;

    public MyAdapter(Activity context, String[] maintitle, Boolean[] switches) {
        super(context, R.layout.listview_custom_apps, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
        this.switchedOn = switches;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listview_custom_apps, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.appName);
        titleText.setText(maintitle[position]);
        Switch appSwitch = rowView.findViewById(R.id.appSwitch);
        appSwitch.setChecked(switchedOn[position]);

        return rowView;

    };
}