package com.karmatech.zonedin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
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
    Switch sleepMode;
    Switch gameMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sleepMode = findViewById(R.id.sleepModeSwitch);
        gameMode = findViewById(R.id.gameModeSwitch);


        //Checks if permission to read notification is granted.
        if (Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName()))
        {
            //Permission Granted - Continue
        } else {
            //Permission Not Granted - Redirect to settings screen for permission
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }


            SleepMode();
            GameMode();

    }

    // Method to goto Custom Activity
    public void Custom(View view) {
        Intent intent = new Intent(this,Customize.class);
        startActivity(intent);

    }

    private void SleepMode(){
        final Intent notificationIntent = new Intent(getApplicationContext(),MainActivity.class);
        //SleepMode Switch
        Switch sleepMode = findViewById(R.id.sleepModeSwitch);

        //if app is closed then this condition would keep it from stopping notifications
        if(sharedpreferences!=null && sharedpreferences.getBoolean("sleepModeOn",false))sleepMode.setChecked(true);

        //SleepMode Switch Listener
        sleepMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Notification created
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

                //Switch On
                if(isChecked){

                    if(sharedpreferences!=null && sharedpreferences.getBoolean("gameModeOn",false)) {
                        gameMode.setChecked(false);
                        SharedPreferences.Editor editorGame = sharedpreferences.edit();
                        editorGame.putBoolean("gameModeOn", false);
                        editorGame.apply();
                    }

                    //Notification added to the Notification Bar if Switch is On
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
                    //TODO Add a dialog saying "check what you've missed" and redirect user to MissedActivity

                    //Notification removed to the Notification Bar if Switch is On
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

    private void GameMode(){
        Switch gameMode = findViewById(R.id.gameModeSwitch);
        final Intent notificationIntent = new Intent(getApplicationContext(),MainActivity.class);
        //if app is closed then this condition would keep it from stopping notifications
        if(sharedpreferences!=null && sharedpreferences.getBoolean("gameModeOn",false))gameMode.setChecked(true);

        gameMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Notification created
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                        .setSmallIcon(R.drawable.game) // notification icon
                        .setContentTitle("ZonedIn") // title for notification
                        .setContentText("Game/Movie Mode On")// message for notification
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

                //Switch On
                if(isChecked){

                    if(sharedpreferences!=null && sharedpreferences.getBoolean("sleepModeOn",false)){
                        sleepMode.setChecked(false);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("sleepModeOn", false);
                        editor.apply();
                    }

                    //Notification added to the Notification Bar if Switch is On
                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    mNotificationManager.notify(0, mBuilder.build());


                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("gameModeOn", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Game Mode On : Notifications & Calls Paused",Toast.LENGTH_LONG).show();
                }
                //Switch Off
                else{
                    //TODO Add a dialog saying "check what you've missed" and redirect user to MissedActivity

                    //Notification removed to the Notification Bar if Switch is On
                    mBuilder.setAutoCancel(true);
                    mNotificationManager.cancelAll();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("gameModeOn", false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Game Mode Off : Notifications & Calls Resumed",Toast.LENGTH_LONG).show();
                }

            }
        });

    }


}
