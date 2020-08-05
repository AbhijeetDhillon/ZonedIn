package com.karmatech.zonedin;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.ContactsContract;
import android.telecom.Call;
import android.telecom.CallScreeningService;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.RequiresApi;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.N)
public class IncomingCallReceiver extends CallScreeningService {
    SharedPreferences sharedPreferences,mCalls;
    static Set<String> missedCalls;

    @Override
    public void onScreenCall(Call.Details callDetails) {
        missedCalls = new HashSet<>();
        sharedPreferences = getSharedPreferences(MainActivity.MyPREFERENCES,Context.MODE_PRIVATE);
        mCalls = getSharedPreferences(MainActivity.NPREFERENCES,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mCalls.edit();
        if(sharedPreferences!=null && (sharedPreferences.getBoolean("gameModeOn",false)) && callDetails.getCallDirection() == Call.Details.DIRECTION_INCOMING) {

            //missedCalls.add(callDetails.getCallerDisplayName());
            String phoneNumber = callDetails.getHandle().getSchemeSpecificPart();
            String formattedPhoneNumber  = PhoneNumberUtils.formatNumber(phoneNumber, Locale.getDefault().getCountry());
            if(getContactName(phoneNumber)!=null){
                String contactName = getContactName(phoneNumber);
                missedCalls = mCalls.getStringSet("CallsMissed",new HashSet<String>());
                missedCalls.add(contactName);
            }
            else if(phoneNumber!=null){
                missedCalls = mCalls.getStringSet("CallsMissed",new HashSet<String>());
                missedCalls.add(formattedPhoneNumber);
            }

            editor.putStringSet("CallsMissed",missedCalls);
            editor.apply();
            respondToCall(callDetails,  new CallResponse.Builder ()
                    .setDisallowCall(true)
                    .setSkipNotification(true)
                    .setSilenceCall(true)
                    .setRejectCall(false)
                    .setSkipCallLog(false)
                    .build());
        }
    }

    private String getContactName(String phoneNumber){
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        String contactname = null;
        String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};
        Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
        if(cursor!=null && cursor.moveToFirst()){
            contactname =  cursor.getString(0);
            cursor.close();
        }
        return contactname;
    }
}