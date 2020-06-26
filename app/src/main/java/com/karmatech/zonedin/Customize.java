package com.karmatech.zonedin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Customize extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize);

        String[] mobileArray = {"App1","App1","App1","App1","App1","App1","App1"};
        Boolean[] switches = {true,true,true,true,true,true,true};
        MyAdapter adapter = new MyAdapter(this, mobileArray,switches);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }
}
