package com.groupproject.Model;


import java.util.ArrayList;
import java.util.List;



public class EventActivity  {


//    private List<Event> eventList;
//    private List<User> users;

    private String name;
    private String description;

    public EventActivity() {

    }

    public EventActivity(String name, String description) {
        this.name = name;
        this.description = description;
//        init();
    }

//    private void init(){
//        eventList = new ArrayList<>();
//        users= new ArrayList<>();
//    }


    public void save(){

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

//    public List<Event> getEventList() {
//        return eventList;
//    }
//
//    public List<User> getUsers() {
//        return users;
//    }


}
