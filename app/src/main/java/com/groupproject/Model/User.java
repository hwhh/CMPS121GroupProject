package com.groupproject.Model;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

public class User {

    private static final String PATH = "/user/";
    private FirebaseFirestore db;
    private DatabaseReference ref;




    enum EVENT_TYPE {
        INTERESTED,
        GOING,
    }

    private DataBaseAPI api = DataBaseAPI.DataBaseAPI();

    private List<User> connection;

    private List<EventActivity> activities;
    private List<Event> goingEvents;
    private List<Event> interestedEvents;//TODO If interested cant be going and vice versa

    private Location location;

    private String name;
    private String email;
    private String id;


    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        init();
    }



    private void init(){
        connection = new ArrayList<>();
        activities= new ArrayList<>();
        goingEvents= new ArrayList<>();
        interestedEvents= new ArrayList<>();
    }


    //Path for events -> /users/{user.id}/interestedEvents

    public void addEvent(Event event, EVENT_TYPE event_type){
        if(event_type == EVENT_TYPE.INTERESTED) {
            interestedEvents.add(event);
            event.getInterested().add(this);
        }else {
            goingEvents.add(event);
            if(event.getInterested().contains(this)){
                event.getInterested().remove(this);
            }
            event.getGoing().add(this);
        }

        String key = ref.child("users").push().getKey();

        Map<String, Object> eventMap = event.toMap();

        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("/users/" + id + "/interestedEvents", eventMap);
        childUpdates.put("/events/" + event.getId() + "/" + key, eventMap);

        ref.updateChildren(childUpdates);



    }


    public void setLocation(Location location) {
        this.location = location;
    }

    public List<User> getConnection() {
        return connection;
    }

    public List<EventActivity> getActivities() {
        return activities;
    }

    public List<Event> getGoingEvents() {
        return goingEvents;
    }

    public List<Event> getInterestedEvents() {
        return interestedEvents;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }
}
