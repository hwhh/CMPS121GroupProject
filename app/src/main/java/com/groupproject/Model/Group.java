package com.groupproject.Model;

import java.util.ArrayList;
import java.util.List;


public class Group extends DataBaseItem{

    private List<String> memebrsIDs;

    private List<String > eventsIDs;

    private List<String > tags;

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
        memebrsIDs = new ArrayList<>();
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
