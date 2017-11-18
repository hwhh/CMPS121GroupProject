package com.groupproject.DataBaseAPI;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.groupproject.Model.Event;

import net.jodah.expiringmap.ExpirationListener;
import net.jodah.expiringmap.ExpiringMap;

import java.util.Observable;
import java.util.concurrent.TimeUnit;


public class EventUpdater extends Observable implements ChildEventListener {

    private static ExpiringMap<String, Event> eventMap;

    public EventUpdater() {
        eventMap = ExpiringMap.builder().variableExpiration().build();
        eventMap.addExpirationListener(new ExpirationListener<String, Event>() {
            @Override
            public void expired(String key, Event value) {
                eventMap.remove(value.getId());
                setChanged();
                notifyObservers(this);
            }
        });
        for (Event event : DataBaseAPI.getActiveEvents().values()) {
            eventMap.put(event.getId(), event);
            eventMap.setExpiration(event.getId(), event.getTimeRemaining(),TimeUnit.MILLISECONDS);
        }
        setChanged();
        notifyObservers(this);
    }


    public static ExpiringMap<String, Event> getEventMap() {
        return eventMap;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
        if (event != null) {
            eventMap.put(event.getId(), event);
            eventMap.setExpiration(event.getId(), event.getTimeRemaining(),TimeUnit.MILLISECONDS);
            setChanged();
            notifyObservers(this);
        }
    }


    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
        if (event != null) {
            eventMap.put(event.getId(), event);
            eventMap.setExpiration(event.getId(), event.getTimeRemaining(),TimeUnit.MILLISECONDS);
            setChanged();
            notifyObservers(this);
        }
    }

    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
        if (event != null) {
            eventMap.remove(event.getId());
            setChanged();
            notifyObservers(this);
        }
    }

    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
