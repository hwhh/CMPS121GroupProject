package com.groupproject.Controller;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.List;


public class ProfileActivity extends AppCompatActivity implements DataBaseCallBacks<User> {

    TextView userName;
    TextView emailAddress;
    TextView location;
    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_profile);
        dataBaseAPI.getUser(dataBaseAPI.getCurrentUserID(), this, null);
        userName = (TextView) findViewById(R.id.userName);
        emailAddress = (TextView) findViewById(R.id.emailAdd);
        location = (TextView) findViewById(R.id.location);
    }

    @Override
    public void getUser(User user, ViewHolder holder) {
        userName.setText(user.getName());
        emailAddress.setText(user.getEmail());
        location.setText(user.getLocation().toString());
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {

    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<User> result, SearchType.Type type) {

    }


    @Override
    public void createUserList(List<User> userList) {

    }
}
