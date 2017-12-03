package com.groupproject.DataBaseAPI;

import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;

import java.util.List;

public interface DataBaseCallBacks<T> {

    void getUser(User user);

    void getEvent(Event event);

    void getGroup(Group g);

    void executeQuery(T result);

    void createUserList(List<User> userList);

}
