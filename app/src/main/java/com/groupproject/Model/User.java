package com.groupproject.Model;


import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.List;

public class User {

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    enum EVENT_TYPE {
        INTERESTED,
        GOING,
    }

//    private List<User> connection;
//    private List<Groups> activities;
    private List<Event> goingEvents;
    private List<String> goingEventsIDs;

    private List<Event> interestedEvents;//TODO If interested cant be going and vice versa
    private List<String> interestedEventsIDs;//TODO If interested cant be going and vice versa


    private Location location;
    private String name;
    private String email;
    private String id;


    public User() {
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        init();
        dataBaseAPI.writeNewUser(this);
    }



    private void init(){
//        connection = new ArrayList<>();
//        activities= new ArrayList<>();
        goingEvents= new ArrayList<>();
        interestedEvents= new ArrayList<>();
    }

    public void addEvent(Event e){
        goingEvents.add(e);
        goingEventsIDs.add(e.getId());
        dataBaseAPI.addEventToUser(this, e);

    }


    public void setLocation(Location location) {
        this.location = location;
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


    public List<String> getGoingEventsIDs() {
        return goingEventsIDs;
    }
}


























//Path for events -> /users/{user.id}/interestedEvents
//TODO Make an interface to extract db calls
//    public void addEvent(Event event, EVENT_TYPE event_type){
//        if(event_type == EVENT_TYPE.INTERESTED) {
//            interestedEvents.add(event);
//            event.getInterested().add(this);
//        }else {
//            goingEvents.add(event);
//            if(event.getInterested().contains(this)){
//                event.getInterested().remove(this);
//            }
//            event.getGoing().add(this);
//        }



//        String key = ref.child("users").push().getKey();
//        Map<String, Object> eventMap = event.toMap();
//        Map<String, Object> childUpdates = new HashMap<>();
//        childUpdates.put("/users/" + id + "/interestedEvents", eventMap);
//        childUpdates.put("/events/" + event.getId() + "/" + key, eventMap);
//
//        ref.updateChildren(childUpdates);



//    }
