package com.groupproject.Controller;

import android.app.Activity;
import android.os.Bundle;

import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.List;

/**
 * Created by haileypun on 03/12/2017.
 */

public class ProfileActivity extends Activity implements DataBaseCallBacks<User> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        dataBaseAPI.getUser(dataBaseAPI.getCurrentUserID(), this);

    }

    @Override
    public void getUser(User user) {
        user.getName();
        user.getEmail();
        user.getLocation();
        user.get
    }

    @Override
    public void getEvent(Event event) {

    }

    @Override
    public void getGroup(Group g) {

    }

    @Override
    public void executeQuery(List<User> result) {

    }

    @Override
    public void createUserList(List<User> userList) {

    }
}
