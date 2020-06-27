package com.karmatech.zonedin;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.HashSet;

import static android.content.ContentValues.TAG;

@SuppressLint("OverrideAbstract")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class Notifications extends NotificationListenerService {
    HashSet<String> set;
    SharedPreferences sharedPreferences;
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        final String packageName = sbn.getPackageName();
        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        if(sharedPreferences!=null && (sharedPreferences.getBoolean("sleepModeOn",false) || sharedPreferences.getBoolean("gameModeOn",false)) && !packageName.contains("com.karmatech")){

            set = new HashSet<>();
            set.add(packageName);
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
