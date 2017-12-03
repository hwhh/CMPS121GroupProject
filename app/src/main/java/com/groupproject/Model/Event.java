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


    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();




    private String hostID;

    private String nameLower;

    private Date startDate;
    private Date endDate;

    private List<String> goingIDs;

    private List<String> tags;

    private CustomLocation customLocation;

    private Visability.VISIBILITY visibility;

    private String name;
    private String description;
    private String id;

    private boolean expired;


    public Event() {
        init();
    }

    public Event(Date startDate, Date endDate, CustomLocation customLocation, Visability.VISIBILITY visibility, String name, String description, String hostID) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.customLocation = customLocation;
        this.visibility = visibility;
        this.name = name;
        this.nameLower = name.toLowerCase();
        this.description = description;
        this.hostID = hostID;
        this.goingIDs= new ArrayList<>();
        goingIDs.add(hostID);
        init();
        dataBaseAPI.writeNewEvent(this);
    }

    private void init(){
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


    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    @Override
    public String getNameLower() {
        return nameLower;
    }

    public void setNameLower(String nameLower) {
        this.nameLower = nameLower;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public CustomLocation getCustomLocation() {
        return customLocation;
    }

    public void setCustomLocation(CustomLocation customLocation) {
        this.customLocation = customLocation;
    }

    public Visability.VISIBILITY getVisibility() {
        return visibility;
    }

    public void setVisibility(Visability.VISIBILITY visibility) {
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

    @Override
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


