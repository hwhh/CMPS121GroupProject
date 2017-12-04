package com.groupproject.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.firebase.auth.FirebaseAuth;
import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Group extends DataBaseItem{

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();


    private String host;
    private List<String> membersIDs;//
//    private List<String> requestedMembersIDs;//

    private List<String > eventsIDs;

//    private List<String > tags;//

    private String name;
    private String description;

    private String nameLower;

    private String id;


    private Visability.VISIBILITY visibility;

    public Group() {
        init();
    }


    public Group(String name, String description, Visability.VISIBILITY visibility, String host) {
        this.name = name;
        this.description = description;
        this.nameLower = name.toLowerCase();
        this.visibility = visibility;
        membersIDs = new ArrayList<>();
        membersIDs.add(host);
        this.host = host;
        init();
        dataBaseAPI.writeNewGroup(this);
    }

    public Group(String name, String description) {
        this.name = name;
        this.nameLower = name.toLowerCase();
        this.description = description;
    }

    private void init(){
        eventsIDs = new ArrayList<>();
        membersIDs = new ArrayList<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMembersIDs(Map<String, Object> map) {
        membersIDs = map.keySet().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getMembersIDs() {
        return membersIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventsIDs(Map<String, Object> map) {
        eventsIDs = map.keySet().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getEventsIDs() {
        return eventsIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }


//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public void setRequestedMembersIDs(Map<String, Object> map) {
//        requestedMembersIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public Map<String, Object> getRequestedMembersIDs() {
//        return requestedMembersIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
//    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getNameLower() {
        return nameLower;
    }

    public void setNameLower(String nameLower) {
        this.nameLower = nameLower;
    }

    public void setVisibility(Visability.VISIBILITY visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public String getLowerCaseName() {
        return nameLower;
    }

    public String getDescription() {
        return description;
    }

    public Visability.VISIBILITY getVisibility() {
        return visibility;
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}


