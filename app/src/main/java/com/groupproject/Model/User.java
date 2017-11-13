package com.groupproject.Model;


import java.util.ArrayList;
import java.util.List;

public class User  {


    private List<User> connection;

    private List<EventActivity> activities;
    private List<Event> goingEvents;
    private List<Event> interestedEvents;//TODO If interested cant be going and vice versa

    private Location location;

    private String name;
    private String email;
    private String id;


    public User() {
        init();
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        init();
    }

    public User(String name) {
        this.name = name;
        init();
    }

    private void init(){
        connection = new ArrayList<>();
        activities= new ArrayList<>();
        goingEvents= new ArrayList<>();
        interestedEvents= new ArrayList<>();
    }

    public void save(){

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
