package com.karmatech.zonedin;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static android.content.ContentValues.TAG;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Notifications extends NotificationListenerService {
    static Set<String> missedNoti = new HashSet<>();
    //HashMap to store app name,package name and icon.
    static HashMap<String, String[]> map = new HashMap<>();
    SharedPreferences sharedPreferences,mNotifications;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        final String packageName = sbn.getPackageName();
        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        mNotifications = getSharedPreferences(MainActivity.NPREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mNotifications.edit();
         if(sharedPreferences!=null && (sharedPreferences.getBoolean("sleepModeOn",false) || sharedPreferences.getBoolean("gameModeOn",false)) && !packageName.contains("com.karmatech")){

            //code to get the package name of the apps.
            final PackageManager pm = getApplicationContext().getPackageManager();
            ApplicationInfo ai;
            try {
                ai = pm.getApplicationInfo( sbn.getPackageName(), 0);
            } catch (final PackageManager.NameNotFoundException e) {
                ai = null;
            }
            missedNoti = mNotifications.getStringSet("NotificationsMissed",new HashSet<String>());
            missedNoti.add(sbn.getPackageName());
            editor.putStringSet("NotificationsMissed",missedNoti);
            editor.apply();
            // setting the default icon for the app
            Drawable icon = getResources().getDrawable(R.drawable.ic_launcher_foreground);
            try
            {
                //getting the app icon
                icon = getApplicationContext().getPackageManager().getApplicationIcon(sbn.getPackageName());
            }
            catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
            //getting the app name
            final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
            //List of package name and icon as string
            String[] appInfo = new String[2];
            appInfo[0] = (sbn.getPackageName());
            appInfo[1] = (String.valueOf(icon));

            map.put(applicationName,appInfo);
            //missedNoti.add(applicationName);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                cancelNotification(sbn.getPackageName(), sbn.getTag(), sbn.getId());
            }
            else {
                cancelNotification(sbn.getKey());
            }
        }

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Nothing to do
        Log.d(TAG, "onNotificationRemoved: Notification removed for" + sbn.getPackageName());
    }
}
