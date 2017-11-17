package com.groupproject.DataBaseAPI;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.groupproject.Model.Event;

import org.apache.commons.collections4.map.PassiveExpiringMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;


public class EventLister extends Observable implements ChildEventListener {


//    Marker marker = map.addMarker(new MarkerOptions()
//            .position(value.getLocation().getLatitude(), value.getLocation().getLongitude())
//            .snippet(""));


    private Map<String, Event> eventMap = new HashMap<>();

    private PassiveExpiringMap<String, Event> events = new PassiveExpiringMap<>(
            new PassiveExpiringMap.ExpirationPolicy<String, Event>() {
        @Override
        public long expirationTime(String key, Event value) {
            return value.getEndDate().getTime() - value.getStartDate().getTime();
        }
    });


    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Event value = dataSnapshot.getValue(com.groupproject.Model.Event.class);
        eventMap.put(value.getId(), value);
    }


    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
