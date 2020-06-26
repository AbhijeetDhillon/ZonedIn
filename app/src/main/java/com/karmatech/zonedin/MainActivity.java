package com.karmatech.zonedin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    static String MyPREFERENCES = "Modes";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        if (Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName()))
        {

        } else {
            //service is not enabled try to enabled by calling...
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }

        Switch sleepMode = findViewById(R.id.sleepModeSwitch);
        if(sharedpreferences!=null && sharedpreferences.getBoolean("sleepModeOn",false))
        {
            sleepMode.setChecked(true);
        }
        final Intent notificationIntent = new Intent(getApplicationContext(),MainActivity.class);
        sleepMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // Switch On
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                        .setSmallIcon(R.drawable.sleep) // notification icon
                        .setContentTitle("ZonedIn") // title for notification
                        .setContentText("Sleep Mode On")// message for notification
                        .setAutoCancel(false) // clear notification after click
                        .setOngoing(true);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                            "YOUR_CHANNEL_NAME",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                    mNotificationManager.createNotificationChannel(channel);
                }

                if(isChecked){

                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    mNotificationManager.notify(0, mBuilder.build());


                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("sleepModeOn", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Sleep Mode On : Notifications Paused",Toast.LENGTH_LONG).show();
                }
                //Switch Off
                else{
                    //alert dialog of missed notifications if HashSet is not empty;
                    mBuilder.setAutoCancel(true);
                    mNotificationManager.cancelAll();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("sleepModeOn", false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Sleep Mode Off : Notifications Resumed",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void Custom(View view) {
        Intent intent = new Intent(this,Customize.class);
        startActivity(intent);

    }
}
