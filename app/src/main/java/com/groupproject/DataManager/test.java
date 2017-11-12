package com.groupproject.DataManager;

import com.groupproject.Model.Activity;
import com.groupproject.Model.Event;
import com.groupproject.Model.User;


public class test {

    public static void main(String[] args) {
        User user1 = new User("1", "1");
        User user2 = new User("2", "2");
        User user3 = new User("3", "3");

        Activity activity1 = new Activity("1", "1");
        Activity activity2 = new Activity("2", "2");

        Event event1 = new Event("1", "1");
        Event event2 = new Event("2", "2");

        activity1.getUsers().add(user1);
        activity1.getUsers().add(user2);
        activity2.getUsers().add(user1);

        event1.getGoing().add(user1);
        event1.getInterested().add(user2);
        event2.getGoing().add(user3);

        user1.save();
        user2.save();
        user3.save();


    }

}
