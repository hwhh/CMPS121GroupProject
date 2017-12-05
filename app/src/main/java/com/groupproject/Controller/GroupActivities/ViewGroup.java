package com.groupproject.Controller.GroupActivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.List;

public class ViewGroup extends AppCompatActivity implements DataBaseCallBacks<Group> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    protected void onCreate(Bundle savedInstanceState) {
        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.layout);
        dataBaseAPI.getGroup(id, this, null);

    }


    @Override
    public void getUser(User user, ViewHolder holder) {

    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {

    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<Group> result, SearchType.Type type) {

    }

    @Override
    public void createUserList(List<User> userList) {

    }
}
