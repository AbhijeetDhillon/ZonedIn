package com.karmatech.zonedin;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MissedCallsAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> maintitle;

    //Both methods are called one by one depending on the number of data
    public MissedCallsAdapter(Activity context, ArrayList<String> maintitle) {
        super(context, R.layout.missed_call_listview, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.missed_call_listview, null,true);
        TextView titleText = (TextView) rowView.findViewById(R.id.appName);
        titleText.setText(maintitle.get(position));
        return rowView;

    };
}
