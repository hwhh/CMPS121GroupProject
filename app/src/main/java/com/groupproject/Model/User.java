package com.groupproject.Model;


import com.google.firebase.auth.FirebaseUser;
import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User extends DataBaseItem{

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    enum EVENT_TYPE {
        INTERESTED,
        GOING,
    }

    private List<String> friendsIDs;
    private List<String> requestsID;
    private List<String> goingEventsIDs;
    private List<String> interestedEventsIDs;//TODO If interested cant be going and vice versa

    private String nameLower;
    private CustomLocation location;
    private String name;
    private String email;
    private String id;


    public User() {
    }

    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.nameLower = name.toLowerCase();
        this.email = email;
        init();
        dataBaseAPI.writeNewUser(this);
    }

    private void init(){
        goingEventsIDs= new ArrayList<>();
        interestedEventsIDs= new ArrayList<>();
        friendsIDs= new ArrayList<>();
        requestsID= new ArrayList<>();
    }

    public void addEvent(FirebaseUser user, Event e){
        goingEventsIDs.add(e.getId());
        dataBaseAPI.addEventToUser(user, e);

    }

    public void setGoingEventsIDs(Map<String, Object> map) {
        if(goingEventsIDs == null)
            goingEventsIDs = new ArrayList<>();
        for (Object o : map.values()) {
            goingEventsIDs.add(o.toString());
        }
    }

    public Map<String, Object> getGoingEventsIDs() {
        if(goingEventsIDs != null) {
            Map<String, Object> map = new HashMap<>();
            for (String i : goingEventsIDs)
                map.put(i, i);
            return map;
        }else{
            return new HashMap<>();
        }
    }

    public List<String> getFriendsIDs() {
        return friendsIDs;
    }

    public List<String> getRequestsID() {
        return requestsID;
    }

    public List<String> getInterestedEventsIDs() {
        return interestedEventsIDs;
    }


    public CustomLocation getLocation() {
        return location;
    }

    public void setLocation(CustomLocation location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLowerCaseName() {
        return nameLower;
    }
}


























