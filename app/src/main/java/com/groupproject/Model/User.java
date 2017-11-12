package com.groupproject.Model;

import com.groupproject.DataManager.DataManager;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Reference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Indexes({
        @Index(value = "location", fields = @Field("location"))
})

public class User extends DataManager {

    @Id
    private ObjectId id;

    @Reference
    private List<User> connection;
    @Reference
    private List<Activity> activities;
    @Reference
    private List<Event> goingEvents;
    @Reference
    private List<Event> interestedEvents;//TODO If interested cant be going and vice versa
    @Reference
    private Location location;

    private String userName;
    private String password;


    public User() {
        super();
        init();
    }

    public User(String userName, String password) {
        super();
        this.userName = userName;
        this.password = password;
        init();
    }

    private void init(){
        connection = new ArrayList<>();
        activities= new ArrayList<>();
        goingEvents= new ArrayList<>();
        interestedEvents= new ArrayList<>();
    }

    public void save(){
        persist(this);
    }


    public void setLocation(Location location) {
        this.location = location;
    }

    public List<User> getConnection() {
        return connection;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public List<Event> getGoingEvents() {
        return goingEvents;
    }

    public List<Event> getInterestedEvents() {
        return interestedEvents;
    }
}
