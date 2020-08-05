package com.karmatech.zonedin;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MissedCallsNotifications extends AppCompatActivity {

    TextView notification, calls;
    HashMap<String, List<String>> mNotification;
    ArrayList<String> mCalls;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_missed_calls_notifications);
        notification = findViewById(R.id.missedNotifications);
        calls = findViewById(R.id.missedCalls);
        notification.setBackgroundResource(R.drawable.highlighter);
        calls.setBackgroundResource(0);
        loadFragment(new missedNotifications());
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void missedNotification(View view) {
        notification.setBackgroundResource(R.drawable.highlighter);
        calls.setBackgroundResource(0);
        //method to help load a fragment in this layout
        loadFragment(new missedNotifications());
    }

    public void missedCalls(View view) {
        notification.setBackgroundResource(0);
        calls.setBackgroundResource(R.drawable.highlighter);
        //add it to a bundle and send it to the fragment MissedCalls
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("mCalls",mCalls);
        //method to help load a fragment in this layout
        loadFragment(new MissedCalls());
    }



    private void loadFragment(Fragment fragment) {
// create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();
// create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
// replace the FrameLayout with new Fragment
        //fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SharedPreferences preferences =getSharedPreferences(MainActivity.NPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }
}
