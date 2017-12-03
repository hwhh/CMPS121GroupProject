package com.groupproject.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Group extends DataBaseItem{

    private List<String> membersIDs;//

    private List<String > eventsIDs;

    private List<String > tags;//

    private String name;
    private String description;

    private String nameLower;


    private Event.VISIBILITY visibility;

    public Group() {
        init();
    }

    public Group(String name, String description) {
        this.name = name;
        this.nameLower = name.toLowerCase();
        this.description = description;
        init();
    }

    private void init(){
        membersIDs = new ArrayList<>();
        eventsIDs= new ArrayList<>();
        tags= new ArrayList<>();
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setMembersIDs(Map<String, Object> map) {
        membersIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getMembersIDs() {
        return membersIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setEventsIDs(Map<String, Object> map) {
        eventsIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getEventsIDs() {
        return eventsIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
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

    public Event.VISIBILITY getVisibility() {
        return visibility;
    }
}
