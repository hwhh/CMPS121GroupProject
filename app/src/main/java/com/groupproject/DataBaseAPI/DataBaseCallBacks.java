package com.groupproject.DataBaseAPI;

import com.groupproject.Controller.SearchActivities.SearchAdapter;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;

import java.util.List;

public interface DataBaseCallBacks<T> {

    void getUser(User user, ViewHolder holder);

    void getEvent(Event event, ViewHolder holder);

    void getGroup(Group group, ViewHolder holder);

    void executeQuery(List<T> result, SearchType.Type type);

}
