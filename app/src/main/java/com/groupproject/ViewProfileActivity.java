package com.groupproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;

import java.util.List;

public class ViewProfileActivity extends AppCompatActivity implements DataBaseCallBacks<User> {

    TextView userName;
    TextView emailAddress;
    TextView location;
    Button profileButton;

    DataBaseAPI.UserRelationship userRelationship;

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
        setContentView(R.layout.view_profile);
        dataBaseAPI.getUser(id, this, null);

        profileButton = (Button) findViewById(R.id.profileButton);
        userName = (TextView) findViewById(R.id.userName);
        emailAddress = (TextView) findViewById(R.id.emailAdd);
        location = (TextView) findViewById(R.id.location);
    }


    @Override
    public void getUser(User user, ViewHolder holder) {
        userRelationship = dataBaseAPI.getRelationShip(user);
        switch (userRelationship){
            case ME:
                profileButton.setVisibility(View.GONE);
                break;
            case FRIENDS:
                profileButton.setText("Remove Friend");
                dataBaseAPI.removeFriend(user);
                break;
            case REQUESTED:
                profileButton.setText("Cancel Request");
                dataBaseAPI.cancelFriendRequest(user);
                break;
            case NONE:
                profileButton.setText("Send Request");
                dataBaseAPI.sendFriendRequest(user);
                break;
        }

        userName.setText(user.getName());
        emailAddress.setText(user.getEmail());
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
