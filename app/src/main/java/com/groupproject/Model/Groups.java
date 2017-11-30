package com.groupproject.Model;


import java.util.ArrayList;
import java.util.List;



public class gitGroups {

    private List<Event> eventList;
    private List<String> eventListIDs; //TODO ONLY SAVE THESE VALUES FOR FLATNESS
    private List<User> users;
    private List<String> usersIDs; //TODO ONLY SAVE THESE VALUES FOR FLATNESS

    private String name;
    private String description;
    private String tags;

    public Groups() {

    }

    public Groups(String name, String description) {
        this.name = name;
        this.description = description;
        init();
    }

    private void init(){
        eventList = new ArrayList<>();
        users= new ArrayList<>();
    }

    public List<String> getEventListIDs() {
        return eventListIDs;
    }

    public List<String> getUsersIDs() {
        return usersIDs;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }



}
