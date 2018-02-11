package com.lions.app.sportsstore.structs;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Panwar on 06/02/18.
 */

public class UserSession {

    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    public static final String KEY_EMAIL = "email";

    // Email address (make variable public to access from outside)
    public static final String KEY_NAME = "name";
    public  static final String KEY_UID = "uid";
    public static final String KEY_DP = "dp";

    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "Laltern";

    public UserSession(Context context)
    {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean CreateUser(String name, String uid, String dp, String email)
    {
        editor.clear();
        editor.commit();


        // Storing name in pref
        editor.putString(KEY_EMAIL, email);

        // Storing email in pref
        editor.putString(KEY_NAME, name);

        editor.putString(KEY_UID,uid);

        editor.putString(KEY_DP, dp);

        // commit changes
        editor.commit();


        return true;

    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user name
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));

        // user email id
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        user.put(KEY_UID, pref.getString(KEY_UID,null));

        user.put(KEY_DP,pref.getString(KEY_DP,null));


        // return user
        return user;

    }

    public void ClearSession()
    {
        editor.clear();
        editor.commit();
    }


}
