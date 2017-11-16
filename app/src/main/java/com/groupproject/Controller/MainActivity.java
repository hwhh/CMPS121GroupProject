package com.groupproject.Controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Event;
import com.groupproject.R;

public class MainActivity extends FragmentActivity {

    private static final int MAPS_INDEX = 1;
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //TODO change back to Events frag
        mTabHost.addTab(mTabHost.newTabSpec("events").setIndicator("events"),
                        GroupsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("maps").setIndicator("maps"),
                        MapsFragment.class, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public void onButtonClick1(View v) {
        Event e = new Event();
        DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        dataBaseAPI.addEventToUser(firebaseUser, e);

    }
    public void switchToMaps(Event event) {
        mTabHost.setCurrentTab(MAPS_INDEX);
        //TODO: Open maps on event's location
    }




}











//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
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
