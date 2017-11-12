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
        @Index(value = "eventList", fields = @Field("eventList")),
        @Index(value = "users", fields = @Field("users")),
})

public class EventActivity extends DataManager {

    @Id
    private ObjectId id;
    @Reference
    private List<Event> eventList;
    @Reference
    private List<User> users;

    private String name;
    private String description;

    public EventActivity() {
        super();
        init();
    }

    public EventActivity(String name, String description) {
        super();
        this.name = name;
        this.description = description;
        init();
    }

    private void init(){
        eventList = new ArrayList<>();
        users= new ArrayList<>();
    }


    public void save(){
        persist(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public List<User> getUsers() {
        return users;
    }


}
