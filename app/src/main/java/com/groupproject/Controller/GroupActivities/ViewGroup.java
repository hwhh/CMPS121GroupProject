package com.groupproject.Controller.GroupActivities;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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

public class ViewGroup extends AppCompatActivity implements DataBaseCallBacks<Group> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    TextView groupName;
    TextView members;
    TextView groupDescription;
    TextView events;
    ImageView groupPic;
    private StorageReference mStorageRef;

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
        groupPic = findViewById(R.id.groupPic);

        mStorageRef = FirebaseStorage.getInstance().getReference();

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

    public void downloadFile() throws IOException {
        File localFile = File.createTempFile("images", "jpg");
        StorageReference riversRef = mStorageRef.child("images/rivers.jpg");
        riversRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Successfully downloaded data to local file
                    // ...
                }).addOnFailureListener(exception -> {
                    // Handle failed download
                    // ...
                });
    }

}
