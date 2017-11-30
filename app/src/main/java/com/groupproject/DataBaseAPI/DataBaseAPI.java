package com.groupproject.DataBaseAPI;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.Model.Event;
import com.groupproject.Model.User;

import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;


public class DataBaseAPI {

    private static DatabaseReference mEventRef;
    private static DatabaseReference mUserRef;
    private static DatabaseReference mGroupRef;

    private static DataBaseAPI single_instance = null;


    private static ExpiringMap<String, Event> eventMap;



    private DataBaseAPI(){
        mUserRef = FirebaseDatabase.getInstance().getReference("users");
        mEventRef = FirebaseDatabase.getInstance().getReference("events");
        mGroupRef = FirebaseDatabase.getInstance().getReference("groups");
        eventMap = ExpiringMap.builder().variableExpiration().build();
        eventMap.addExpirationListener(new ExpirationListener<String, Event>() {
            @Override
            public void expired(String key, Event e) {
                e.setExpired(true);
                HashMap<String, Object> result = new HashMap<>();
                result.put(e.getId(), e);
                mEventRef.updateChildren(result);
                eventMap.remove(e.getId());
            }
        });
    }

    public static DataBaseAPI getDataBase() {
        if (single_instance == null) {
            single_instance = new DataBaseAPI();
        }
        return single_instance;
    }



    public void addChildListener(String collection, ChildEventListener childEventListener){
        if(collection.equals("events")){
            mEventRef.addChildEventListener(childEventListener);
        }

    }


    public DatabaseReference getmUserRef() {
        return mUserRef;
    }

    public DatabaseReference getmEventRef() {
        return mEventRef;
    }

    public DatabaseReference getmGroupRef() {
        return mGroupRef;
    }

    //TODO Validate user
    public void writeNewUser(User user) {
        mUserRef.child(user.getId())
                .setValue(user);
    }


    public void writeNewEvent(Event event) {
        event.setId(mEventRef.push().getKey());
        mEventRef.child(event.getId())
                .setValue(event);
    }

    public boolean checkIfFriends(){
        return true;
    }

    public void addEventToUser(FirebaseUser firebaseUser, Event event) {
        String key = mUserRef.child(firebaseUser.getUid()).child("goingEventsIDs").push().getKey();
        mUserRef.child(firebaseUser.getUid()).child("goingEventsIDs").child(key).setValue(event.getId());
    }

    public static ExpiringMap<String, Event> getEventMap() {
        return eventMap;
    }

    public static void loadActiveEvents() {
        Date date = new Date();
        Query activeEvents = mEventRef.orderByChild("endDate/time").startAt(date.getTime());
        activeEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event event = snapshot.getValue(com.groupproject.Model.Event.class);
                        if (event != null && eventMap.get(event.getId()) == null && !event.isExpired()) {
                            eventMap.put(event.getId(), event);
                            eventMap.setExpiration(event.getId(), event.calculateTimeRemaining(), TimeUnit.MILLISECONDS);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }




}


