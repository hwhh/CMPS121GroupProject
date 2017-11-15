package com.groupproject.Controller;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Event;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends FragmentActivity {

    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("groups").setIndicator("groups"),
                        GroupsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("maps").setIndicator("maps"),
                        MapsFragment.class, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onButtonClick1(View v){
        FirebaseFirestore db =FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user =  mAuth.getCurrentUser();


//
    }

}
//
//        String key = mDatabase.child("events").push().getKey();
//        Event e = new Event();
//        Map<String, Object> postValues = e.toMap();
//
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/events/" + key, postValues);
//        childUpdates.put("/users/" + user.getUid() + "/goingEvents/" + key, postValues);
//
//        mDatabase.updateChildren(childUpdates);
//
////        User u = db.collection("users").document(user.getUid());
////        Map<String, Object> map = new HashMap<>();
////        Event b = new Event();
////        Event c = new Event();
////
////        Map<String, Object> data = new HashMap<>();
////        DocumentReference newEventRef = db.collection("events").document();
////        newEventRef.set(data);
//
////        map.put("goingEvents", e.toMap());
//////        db.collection("users").document(e.getUid()).set(newUser);
////        map.put("goingEvents", b.toMap());
////        map.put("goingEvents", c.toMap());
//////        db.collection("users").document(user.getUid()).update(map, SetOptions.merge());
////
////        db.collection("users").document(user.getUid())
////                .update(
////                        "age", 13,
////                        ".color", "Red"
////                );
