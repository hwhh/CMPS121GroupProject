package com.groupproject.Controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.Query;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity implements DataBaseCallBacks<String> {

    TextView userName;
    TextView emailAddress;
    TextView location;
    Button profileButton;
    List<String> goingEventsLists = new ArrayList<>();

    DataBaseAPI.UserRelationship userRelationship;

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private ArrayAdapter<String> adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_profile);
        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
        setContentView(R.layout.view_profile);
        dataBaseAPI.getUser(id, this, null);

        profileButton = findViewById(R.id.profileButton);
        userName = findViewById(R.id.userName);
        emailAddress = findViewById(R.id.emailAdd);

        ListView goingEvents = findViewById( R.id.goingEventsLists);
        adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, goingEventsLists);
        goingEvents.setAdapter(adapter);

        Query query = dataBaseAPI.getmUserRef().child(id).child("goingEventsIDs");
        dataBaseAPI.executeQuery(query, this, SearchType.Type.EVENTS);
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
        //TODO IF PUBLIC etc
        goingEventsLists.add(event.getName() + " " +event.getDescription());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {}

    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {
        for (String id : result) {
            dataBaseAPI.getEvent(id, this, null);
        }

    }




}
