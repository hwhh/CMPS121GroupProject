package com.groupproject.Model;

import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Event extends DataBaseItem {



    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();


    public enum VISIBILITY {
        INVITE_ONLY,
        PUBLIC
    }

    private String hostID;

    private Date startDate;
    private Date endDate;

    private List<User> going;//TODO remove
    private List<User> interested;//TODO remove

    private List<String> relatedActivities;

    private List<String> goingIDs;
    private List<String> interestedIDs;//TODO implement
    private List<String> relatedActivitiesIDs;

    private List<String> tags;

    private CustomLocation customLocation;

    private VISIBILITY visibility;

    private String name;
    private String description;
    private String id;

    private boolean expired;

    public Event() {

    }

    public Event(Date startDate, Date endDate, CustomLocation customLocation, VISIBILITY visibility, String name, String description, String hostID) {

        this.startDate = startDate;
        this.endDate = endDate;
        this.customLocation = customLocation;
        this.visibility = visibility;
        this.name = name;
        this.description = description;
        this.hostID = hostID;
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

    public long calculateTimeRemaining(){
        return endDate.getTime() - System.currentTimeMillis();
    }


    public List<String> getTags() {
        return tags;
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

    public CustomLocation getCustomLocation() {
        return customLocation;
    }

    public void setCustomLocation(CustomLocation customLocation) {
        this.customLocation = customLocation;
    }

    public VISIBILITY getVisibility() {
        return visibility;
    }

    public void setVisibility(VISIBILITY visibility) {
        this.visibility = visibility;
    }

    @Override
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


