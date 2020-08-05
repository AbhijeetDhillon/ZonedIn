package com.karmatech.zonedin;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomNotificationAdapter implements ListAdapter {

    private ArrayList<String> appName;
    private ArrayList<String> appPackage;
    private ArrayList<Drawable> appIcon;
    Context context;
    public CustomNotificationAdapter(Context context, ArrayList<String> appName, ArrayList<String> appPackage, ArrayList<Drawable> appIcon){
        this.appIcon = appIcon;
        this.appName = appName;
        this.context = context;
        this.appPackage = appPackage;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getCount() {
        return appName.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String mAppName = appName.get(position);
        Drawable mAppIcon = appIcon.get(position);
        final String mAppPackage = appPackage.get(position);
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.missed_notification_listview,null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(mAppPackage);
                    if (launchIntent != null) {
                        context.startActivity(launchIntent);//null pointer check in case package name was not found
                    }
                }
            });

            TextView textView = convertView.findViewById(R.id.appName);
            ImageView imageView = convertView.findViewById(R.id.listview_image);
            textView.setText(mAppName);
            imageView.setImageDrawable(mAppIcon);
            return convertView;



    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return appName.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
