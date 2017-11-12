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
        @Index(value = "location", fields = @Field("location")),
        @Index(value = "visibility", fields = @Field("visibility")),
        @Index(value = "going", fields = @Field("going")),
        @Index(value = "interested", fields = @Field("interested")),
        @Index(value = "relatedActivities", fields = @Field("relatedActivities")),
})

public class Event extends DataManager {

    enum VISIBILITY {
        INVITE_ONLY,
        FRIENDS,
        PUBLIC
    }

    @Id
    private ObjectId id;
    @Reference
    private List<User> going;
    @Reference
    private List<User> interested;
    @Reference
    private List<EventActivity> relatedActivities;

    private Location location;

    private VISIBILITY visibility;

    private String name;
    private String description;


    public Event() {
        super();
        init();
    }

    public Event(String name, String description) {
        super();
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
        persist(this);
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
