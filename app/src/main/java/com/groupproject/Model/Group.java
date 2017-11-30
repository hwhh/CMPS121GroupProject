package com.groupproject.Model;

import java.util.ArrayList;
import java.util.List;


public class Group extends DataBaseItem{

    private List<User> memebrs;
    private List<String> memebrsIDs;

    private List<Event> events;
    private List<String > eventsIDs;

    private List<String > tags;

    private String name;
    private String description;

    private Event.VISIBILITY visibility;

    public Group() {

    }

    public Group(String name, String description) {

        this.name = name;
        this.description = description;
        init();
    }

    private void init(){
        memebrs = new ArrayList<>();
        memebrsIDs = new ArrayList<>();
        events= new ArrayList<>();
        eventsIDs= new ArrayList<>();
        tags= new ArrayList<>();
    }



    public List<String> getTags() {
        return tags;
    }

    public List<String> getMemebrsIDs() {
        return memebrsIDs;
    }

    public List<String> getEventsIDs() {
        return eventsIDs;
    }

    @Override
    public String getName() {
        return name;
    }


    public String getDescription() {
        return description;
    }

    public Event.VISIBILITY getVisibility() {
        return visibility;
    }
}
