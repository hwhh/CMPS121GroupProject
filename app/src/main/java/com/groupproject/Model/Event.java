package com.groupproject.Model;



import java.util.ArrayList;
import java.util.List;



public class Event {

    enum VISIBILITY {
        INVITE_ONLY,
        FRIENDS,
        PUBLIC
    }



    private List<User> going;
    private List<User> interested;
    private List<EventActivity> relatedActivities;

    private Location location;

    private VISIBILITY visibility;

    private String name;
    private String description;


    public Event() {
        init();
    }

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
        init();
    }


    private void init(){
        going = new ArrayList<>();
        interested= new ArrayList<>();
        relatedActivities= new ArrayList<>();
    }

    public void save(){

    }

    public void setVisibility(VISIBILITY visibility) {
        this.visibility = visibility;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public VISIBILITY getVisibility() {
        return visibility;
    }

    public List<User> getGoing() {
        return going;
    }

    public List<User> getInterested() {
        return interested;
    }

    public List<EventActivity> getRelatedActivities() {
        return relatedActivities;
    }
}
