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


//    public void somthing(User user){
//        FirebaseFirestore mDatabase =FirebaseFirestore.getInstance();
//        Map<String, Object> map = new HashMap<>();
//        Event e = new Event();
//        Event b = new Event();
//        Event c = new Event();
//        map.put("activities", e);
//        mDatabase.collection("user").document(user.getId()).update(map);
//        map.put("activities", b);
//        mDatabase.collection("user").document(user.getId()).update(map);
//        map.put("activities", c);
//        mDatabase.collection("user").document(user.getId()).update(map);
//
//    }
//
//
//    public void updateElement(String collection, String child) {
//        ref.child(collection).child(child).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // use "username" already exists
//                } else {
//                    // "username" does not exist yet.
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


//
//        Map<String, Object> user = new HashMap<>();
//        user.put("id", currentUser.getUid());
//        user.put("name", currentUser.getDisplayName());
//        user.put("email", currentUser.getEmail());
//
//        mDatabase.collection("users")
//                .document("some id")
//                .set(user);
//    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());


//    Firebase m_objFireBaseRef = new Firebase(AppConstants.FIREBASE_URL);
//    Firebase objRef = m_objFireBaseRef.child("tasks");
//    Query pendingTasks = objRef.orderByChild("Status").equalTo("PENDING");
//    pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot tasksSnapshot) {
//            for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
//                snapshot.getRef().child("Status").setValue("COMPLETED");
//            }
//        }
//        @Override
//        public void onCancelled(FirebaseError firebaseError) {
//            System.out.println("The read failed: " + firebaseError.getMessage());
//        }
//    });
//    ref.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            final Profile tempProfile = new Profile(); //this is my user_class Class
//            final Field[] fields = tempProfile.getClass().getDeclaredFields();
//            for(Field field : fields){
//                Log.i(TAG, field.getName() + ": " + dataSnapshot.child(field.getName()).getValue());
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//        }
//    });