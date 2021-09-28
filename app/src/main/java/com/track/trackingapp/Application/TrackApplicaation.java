package com.track.trackingapp.Application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;


public class TrackApplicaation extends Application {

    public static Context appContext;// instance
    private static TrackApplicaation mInstance = null;


    public static final String TAG = TrackApplicaation.class.getSimpleName();

    public static SharedPreferences getSharedPreference(String name, int mode) {
        SharedPreferences preferences = getInstance().getSharedPreferences(
                name, mode);
        return preferences;
    }

    public static SharedPreferences.Editor getSharedPreferenceEditor(
            String name, int mode) {
        SharedPreferences preferences = getInstance().getSharedPreferences(
                name, mode);
        SharedPreferences.Editor editor = preferences.edit();
        return editor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        mInstance = this;
//        startService(new Intent(this, MyBackgroundService.class));

    }

    public static synchronized TrackApplicaation getInstance() {
        return mInstance;
    }


    public static Context getAppContext() {
        return appContext;
    }
}