package com.karmatech.zonedin;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MissedCalls extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_missed_calls, container, false);
        // initialize the bundle class
        Bundle bundle = this.getArguments();
        // get the missedCalls data of the it's there
        ListView listView = (ListView) view.findViewById(R.id.mclistView);
        TextView noCalls = view.findViewById(R.id.noCalls);
        SharedPreferences mCalls = getActivity().getSharedPreferences(MainActivity.NPREFERENCES, Context.MODE_PRIVATE);
        Set<String> missedCalls = mCalls.getStringSet("CallsMissed", new HashSet<String>());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if(!missedCalls.isEmpty()){
                //Call the Adapter and display the data in listview
                listView.setVisibility(View.VISIBLE);
                noCalls.setVisibility(View.GONE);
                MissedCallsAdapter adapter = new MissedCallsAdapter(getActivity(), new ArrayList<String>(missedCalls));
                listView.setAdapter(adapter);

            }
            else{
                listView.setVisibility(View.GONE);
                noCalls.setVisibility(View.VISIBLE);
            }
        }

        return view;
    }

}
