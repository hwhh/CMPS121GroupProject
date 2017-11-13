package com.groupproject.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.groupproject.Authentication.FacebookAuthenticationActivity;
import com.groupproject.Authentication.GoogleAuthenticationActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        Intent intent;
        if (accessToken == null) {
            intent = new Intent(this, GoogleAuthenticationActivity.class);
        } else {
            intent = new Intent(this, MainActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
