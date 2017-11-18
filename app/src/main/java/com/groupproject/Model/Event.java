package com.groupproject.Model;

import com.groupproject.DataBaseAPI.DataBaseAPI;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Event {


    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();


    public enum VISIBILITY {
        INVITE_ONLY,
        FRIENDS,
        PUBLIC
    }


    private Date startDate;
    private Date endDate;

    private List<User> going;
    private List<User> interested;

    private List<String> relatedActivities;

    private List<String> goingIDs;
    private List<String> interestedIDs;
    private List<String> relatedActivitiesIDs;

    private Location location;

    private VISIBILITY visibility;

    private String name;
    private String description;
    private String id;

    private boolean expired;


    public Event() {

    }

    public Event(Date startDate, Date endDate, Location location, VISIBILITY visibility, String name, String description) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.visibility = visibility;
        this.name = name;
        this.description = description;
        init();
        dataBaseAPI.writeNewEvent(this);
    }

    private void init(){
        going = new ArrayList<>();
        interested= new ArrayList<>();
        relatedActivities= new ArrayList<>();
        goingIDs= new ArrayList<>();
        relatedActivitiesIDs= new ArrayList<>();
        expired = checkExpired();
    }

    public boolean checkExpired(){
        return (endDate.getTime() - System.currentTimeMillis()) < 0 ;
    }

    public long getTimeRemaining(){
        return endDate.getTime() - System.currentTimeMillis();
    }


    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<String> getRelatedActivities() {
        return relatedActivities;
    }

    public List<String> getGoingIDs() {
        return goingIDs;
    }

    public List<String> getInterestedIDs() {
        return interestedIDs;
    }

    public List<String> getRelatedActivitiesIDs() {
        return relatedActivitiesIDs;
    }

    public void setRelatedActivitiesIDs(List<String> relatedActivitiesIDs) {
        this.relatedActivitiesIDs = relatedActivitiesIDs;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public VISIBILITY getVisibility() {
        return visibility;
    }

    public void setVisibility(VISIBILITY visibility) {
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}


