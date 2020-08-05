package com.karmatech.zonedin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.role.RoleManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 0;
    SharedPreferences sharedpreferences;
    static String MyPREFERENCES = "Modes";
    static String NPREFERENCES = "Notifications";
    Switch sleepMode;
    Switch gameMode;
    TextView notificationsDisabled;
    TextView callsDisabled;
    TextView notificationsEnabled;
    TextView callsEnabled;
    int height,width;
    private static final int REQUEST_ID = 1;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sleepMode = findViewById(R.id.sleepModeSwitch);
        gameMode = findViewById(R.id.gameModeSwitch);
        notificationsDisabled = findViewById(R.id.notificationDisabled);
        callsDisabled = findViewById(R.id.callsDisabled);
        notificationsEnabled = findViewById(R.id.notificationEnabled);
        callsEnabled = findViewById(R.id.callsEnabled);
        //Call Screening Permission
        NotificationManager mNotificationManager = (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the notification policy access has been granted for the app.
        if (!mNotificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }

        //Checks if permission to read notification is granted.
        if (Settings.Secure.getString(this.getContentResolver(),"enabled_notification_listeners").contains(getApplicationContext().getPackageName()))
        {
            //Permission Granted - Continue
        } else {
            //Permission Not Granted - Redirect to settings screen for permission
            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        }
        //Checks if permission to read contacts is granted.
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Permission Not Granted - Ask for permission
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    PERMISSIONS_REQUEST_READ_CONTACTS);
        }

        //Get Device dimensions
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        //Call the modes
        SleepMode();
        GameMode();

    }

    // Method to goto Custom Activity
    public void Custom(View view) {
        Intent intent = new Intent(this,Customize.class);
        startActivity(intent);

    }

    //SleepMode Functoion
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
                    NotificationChannel channel = new NotificationChannel("180895",
                            "ZonedIn",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("Mode Activated");
                    mNotificationManager.createNotificationChannel(channel);
                }

                //Switch On
                if(isChecked){
                    //check if the mode is still running even when the app is closed. If running Switch Off the other Mode(Calls).
                    // Only one mode can be ON at once. So this condition helps. if one mode is on then it switches the other mode off.
                    if(sharedpreferences!=null && sharedpreferences.getBoolean("gameModeOn",false)) {
                        gameMode.setChecked(false);
                        SharedPreferences.Editor editorGame = sharedpreferences.edit();
                        editorGame.putBoolean("gameModeOn", false);
                        editorGame.apply();
                        //Display a AlertBox to ask the user to see the missed notifications and/or calls
                        AlertBox();
                    }

                    //Notification added to the Notification Bar if Switch is On
                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    mNotificationManager.notify(0, mBuilder.build());
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("sleepModeOn", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Sleep Mode On : Notifications Paused",Toast.LENGTH_LONG).show();

                    //Change text view color and background
                    disableNotification();
                    enableCalls();
                }
                //Switch Off
                else{
                    //Display a AlertBox to ask the user to see the missed notifications and/or calls
                    AlertBox();

                    //Notification removed to the Notification Bar if Switch is On
                    mBuilder.setAutoCancel(true);
                    mNotificationManager.cancelAll();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("sleepModeOn", false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Sleep Mode Off : Notifications Resumed",Toast.LENGTH_LONG).show();

                    //Change text view color and background
                    enableNotification();
                    enableCalls();
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
                    //check if the mode is still running even when the app is closed. If running Switch Off the other Mode(Notification Mode).
                    // Only one mode can be ON at once. So this condition helps. if one mode is on then it switches the other mode off.
                    if(sharedpreferences!=null && sharedpreferences.getBoolean("sleepModeOn",false)){
                        sleepMode.setChecked(false);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("sleepModeOn", false);
                        editor.apply();
                        //Display a AlertBox to ask the user to see the missed notifications and/or calls
                        AlertBox();
                    }

                    //Notification added to the Notification Bar if Switch is On
                    PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    mBuilder.setContentIntent(pi);
                    mNotificationManager.notify(0, mBuilder.build());


                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("gameModeOn", true);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Game Mode On",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,IncomingCallReceiver.class);
                    startService(intent);

                    //Change text view color and background
                    disableCalls();
                    disableNotification();


                }
                //Switch Off
                else{
                    //Display a AlertBox to ask the user to see the missed notifications and/or calls
                   AlertBox();

                    //Notification removed to the Notification Bar if Switch is On
                    mBuilder.setAutoCancel(true);
                    mNotificationManager.cancelAll();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putBoolean("gameModeOn", false);
                    editor.apply();
                    Toast.makeText(getApplicationContext(),"Game Mode Off",Toast.LENGTH_LONG).show();

                    //Change text view color and background
                    enableCalls();
                    enableNotification();

                }

            }
        });

    }

// Code for Custom AlertBox
    public void AlertBox(){
        //uses custom layout.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.view_missed, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        TextView next = dialogView.findViewById(R.id.next);
        TextView close = dialogView.findViewById(R.id.close);

        // if user wants to see the missed calls and notifications,
        //the HashSets from Notifications Service(missedNoti) and IncomingCallReceiver Service(missedCalls)
        // is converted to ArrayList of a String and Passed to the MissedCallsNotifications activity;
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MissedCallsNotifications.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if(IncomingCallReceiver.missedCalls!=null)
                    intent.putExtra("missedCalls",new ArrayList<String>(IncomingCallReceiver.missedCalls));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    if(Notifications.map!=null)
                    intent.putExtra("missedNotifications",(Notifications.map));
                }
                startActivity(intent);
                alertDialog.dismiss();
            }
        });

        //close the dialog box;
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

    }
    // checks if screening permission granted
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ID) {
            if (resultCode == Activity.RESULT_OK) {
                // Your app is now the call screening app
            } else {
                requestRole();
            }
        }
    }
    // request for screening permission
    public void requestRole() {
        RoleManager roleManager = (RoleManager) getSystemService(ROLE_SERVICE);
        Intent intent = roleManager.createRequestRoleIntent(RoleManager.ROLE_CALL_SCREENING);
        startActivityForResult(intent, REQUEST_ID);
    }

    public void disableNotification(){
        notificationsEnabled.setVisibility(View.INVISIBLE);
        notificationsDisabled.setVisibility(View.VISIBLE);
    }
    public void disableCalls(){
        callsEnabled.setVisibility(View.INVISIBLE);
        callsDisabled.setVisibility(View.VISIBLE);
    }
    public void enableNotification(){
        notificationsEnabled.setVisibility(View.VISIBLE);
        notificationsDisabled.setVisibility(View.INVISIBLE);
    }
    public void enableCalls(){
        callsEnabled.setVisibility(View.VISIBLE);
        callsDisabled.setVisibility(View.INVISIBLE);
    }





}
