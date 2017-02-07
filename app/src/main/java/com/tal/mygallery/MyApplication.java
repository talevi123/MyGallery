package com.tal.mygallery;

import android.app.Activity;
import android.app.Application;
import android.support.multidex.MultiDex;

/**
 * Created by tal on 28/12/16.
 */
public class MyApplication extends Application {

    private static MyApplication instance;
    private Activity currentActivity;

    @Override
    public void onCreate() {
        MultiDex.install(this);
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

}
