package com.groupproject.Model.DataBaseAPI;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.Controller.LoginActivities.LoginActivity;
import com.groupproject.Controller.NotificationCallBack;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.PinnedNotification;
import com.groupproject.Model.User;
import com.groupproject.Model.Visibility;

import net.jodah.expiringmap.ExpiringMap;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.groupproject.Model.DataBaseAPI.DataBaseAPI.UserRelationship.ME;
import static com.groupproject.Model.DataBaseAPI.DataBaseAPI.UserRelationship.FRIENDS;
import static com.groupproject.Model.DataBaseAPI.DataBaseAPI.UserRelationship.REQUESTED;
import static com.groupproject.Model.DataBaseAPI.DataBaseAPI.UserRelationship.NONE;

public class DataBaseAPI {

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

    public enum STATUS {
        HOST,
        JOINED,
        INVITED,
        PUBLIC,
        HIDDEN
    }

    private DataBaseAPI(){
        mUserRef = FirebaseDatabase.getInstance().getReference("users");
        mEventRef = FirebaseDatabase.getInstance().getReference("events");
        mGroupRef = FirebaseDatabase.getInstance().getReference("groups");
        eventMap = ExpiringMap.builder().variableExpiration().build();
        eventMap.addExpirationListener((key, e) -> {
            e.setExpired(true);
            e.setExpired_vis_nameLower(true+"_"+e.getVisibility()+"_"+e.getNameLower());
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
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        }else if(false){ //TODO Check if the user requested you
            return REQUESTED;
        }else{
            return NONE;
        }
    }

    public STATUS getEventRelationShip(Event event){
        if(Objects.equals(event.getHostID(), getCurrentUserID()))
            return STATUS.HOST;
        else if (event.goingIDs.contains(getCurrentUserID()))
            return STATUS.JOINED;
        else if (event.invitedIDs.contains(getCurrentUserID()))
            return STATUS.INVITED;
        else if (event.getVisibility() == Visibility.VISIBILITY.PUBLIC)
            return STATUS.PUBLIC;
        else
            return STATUS.HIDDEN;
    }

    public STATUS getGroupRelationShip(Group group){
        if(Objects.equals(group.getHost(), getCurrentUserID()))
            return STATUS.HOST;
        else if (group.membersIDs.contains(getCurrentUserID()))
            return STATUS.JOINED;
        else if (group.invitedIDs.contains(getCurrentUserID()))
            return STATUS.INVITED;
        else if (group.getVisibility() == Visibility.VISIBILITY.PUBLIC)
            return STATUS.PUBLIC;
        else
            return STATUS.HIDDEN;
    }


    //TODO ON USER PROFILE INVITE TO EVENTS OR GROUPS ***
    public void sendFriendRequest(User user){
        getmUserRef().child(user.getId()).child("requestsID").child(getCurrentUserID()).setValue(true);
        getmUserRef().child(user.getId()).child("unSeenNotifications").child(getCurrentUserID()).setValue("user");
    }

    public void sendEventInvite(String userID, String eventID){
        getmEventRef().child(eventID).child("invitedIDs").child(userID).setValue(true);
        getmUserRef().child(userID).child("invitedEventsIDs").child(eventID).setValue(true);
        getmUserRef().child(userID).child("unSeenNotifications").child(eventID).setValue("event");
    }

    public void sendGroupInvite(String userID, String groupID){
        getmGroupRef().child(groupID).child("invitedIDs").child(userID).setValue(true);
        getmUserRef().child(userID).child("invitedGroupIDs").child(groupID).setValue(true);
        getmUserRef().child(userID).child("unSeenNotifications").child(groupID).setValue("group");
    }


    public void removeFriend(User user){
        getmUserRef().child(getCurrentUserID()).child("friendsIDs").child(user.getId()).removeValue();
        getmUserRef().child(user.getId()).child("friendsIDs").child(getCurrentUserID()).removeValue();
    }

    public void leaveEvent(Event event){
        getmEventRef().child(event.getId()).child("goingIDs").child(getCurrentUserID()).removeValue();
        getmUserRef().child(getCurrentUserID()).child("goingEventsIDs").child(event.getId()).removeValue();
    }

    public void leaveGroup(Group group){
        getmGroupRef().child(group.getId()).child("membersIDs").child(getCurrentUserID()).removeValue();
        getmUserRef().child(getCurrentUserID()).child("joinedGroupIDs").child(group.getId()).removeValue();
    }

    public void setNotificationAsSeen(String notificationID, SearchType.Type type){
        getmUserRef().child(getCurrentUserID()).child("unSeenNotifications").child(notificationID).removeValue();
        if(type == SearchType.Type.USERS){
            getmUserRef().child(getCurrentUserID()).child("requestsID").child(notificationID).setValue(false);
        } else if(type == SearchType.Type.EVENTS){
            getmUserRef().child(getCurrentUserID()).child("invitedEventsIDs").child(notificationID).setValue(false);
        }else if(type == SearchType.Type.GROUPS){
            getmUserRef().child(getCurrentUserID()).child("invitedGroupIDs").child(notificationID).setValue(false);
        }
    }

    public void getNotifications(NotificationCallBack notificationCallBack){
        List<PinnedNotification> notifications = new ArrayList<>();
        Query query = getmUserRef().child(getCurrentUserID()).child("unSeenNotifications");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String id = snapshot.getKey();
                        String type = snapshot.getValue(String.class);
                        notifications.add(new PinnedNotification(id, type));
                    }
                    notificationCallBack.getNotifications(notifications);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteEvent(Event event){
        List<String> goingIDs = new ArrayList<>();
        List<String> invitedIDs = new ArrayList<>();
        Query query1 = getmEventRef().child(event.getId()).child("goingIDs");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        goingIDs.add(snapshot.getKey());
                    }
                    cascadeDelete(goingIDs, "goingEventsIDs", event.getId());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Query query2 = getmEventRef().child(event.getId()).child("invitedIDs");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        invitedIDs.add(snapshot.getKey());
                    }
                    cascadeDelete(invitedIDs, "invitedEventsIDs", event.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        getmEventRef().child(event.getId()).removeValue();
    }

    public void deleteGroup(Group group){
        List<String> goingIDs = new ArrayList<>();
        List<String> invitedIDs = new ArrayList<>();
        Query query1 = getmGroupRef().child(group.getId()).child("membersIDs");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        goingIDs.add(snapshot.getKey());
                    }
                    cascadeDelete(goingIDs, "joinedGroupIDs", group.getId());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        Query query2 = getmGroupRef().child(group.getId()).child("invitedIDs");
        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        invitedIDs.add(snapshot.getKey());
                    }
                    cascadeDelete(invitedIDs, "invitedGroupIDs", group.getId());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    public void cancelFriendRequest(User user){
        getmUserRef().child(user.getId()).child("requestsID").child(getCurrentUserID()).removeValue();
        getmUserRef().child(user.getId()).child("unSeenNotifications").child(getCurrentUserID()).removeValue();

    }

    public void cancelEventInvite(String userID, String eventID){
        getmUserRef().child(userID).child("invitedEventsIDs").child(eventID).removeValue();
        getmUserRef().child(eventID).child("invitedIDs").child(userID).removeValue();
    }

    public void cancelGroupInvite(String userID, String groupID){
        getmUserRef().child(userID).child("invitedGroupIDs").child(groupID).removeValue();
        getmUserRef().child(groupID).child("invitedIDs").child(userID).removeValue();
    }

    public void acceptRequestUser (User user){
        getmUserRef().child(getCurrentUserID()).child("friendsIDs").child(user.getId()).setValue(true);
        getmUserRef().child(getCurrentUserID()).child("requestsID").child(user.getId()).removeValue();//Remove request

        getmUserRef().child(user.getId()).child("friendsIDs").child(getCurrentUserID()).setValue(true);//Add current user to users friends
    }

    public void acceptEventInvite (Event event){
        getmEventRef().child(event.getId()).child("goingIDs").child(getCurrentUserID()).setValue(true);
        getmEventRef().child(event.getId()).child("invitedIDs").child(getCurrentUserID()).removeValue();

        getmUserRef().child(getCurrentUserID()).child("goingEventsIDs").child(event.getId()).setValue(true);
        getmUserRef().child(getCurrentUserID()).child("invitedEventsIDs").child(event.getId()).removeValue();//Remove request
    }

    public void acceptGroupInvite (Group group){
        getmGroupRef().child(group.getId()).child("membersIDs").child(getCurrentUserID()).setValue(true);
        getmEventRef().child(group.getId()).child("invitedIDs").child(getCurrentUserID()).removeValue();
        getmUserRef().child(getCurrentUserID()).child("joinedGroupIDs").child(group.getId()).setValue(true);
        getmUserRef().child(getCurrentUserID()).child("invitedGroupIDs").child(group.getId()).removeValue();//Remove request
    }

    public void writeNewUser(User user) {
        mUserRef.child(user.getId()).setValue(user);
    }

    public void writeNewEvent(Event event) {
        event.setId(mEventRef.push().getKey());
        mEventRef.child(event.getId()).setValue(event);
    }

    public void writeNewGroup(Group group) {
        group.setId(mGroupRef.push().getKey());
        mGroupRef.child(group.getId()).setValue(group);
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

    public static void removeExpiredEvents() {
        Query activeEvents = mEventRef.orderByChild("expired").equalTo(false);
        activeEvents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Event event = snapshot.getValue(com.groupproject.Model.Event.class);
                        if (event != null) {
                            if (event.getEndDate().before(new Date())){
                                event.setExpired(true);
                                event.setExpired_vis_nameLower(true+"_"+event.getVisibility()+"_"+event.getNameLower());
                                HashMap<String, Object> result = new HashMap<>();
                                result.put(event.getId(), event);
                                mEventRef.updateChildren(result);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void cascadeDelete(List<String> result, String child, String idToBeRemoved) {
        Query query;
        if(!result.isEmpty()) {
            for (String id : result) {
                query = getmUserRef().child(id).child(child).child(idToBeRemoved);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().removeValue();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
}


