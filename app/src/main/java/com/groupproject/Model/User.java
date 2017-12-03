package com.groupproject.Model;


import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseUser;
import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class User extends DataBaseItem{

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    enum EVENT_TYPE {
        INTERESTED,
        GOING,
    }

    public List<String> friendsIDs;
    public List<String> requestsID;
    public List<String> goingEventsIDs;


    public List<String> joinedGroupIDs;
    public List<String> requestsGroupIDs;


//    private List<String> interestedEventsIDs;//TODO If interested cant be going and vice versa

    private String nameLower;
    private CustomLocation location;
    private String name;
    private String email;
    private String id;


    public User() {
        init();
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
        friendsIDs= new ArrayList<>();
        requestsID= new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setGoingEventsIDs(Map<String, Object> map) {
        goingEventsIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getGoingEventsIDs() {
        return goingEventsIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setFriendsIDs(Map<String, Object> map) {
        friendsIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getFriendsIDs() {
        return friendsIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setRequestsID(Map<String, Object> map) {
        requestsID = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getRequestsID() {
        return requestsID.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setJoinedGroupIDs(Map<String, Object> map) {
        joinedGroupIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getJoinedGroupIDs() {
        return joinedGroupIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setRequestsGroupIDs(Map<String, Object> map) {
        requestsGroupIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getRequestsGroupIDs() {
        return requestsGroupIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
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

    public String getId() {return id;}

    public void setId(String id) {
        this.id = id;
    }

    public String getLowerCaseName() {
        return nameLower;
    }

    public void setLowerCaseName(String nameLower) {
        this.nameLower = nameLower;
    }
}



























