package com.aztechcorps.texter.Services;

import android.content.Context;
import android.content.SharedPreferences;

import com.aztechcorps.texter.Utils.AppConstants;
import com.aztechcorps.texter.Utils.Helpers;

import java.util.Date;

/**
 * Created by Subhamay on 07-Apr-18.
 */

public class SharedPreferencesHandler {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesHandler(Context ctx) {
        this.context = ctx;
        sharedPreferences = context.getSharedPreferences(AppConstants.APP_SHARED_PREF, Context.MODE_PRIVATE);
    }

    public void init() {
        Date d = new Date();
        sharedPreferences.edit()
                .putBoolean(AppConstants.APP_SHARED_PREF_INIT, true)
                .putString(AppConstants.APP_SHARED_PREF_TOKEN, Helpers.md5(d.getTime() + ""))
                .apply();
    }

    public boolean check() {
        return sharedPreferences.getBoolean(AppConstants.APP_SHARED_PREF_INIT, false);
    }

    public String getToken() {
        return sharedPreferences.getString(AppConstants.APP_SHARED_PREF_TOKEN, null);
    }
}
