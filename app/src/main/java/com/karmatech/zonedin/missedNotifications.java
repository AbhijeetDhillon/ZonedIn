package com.karmatech.zonedin;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.karmatech.zonedin.Notifications.map;

public class missedNotifications extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    private SharedPreferences mNotificationsShared;
    private static Set<String> missedNoti = new HashSet<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missed_notifications, container, false);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        mNotificationsShared = getActivity().getSharedPreferences(MainActivity.NPREFERENCES,Context.MODE_PRIVATE);
        missedNoti = mNotificationsShared.getStringSet("NotificationsMissed",new HashSet<String>());
        final PackageManager pm = getActivity().getApplicationContext().getPackageManager();
        ArrayList<String> appName = new ArrayList<>();
        ArrayList<String> appPackage = new ArrayList<>();
        ArrayList<Drawable> appIcon = new ArrayList<>();
        ApplicationInfo ai;
        if(!missedNoti.isEmpty()){
            for(String packageName : missedNoti){

                try {
                    ai = pm.getApplicationInfo( packageName, 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }

                appName.add((String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));

                Drawable icon = null;
                 //getting the app icon
                    try {
                        //icon = getActivity().getApplicationInfo().loadIcon(getActivity().getApplicationContext().getPackageManager());
                       icon = getActivity().getApplicationContext().getPackageManager().getApplicationIcon(packageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    appIcon.add(icon);
                    appPackage.add(packageName);




//                    ApplicationInfo applicationInfo = null;
//                    try {
//                        applicationInfo = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
//                    } catch (PackageManager.NameNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                    final int appIconResId=applicationInfo.icon;
//                    int resID = getActivity().getResources().getIdentifier(icon.toString(),"id",packageName);
//                    HashMap<String, String> hm = new HashMap<String, String>();
//                    hm.put("listview_title", appName);
//                    hm.put("packageName", packageName);
//                    hm.put("listview_image", Integer.toString(appIconResId));
//                    aList.add(hm);
            }
        }

        //Adapter to display the missed notifications
        ListView androidListView = (ListView) view.findViewById(R.id.mnlistView);
        //ListView listView = (ListView) view.findViewById(R.id.mnlistView);
        TextView noNotifications = view.findViewById(R.id.noNotifications);
        if(!missedNoti.isEmpty()) {

//
//            String[] from = {"listview_image", "listview_title",};
//            int[] to = {R.id.listview_image, R.id.appName,};
//            SimpleAdapter simpleAdapter = new SimpleAdapter(view.getContext(),aList, R.layout.missed_notification_listview, from, to);
//            androidListView.setAdapter(simpleAdapter);
//
//            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View container, int position, long id) {
//                    HashMap<String, String> item = (HashMap<String, String>) parent.getItemAtPosition(position);
//                    String value = item.get("packageName");
//                    Intent launchIntent = getActivity().getPackageManager().getLaunchIntentForPackage(value);
//                    if (launchIntent != null) {
//                        startActivity(launchIntent);//null pointer check in case package name was not found
//                    }
//                }
//            };

//            androidListView.setOnItemClickListener(itemClickListener);

            androidListView.setVisibility(View.VISIBLE);
            noNotifications.setVisibility(View.GONE);
            CustomNotificationAdapter customNotificationAdapter = new CustomNotificationAdapter(view.getContext(),appName,appPackage,appIcon);
            androidListView.setAdapter(customNotificationAdapter);
        }
        else{
            noNotifications.setVisibility(View.VISIBLE);
            androidListView.setVisibility(View.GONE);
        }
        return view;
    }
}
