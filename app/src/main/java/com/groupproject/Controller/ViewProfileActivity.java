package com.groupproject.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ViewProfileActivity extends AppCompatActivity implements DataBaseCallBacks<String> {

    TextView userName;
    TextView emailAddress;
    Button profileButton;
    ImageButton upload;
    List<String> goingEventsLists = new ArrayList<>();
    private static final int PICK_PHOTO_FOR_AVATAR = 0;

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
        upload = findViewById(R.id.profileUpload);

        upload.setOnClickListener(view -> {
            pickImage();
        });

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
                upload.setClickable(true);
                break;
            case FRIENDS:
                profileButton.setText("Remove Friend");
                dataBaseAPI.removeFriend(user);
                upload.setClickable(false);
                break;
            case REQUESTED:
                profileButton.setText("Cancel Request");
                dataBaseAPI.cancelFriendRequest(user);
                upload.setClickable(false);
                break;
            case NONE:
                profileButton.setText("Send Request");
                dataBaseAPI.sendFriendRequest(user);
                upload.setClickable(false);
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

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                //e.g. create user, then change "images" to where i was called from
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                Drawable buttonBg = Drawable.createFromStream(inputStream, null);
                upload.setImageDrawable(buttonBg);
                Toast.makeText(getApplicationContext(), "Image uploaded.", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }




}
