package com.groupproject.Controller.GroupActivities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.groupproject.Controller.InviteActivity;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.HIDDEN;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.HOST;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.INVITED;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.JOINED;
import static com.groupproject.DataBaseAPI.DataBaseAPI.STATUS.PUBLIC;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.FRIENDS;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.ME;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.REQUESTED;
import static com.groupproject.DataBaseAPI.DataBaseAPI.UserRelationship.NONE;

public class ViewGroupActivity extends AppCompatActivity implements DataBaseCallBacks<Group> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    TextView groupName;
    TextView members;
    TextView groupDescription;
    TextView events;
    Button joinGroup;
    ImageView groupPic;
    private StorageReference mStorageRef;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_group);
        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
        dataBaseAPI.getGroup(id, this, null);
        groupName = findViewById(R.id.groupName);
        members = findViewById(R.id.memberInfo);
        groupDescription = findViewById(R.id.groupDescription);
        events = findViewById(R.id.eventsGroup);
        groupPic = findViewById(R.id.groupPic);
        joinGroup = findViewById(R.id.joinGroup);
        mStorageRef = FirebaseStorage.getInstance().getReference();



    }

    @Override
    public void onResume() {
        super.onResume();
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
        StorageReference storageReference = mStorageRef.child(group.getId()+".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(groupPic);
        joinGroup.setOnClickListener(view -> {
            dataBaseAPI.acceptGroupInvite(group);
            finish();
        });

        DataBaseAPI.STATUS status = dataBaseAPI.getGroupRelationShip(group);
        switch (status){
            case HOST:
                break;
            case HIDDEN:
                finish();
                break;
            case JOINED:
                break;
            case INVITED:
                break;
            case PUBLIC:
                break;
        }
    }

    @Override
    public void executeQuery(List<Group> result, SearchType.Type type) {

    }


}
