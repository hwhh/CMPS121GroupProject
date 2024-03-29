package com.groupproject.Controller.EventActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.groupproject.Model.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class EventInfoActivity extends AppCompatActivity implements DataBaseCallBacks<String> {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private TextView eventText;
    private TextView startDateText;
    private TextView endDateText;
    private TextView startTimeText;
    private TextView endTimeText;
    private TextView numOfPeopleText;
    private TextView eventDescription;
    private Button button;
    private Button inviteButton;
    private String userID;
    private StorageReference mStorageRef;
    private ImageView eventPic;
    List<String> goingEventsLists = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        eventText = findViewById(R.id.eventName);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        numOfPeopleText = findViewById(R.id.numOfPeopleText);
        eventDescription = findViewById(R.id.eventDescription);
        button = findViewById(R.id.btn_join_leave_del);
        userID = dataBaseAPI.getCurrentUserID();
        resetEvent();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        eventPic = findViewById(R.id.eventPic);
        inviteButton = findViewById(R.id.invite_friends);
        Button doneButton = findViewById(R.id.btn_done);
        doneButton.setOnClickListener(view -> finish());
    }

    private void resetEvent() {
        if (getIntent().hasExtra("key")) {
            String event_id = getIntent().getStringExtra("key");
            dataBaseAPI.getEvent(event_id, this, null);
        }
    }

    private String changeStringDisplay(String string) {
        return "<font color=#ff0000>" + string.charAt(0) + "</font><font color=#000000>"
                + string.substring(1, string.length()) + "</font>";
    }

    private boolean userGoingToEvent(Event event) {
        return event.goingIDs != null && userID != null && event.goingIDs.contains(userID);
    }

    private boolean userIsHost(Event event) {
        return event != null && userID != null && event.getHostID().equals(userID);
    }


    private void assignButtons(Event event) {
        if (event != null && userID != null && !userIsHost(event)) {
            if (userGoingToEvent(event)) {
                button.setBackgroundColor(getResources().getColor(R.color.red));
                button.setText(R.string.leave);
                inviteButton.setVisibility(View.VISIBLE);
            } else {
                button.setBackgroundColor(getResources().getColor(R.color.green));
                button.setText(R.string.join);
                inviteButton.setVisibility(View.GONE);
            }
        }

        if (event != null) {
            button.setOnClickListener(v -> {
                if (userID != null) {
                    if (userIsHost(event)) {
                        DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                            if (which == DialogInterface.BUTTON_POSITIVE) {
                                dataBaseAPI.deleteEvent(event);
                                finish();
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Are you sure you want to delete the event?")
                                .setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    } else {
                        if (userGoingToEvent(event)) {
                            dataBaseAPI.leaveEvent(event);
                            resetEvent();
                        } else {
                            dataBaseAPI.acceptEventInvite(event);
                            resetEvent();
                        }
                    }
                }
            });

            inviteButton.setOnClickListener(view -> {
                Intent intent = new Intent(this, InviteActivity.class);
                intent.putExtra("type", "event");
                intent.putExtra("id", event.getId());
                startActivity(intent);
            });
        }
    }

    public void display(Event event) {
        eventText.setText(Html.fromHtml(changeStringDisplay(event.getName())));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        startDateText.setText(dateFormat.format(event.getStartDate()));
        endDateText.setText(dateFormat.format(event.getEndDate()));
        startTimeText.setText(timeFormat.format(event.getStartDate()));
        endTimeText.setText(timeFormat.format(event.getEndDate()));
        String numOfPeople;
        if (event.goingIDs == null)
            numOfPeople = "0";
        else
            numOfPeople = "" + event.goingIDs.size();
        numOfPeopleText.setText(numOfPeople);
        if (userIsHost(event)) {
            button.setText(R.string.Delete);
            button.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }

    private void populateGoingUserList(Event event) {
        ListView goingUsers = findViewById(R.id.goingUsers);
        goingUsers.setAdapter(null);
        adapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item, goingEventsLists);
        goingUsers.setAdapter(adapter);
        Query query = dataBaseAPI.getmEventRef().child(event.getId()).child("goingIDs");
        dataBaseAPI.executeQuery(query, this, SearchType.Type.EVENTS);
    }

    @Override
    public void getUser(User user, ViewHolder holder) {
        if (!goingEventsLists.contains(user.getName())) {
            goingEventsLists.add(user.getName());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {
        eventDescription.setText(event.getDescription());
        goingEventsLists.clear();
        StorageReference storageReference = mStorageRef.child(event.getId()+".jpg");
        Glide.with(this)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(eventPic);
        display(event);
        assignButtons(event);
        populateGoingUserList(event);
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