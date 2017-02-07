package com.tal.mygallery.Activites;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.tal.mygallery.Network.Utility;
import com.tal.mygallery.R;

import java.util.Arrays;

public class IntroActivity extends AppCompatActivity {

    boolean firstLaunch;
    Button loginBtn;
    private CallbackManager callbackManager;
    boolean result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (PreferenceManager.getDefaultSharedPreferences(this).
                getBoolean("isFirstLaunch", firstLaunch)) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        firstLaunch = true;
        PreferenceManager.getDefaultSharedPreferences(this).edit().
                putBoolean("isFirstLaunch", firstLaunch).apply();

        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        loginManager();

        setContentView(R.layout.activity_intro);

        result = Utility.checkPermission(IntroActivity.this);

        getStarted();

    }

    private void loginManager() {
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        String accessToken = loginResult.getAccessToken().getToken();

                        Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                        intent.putExtra("accessToken", accessToken);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(IntroActivity.this, "Login Cancel", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(IntroActivity.this, exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void getStarted() {
        loginBtn = (Button) findViewById(R.id.started_btn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(IntroActivity.this, Arrays.asList("public_profile", "user_photos"));

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager.onActivityResult(requestCode, resultCode, data)) {
            return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                //getPhoneGallery();
                break;
        }
    }
}


