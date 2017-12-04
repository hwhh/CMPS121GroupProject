package com.groupproject.DataBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.Controller.LoginActivities.LoginActivity;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;

import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.ME;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.FRIENDS;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.REQUESTED;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.NONE;


public class DataBaseAPI {

    private static final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static DatabaseReference mEventRef;
    private static DatabaseReference mUserRef;
    private static DatabaseReference mGroupRef;
    private DataBaseCallBacks dataBaseCallBacks;
    private static DataBaseAPI single_instance = null;
    private static ExpiringMap<String, Event> eventMap;

    public enum UserRelationship {
        ME,
        FRIENDS,
        REQUESTED,
        NONE
    }

    private DataBaseAPI(){
        mUserRef = FirebaseDatabase.getInstance().getReference("users");
        mEventRef = FirebaseDatabase.getInstance().getReference("events");
        mGroupRef = FirebaseDatabase.getInstance().getReference("groups");
        eventMap = ExpiringMap.builder().variableExpiration().build();
        eventMap.addExpirationListener((key, e) -> {
            e.setExpired(true);
            HashMap<String, Object> result = new HashMap<>();
            result.put(e.getId(), e);
            mEventRef.updateChildren(result);
            eventMap.remove(e.getId());
        });
    }

    public static DataBaseAPI getDataBase() {
        if (single_instance == null) {
            single_instance = new DataBaseAPI();
        }
        return single_instance;
    }


    public void signOut(Activity activity){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
    }


    public void addChildListener(String collection, ChildEventListener childEventListener) {
        if (collection.equals("events")) {
            mEventRef.addChildEventListener(childEventListener);
        }
    }


    public void getUser(String id, DataBaseCallBacks callBacks, @Nullable ViewHolder holder){
        Query query = getmUserRef().child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(com.groupproject.Model.User.class);
                    dataBaseCallBacks = callBacks;
                    dataBaseCallBacks.getUser(user, holder);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void getEvent(String id, DataBaseCallBacks callBacks, @Nullable ViewHolder holder){
        Query query = getmEventRef().child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
                    dataBaseCallBacks = callBacks;
                    dataBaseCallBacks.getEvent(event, holder);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getGroup(String id, DataBaseCallBacks callBacks, @Nullable ViewHolder holder){
        Query query = getmGroupRef().child(id);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Group group = dataSnapshot.getValue(com.groupproject.Model.Group.class);
                    dataBaseCallBacks = callBacks;
                    dataBaseCallBacks.getGroup(group, holder);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //TODO fix this
    public void executeQuery(Query query, DataBaseCallBacks callBacks, SearchType.Type type){
        List<String> ids = new ArrayList<>();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataBaseCallBacks = callBacks;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ids.add(snapshot.getKey());
                    }
                }
                dataBaseCallBacks.executeQuery(ids, type);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getCurrentUserID(){
        return currentUser.getUid();
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

    public UserRelationship getRelationShip(User user){
        if(user.getId().equals(getCurrentUserID())) {
            return ME;
        }else if(user.friendsIDs.contains(getCurrentUserID())){
            return FRIENDS;
        }else if(user.requestsID.contains(getCurrentUserID())){
            return REQUESTED;
        }else{
            return NONE;
        }
    }

    public void sendFriendRequest(User user){
        getmUserRef().child(user.getId()).child("requestsID").child(getCurrentUserID()).setValue(true);//TODO COPY LINE
    }

    public void removeFriend(User user){
        getmUserRef().child(getCurrentUserID()).child("friendsIDs").child(user.getId()).removeValue();
        getmUserRef().child(user.getId()).child("friendsIDs").child(getCurrentUserID()).removeValue();
    }

    public void cancelRequest(User user){
        getmUserRef().child(user.getId()).child("requestsID").child(getCurrentUserID()).removeValue();
    }

    public void writeNewGroup(Group group) {
        group.setId(mGroupRef.push().getKey());
        mGroupRef.child(group.getId()).setValue(group);
    }

//    public void addGroupToUser(Group group) {
//        String key = mUserRef.child(getCurrentUserID()).child("joinedGroupIDs").push().getKey();
//        mUserRef.child(getCurrentUserID()).child("joinedGroupIDs").child(key).setValue(group.getId());
//    }


    public void acceptRequestUser (User user){
//        String key1 = getmUserRef().child(getCurrentUserID()).child("friendsIDs").push().getKey(); //Add user to current users friends
        getmUserRef().child(getCurrentUserID()).child("friendsIDs").child(user.getId()).setValue(true);
        getmUserRef().child(getCurrentUserID()).child("requestsID").child(user.getId()).removeValue();//Remove request

//        String key2 = getmUserRef().child(user.getId()).child("friendsIDs").push().getKey();
        getmUserRef().child(user.getId()).child("friendsIDs").child(getCurrentUserID()).setValue(true);//Add current user to users friends
    }

    public void acceptEventInvite (Event event){
//        String key1 = getmUserRef().child().child("goingIDs").push().getKey(); //Add user to current users friends
        getmEventRef().child(event.getId()).child("goingIDs").child(getCurrentUserID()).setValue(true);


//        String key2 = getmUserRef().child(getCurrentUserID()).child("goingEventsIDs").push().getKey();//Add current user to users friends
        getmUserRef().child(getCurrentUserID()).child("goingEventsIDs").child(event.getId()).setValue(true);
        getmUserRef().child(getCurrentUserID()).child("invitedEventsIDs").child(event.getId()).removeValue();//Remove request
    }


    public void acceptGroupInvite (Group group){
//        String key1 = getmUserRef().child(group.getId()).child("membersIDs").push().getKey(); //Add user to current users friends
        getmGroupRef().child(group.getId()).child("membersIDs").child(getCurrentUserID()).setValue(true);


//        String key2 = getmUserRef().child(getCurrentUserID()).child("joinedGroupIDs").push().getKey();//Add current user to users friends
        getmUserRef().child(getCurrentUserID()).child("joinedGroupIDs").child(group.getId()).setValue(true);
        getmUserRef().child(getCurrentUserID()).child("invitedGroupIDs").child(group.getId()).removeValue();//Remove request
    }


    //TODO Validate user
    public void writeNewUser(User user) {

        mUserRef.child(user.getId()).setValue(user);
    }


    public void writeNewEvent(Event event) {
        event.setId(mEventRef.push().getKey());
        mEventRef.child(event.getId()).setValue(event);
    }

    public void addEventToUser(Event event) {
        mUserRef.child(getCurrentUserID()).child("goingEventsIDs").child(event.getId()).setValue(true);
    }

    public void addGroupToUser(Group group) {
        mUserRef.child(getCurrentUserID()).child("joinedGroupIDs").child(group.getId()).setValue(true);
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


