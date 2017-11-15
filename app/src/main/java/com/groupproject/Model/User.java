package com.groupproject.Model;


import java.util.ArrayList;
import java.util.List;

public class User  {


    private List<User> connection;

    private List<EventActivity> activities;
    private List<Event> goingEvents;
    private List<Event> interestedEvents;//TODO If interested cant be going and vice versa

    private Location location;

    private String userName;
    private String password;


    public User() {
        init();
    }

    public User(String userName) {
        this.userName = userName;
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
}
