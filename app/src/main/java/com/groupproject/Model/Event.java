package com.groupproject.Model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Event {

    public Map<String, Object> toMap() {
        Map<String, Object> eventMap = new HashMap<>();
        eventMap.put("date", eventDate);
//        eventMap.put("going", going);
//        eventMap.put("interested", interested);
//        eventMap.put("relatedActivities", relatedActivities);
        eventMap.put("location", location);
        eventMap.put("visibility", visibility);
        eventMap.put("name", name);
        eventMap.put("description", description);
        eventMap.put("id", id);
        return eventMap;
    }

    enum VISIBILITY {
        INVITE_ONLY,
        FRIENDS,
        PUBLIC
    }


    private Date eventDate;

//    private List<User> going;
//    private List<User> interested;
//    private List<EventActivity> relatedActivities;

    private Location location;

    private VISIBILITY visibility;

    private String name;
    private String description;
    private String id;



    public Event() {
//        init();
    }

    public Event(Date eventDate, Location location, VISIBILITY visibility, String name, String description, String id) {
        this.eventDate = eventDate;
        this.location = location;
        this.visibility = visibility;
        this.name = name;
        this.description = description;
        this.id = id;
//        init();
    }

//    private void init(){
//        going = new ArrayList<>();
//        interested= new ArrayList<>();
//        relatedActivities= new ArrayList<>();
//    }

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

    public Date getEventDate() {
        return eventDate;
    }

//    public List<User> getGoing() {
//        return going;
//    }
//
//    public List<User> getInterested() {
//        return interested;
//    }
//
//    public List<EventActivity> getRelatedActivities() {
//        return relatedActivities;
//    }

    public Location getLocation() {
        return location;
    }

    public VISIBILITY getVisibility() {
        return visibility;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }
}
