package com.groupproject.DataBaseAPI;

import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;

public interface DataBaseCallBacks {

    User getUser(String id);

    Event getEvent(String id);

    Group getGroup(String id);

}
