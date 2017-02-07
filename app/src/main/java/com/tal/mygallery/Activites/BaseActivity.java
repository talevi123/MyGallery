package com.tal.mygallery.Activites;

import android.support.v7.app.AppCompatActivity;

import com.tal.mygallery.MyApplication;

/**
 * Created by tal on 28/12/16.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPause() {
        MyApplication.getInstance().setCurrentActivity(null);
        super.onPause();
    }
}
