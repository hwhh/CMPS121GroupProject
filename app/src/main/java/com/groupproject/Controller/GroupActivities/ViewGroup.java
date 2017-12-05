package com.groupproject.Controller.GroupActivities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
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
    TextView groupName;
    TextView members;
    TextView groupDescription;
    TextView events;


    protected void onCreate(Bundle savedInstanceState) {

        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);
        dataBaseAPI.getGroup(id, this, null);
        groupName = findViewById(R.id.groupName);
        members = findViewById(R.id.memberInfo);
        groupDescription = findViewById(R.id.groupDescription);
        events = findViewById(R.id.eventsGroup);

    }


    @Override
    public void getUser(User user, ViewHolder holder) {

    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {

    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {
        groupName.setText(group.getName());
        members.setText(group.getMembersIDs().toString());
        groupDescription.setText(group.getDescription());
        events.setText(group.getEventsIDs().toString());
    }

    @Override
    public void executeQuery(List<Group> result, SearchType.Type type) {

    }

}
