package com.groupproject.DataBaseAPI;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.Model.Event;


public class EventLister implements ValueEventListener {

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        Event event = dataSnapshot.getValue(Event.class);
        System.out.println();
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
//    @Override
//    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//        String value = dataSnapshot.getValue(String.class);
//
//    }
//
//    @Override
//    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//    }
//
//    @Override
//    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//    }
//
//    @Override
//    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//    }
//
//    @Override
//    public void onCancelled(DatabaseError databaseError) {
//
//    }
}
