package com.groupproject.Controller.GroupActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.groupproject.Controller.InviteActivity;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.Model.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.List;


public class ViewGroupActivity extends AppCompatActivity implements DataBaseCallBacks<String> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private ArrayAdapter<String> eventAdapter;
    private ArrayAdapter<String> memberAdapter;

    TextView groupName;
    TextView groupDescription;

    Button groupInteract;
    Button inviteButton;
    ImageView groupPic;
    List<String> eventList = new ArrayList<>();
    List<String> memberList = new ArrayList<>();

    private StorageReference mStorageRef;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);

        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
        dataBaseAPI.getGroup(id, this, null);
        groupName = findViewById(R.id.groupName);
        groupDescription = findViewById(R.id.groupDescription);
        groupPic = findViewById(R.id.groupPic);
        groupInteract = findViewById(R.id.groupInteract);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        ListView eventListView = findViewById( R.id.eventListView);
        ListView membersListView = findViewById( R.id.memberListView);
        eventAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, eventList);
        memberAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, memberList);
        eventListView.setAdapter(eventAdapter);
        membersListView.setAdapter(memberAdapter);

        inviteButton = findViewById(R.id.groupInvite);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void getUser(User user, ViewHolder holder) {
        memberList.add(user.getName());
        memberAdapter.notifyDataSetChanged();
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {
        eventList.add(event.getName());
        eventAdapter.notifyDataSetChanged();
    }


    @Override
    public void getGroup(Group group, ViewHolder holder) {
        groupName.setText(group.getName());
        groupDescription.setText(group.getDescription());
        Query query1 = dataBaseAPI.getmGroupRef().child(group.getId()).child("membersIDs");
        dataBaseAPI.executeQuery(query1, this, SearchType.Type.USERS);
        Query query2 = dataBaseAPI.getmGroupRef().child(group.getId()).child("eventsIDs");
        dataBaseAPI.executeQuery(query2, this, SearchType.Type.EVENTS);
        StorageReference storageReference = mStorageRef.child(group.getId()+".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(groupPic);

        DataBaseAPI.STATUS status = dataBaseAPI.getGroupRelationShip(group);
        switch (status) {
            case HOST:
                groupInteract.setText("Delete Group");
                break;
            case HIDDEN:
                finish();
                break;
            case JOINED:
                groupInteract.setText("Leave Group");
                break;
            case INVITED:
                groupInteract.setText("Accept Invite");
                break;
            case PUBLIC:
                groupInteract.setText("Join Group");
                break;
        }

        groupInteract.setOnClickListener(view -> {
            switch (status) {
                case HOST:
                    dataBaseAPI.deleteGroup(group);
                    finish();
                    break;
                case HIDDEN:
                    finish();
                    break;
                case JOINED:
                    resetGroup();
                    dataBaseAPI.leaveGroup(group);
                    break;
                case INVITED:
                    resetGroup();
                    inviteButton.setVisibility(View.VISIBLE);
                    dataBaseAPI.acceptGroupInvite(group);
                    break;
                case PUBLIC:
                    resetGroup();
                    dataBaseAPI.acceptGroupInvite(group);
                    break;
            }
        });


        DataBaseAPI.STATUS userStatus = dataBaseAPI.getGroupRelationShip(group);
        if(userStatus == DataBaseAPI.STATUS.JOINED || userStatus == DataBaseAPI.STATUS.HOST) {
            inviteButton.setVisibility(View.VISIBLE);
            inviteButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, InviteActivity.class);
                intent.putExtra("type", "group");
                intent.putExtra("id", group.getId());
                startActivity(intent);
            });
        }else{
            inviteButton.setVisibility(View.GONE);
        }

    }

    private void resetGroup() {
        if (getIntent().hasExtra("key")) {
            String id = getIntent().getStringExtra("key");
            dataBaseAPI.getGroup(id, this, null);
        }
    }

    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {
        if(type == SearchType.Type.USERS){
            for (String id : result) {
                dataBaseAPI.getUser(id, this, null);
            }
        }else if(type == SearchType.Type.EVENTS){
            for (String id : result) {
                dataBaseAPI.getEvent(id, this, null);
            }
        }
    }
}
