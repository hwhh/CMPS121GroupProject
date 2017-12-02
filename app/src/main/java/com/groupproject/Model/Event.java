package com.groupproject.Model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.groupproject.DataBaseAPI.DataBaseAPI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;


public class Event extends DataBaseItem {


    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();


    public enum VISIBILITY {
        INVITE_ONLY,
        PUBLIC
    }

    private String hostID;

    private String nameLower;

    private Date startDate;
    private Date endDate;

    private List<String> goingIDs;

    private List<String> tags;

    private CustomLocation customLocation;

    private VISIBILITY visibility;

    private String name;
    private String description;
    private String id;

    private boolean expired;


    public Event() {
        init();
    }

    public Event(Date startDate, Date endDate, CustomLocation customLocation, VISIBILITY visibility, String name, String description, String hostID) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customLocation = customLocation;
        this.visibility = visibility;
        this.name = name;
        this.nameLower = name.toLowerCase();
        this.description = description;
        this.hostID = hostID;
        init();
        dataBaseAPI.writeNewEvent(this);
    }

    private void init(){
        goingIDs= new ArrayList<>();
        if(endDate != null)
            expired = checkExpired();
    }


    public boolean checkExpired(){
        return (endDate.getTime() - System.currentTimeMillis()) < 0 ;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setGoingIDs(Map<String, Object> map) {
        goingIDs = map.values().stream().map(Object::toString).collect (Collectors.toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Map<String, Object> getGoingIDs() {
        return goingIDs.stream().collect(Collectors.toMap(Function.identity(), id -> true));
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

    public CustomLocation getCustomLocation() {
        return customLocation;
    }

    public void setCustomLocation(CustomLocation customLocation) {this.customLocation = customLocation;}

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

    public String getLowerCaseName() {
        return nameLower;
    }
}


