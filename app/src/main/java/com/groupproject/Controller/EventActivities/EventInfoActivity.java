package com.groupproject.Controller.EventActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.Query;
import com.groupproject.Controller.InviteActivity;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class EventInfoActivity extends AppCompatActivity implements DataBaseCallBacks<String> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private TextView eventText;
    private TextView startDateText;
    private TextView endDateText;
    private TextView startTimeText;
    private TextView endTimeText;
    private TextView numOfPeopleText;
    private Button joinButton;
    private Event event;
    private User user;
    private StorageReference mStorageRef;
    private ImageView eventPic;
    private Button interact;
    List<String> goingEventsLists = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private AlertDialog.Builder builder;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        eventText = findViewById(R.id.eventName);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        numOfPeopleText = findViewById(R.id.numOfPeopleText);
        joinButton = findViewById(R.id.btn_join);

        dataBaseAPI.getEvent((id), this, null);
        dataBaseAPI.getUser(dataBaseAPI.getCurrentUserID(), this, null);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        eventPic = findViewById(R.id.eventPic);
        interact = findViewById(R.id.btn_join);
        builder = new AlertDialog.Builder(this);
        Bundle b = getIntent().getExtras();
        String id = b.getString("key");
    }


    @Override
    public void getUser(User user, ViewHolder holder) {
        goingEventsLists.add(user.getName());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {
        display(event);
        StorageReference storageReference = mStorageRef.child(event.getId()+".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(eventPic);
        display(event);
        assignButton();
        ListView goingEvents = findViewById( R.id.goingUsers);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, goingEventsLists);
        goingEvents.setAdapter(adapter);
        Query query = dataBaseAPI.getmEventRef().child(event.getId()).child("goingIDs");
        dataBaseAPI.executeQuery(query, this, SearchType.Type.EVENTS);
    }


    public void display(Event event) {
        eventText.setText(event.getName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        startDateText.setText(dateFormat.format(event.getStartDate()));
        endDateText.setText(dateFormat.format(event.getEndDate()));
        startTimeText.setText(timeFormat.format(event.getStartDate()));
        endTimeText.setText(timeFormat.format(event.getEndDate()));
        if (event.goingIDs == null)
            numOfPeopleText.setText("0");
        else
            numOfPeopleText.setText(String.valueOf(event.goingIDs.size()));
        DataBaseAPI.STATUS status = dataBaseAPI.getEventRelationShip(event);
        updateButton(status, event);
        interact.setOnClickListener(v -> {
            if (status == DataBaseAPI.STATUS.HOST) {
                DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                    if (which == DialogInterface.BUTTON_POSITIVE) {
                        dataBaseAPI.deleteEvent(event);
                        finish();
                    }
                };
                builder.setMessage("Are you sure you want to delete the event?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            } else if (status == DataBaseAPI.STATUS.JOINED) {
                dataBaseAPI.leaveEvent(event);
                interact.setText("Leave");
                updateButton(DataBaseAPI.STATUS.INVITED, event);
            } else if (status == DataBaseAPI.STATUS.INVITED) {
                interact.setBackgroundColor(getResources().getColor(R.color.green));
                interact.setText("Accept Invite");
                updateButton(DataBaseAPI.STATUS.JOINED, event);
            } else {
                interact.setBackgroundColor(getResources().getColor(R.color.red));
                interact.setText("Join");
                updateButton(DataBaseAPI.STATUS.JOINED, event);
            }
        });
    }

    public void updateButton(DataBaseAPI.STATUS status, Event event){
        switch (status){
            case HOST:
                interact.setText(R.string.Delete);
                interact.setBackgroundColor(getResources().getColor(R.color.red));
                Button button = findViewById(R.id.invite_friends);
                button.setOnClickListener(view -> {
                    Intent intent = new Intent(this, InviteActivity.class);
                    intent.putExtra("type", "event");
                    intent.putExtra("id", event.getId());
                    startActivity(intent);
                });
                break;
            case HIDDEN:
                finish();
                break;
            case JOINED:
                interact.setBackgroundColor(getResources().getColor(R.color.red));
                interact.setText("Leave");
                break;
            case INVITED:
                interact.setBackgroundColor(getResources().getColor(R.color.green));
                interact.setText("Accept Invite");
                break;
            case PUBLIC:
                interact.setBackgroundColor(getResources().getColor(R.color.red));
                interact.setText("Join");
                break;
        }

    }


    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {
        for (String id : result) {
            dataBaseAPI.getUser(id, this, null);
        }
    }

}


