package com.lions.app.sportsstore.structs;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;

/**
 * Created by Panwar on 28/01/18.
 */

public class LocationSession {
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    String KEY_ADD = "Address";
    String KEY_LAT = "Latitude";
    String KEY_LONG = "Longitude";


    public LocationSession(Context context) {
        _context = context;
    }

    public boolean CreateLocationSession(CharSequence address, double latitude, double longitude) {
        editor.clear();
        editor.commit();
        // Storing name in pref


        editor.putString(KEY_ADD, String.valueOf(address));
        editor.putString(KEY_LAT, String.valueOf(latitude));
        editor.putString(KEY_LONG, String.valueOf(longitude));

        // commit changes
        editor.commit();
        return true;

    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_ADD, pref.getString(KEY_ADD, null));
        user.put(KEY_LAT, pref.getString(KEY_LAT, null));
        user.put(KEY_LONG, pref.getString(KEY_LONG, null));

        // return user
        return user;


    }

}