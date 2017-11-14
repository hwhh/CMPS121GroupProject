package com.groupproject.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groupproject.Authentication.LoginActivity;

public class SplashActivity extends AppCompatActivity {



    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            intent = new Intent(this, LoginActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
            // No user is signed in
        }

        startActivity(intent);
        finish();
    }
}
