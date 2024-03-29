package com.groupproject.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groupproject.Controller.LoginActivities.LoginActivity;

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //Duration of splash screen in milliseconds
        int SPLASH_DISPLAY_LENGTH = 1000;
        new Handler().postDelayed(() -> {
            Intent intent;
            if (user != null) {
                // User is signed in
                intent = new Intent(SplashActivity.this, BaseActivity.class);
            } else {
                // No user is signed in
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        }, SPLASH_DISPLAY_LENGTH);
    }
}
