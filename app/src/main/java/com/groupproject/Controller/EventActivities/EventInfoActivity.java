package com.groupproject.Controller.EventActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
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
import com.groupproject.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EventInfoActivity extends AppCompatActivity implements DataBaseCallBacks<String> {

    private static final DataBaseAPI database = DataBaseAPI.getDataBase();
    private TextView eventText;
    private TextView startDateText;
    private TextView endDateText;
    private TextView startTimeText;
    private TextView endTimeText;
    private Button joinButton;
    private Event event;
    private User user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        eventText = findViewById(R.id.eventName);
        startDateText = findViewById(R.id.startDateText);
        endDateText = findViewById(R.id.endDateText);
        startTimeText = findViewById(R.id.startTimeText);
        endTimeText = findViewById(R.id.endTimeText);
        joinButton = findViewById(R.id.btn_join);
        if (getIntent().hasExtra("event_id")) {
            String event_id = getIntent().getStringExtra("event_id");
            database.getEvent(event_id, this, null);
        }
        database.getUser(database.getCurrentUserID(), this, null);

        joinButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (event != null && user != null) {
                    if (userGoingToEvent()) {
                        database.leaveEvent(event);
//                        switchButton();
                        finish();
                    } else {
                        database.addEventToUser(event);
//                        switchButton();
                        finish();
                    }
                }
            }
        });
    }

    private String changeStringDisplay(String string) {
        return "<font color=#ff0000>" + string.charAt(0) + "</font><font color=#000000>"
                + string.substring(1, string.length()) + "</font>";
    }

    private boolean userGoingToEvent() {
        return event.goingIDs.contains(database.getCurrentUserID());
    }

    private void assignButton() {
        if (event != null && user != null) {
            if (userGoingToEvent()) {
                joinButton.setBackgroundColor(Color.RED);
                joinButton.setText(R.string.leave);
            } else {
                joinButton.setBackgroundColor(Color.GREEN);
                joinButton.setText(R.string.join);
            }
        }
    }

    private void switchButton() {
        if (event != null && user != null) {
            if (userGoingToEvent()) {
                joinButton.setBackgroundColor(Color.GREEN);
                joinButton.setText(R.string.join);
            } else {
                joinButton.setBackgroundColor(Color.RED);
                joinButton.setText(R.string.leave);
            }
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
    }

    @Override
    public void getUser(User user, ViewHolder holder) {
        this.user = user;
        assignButton();
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {
        this.event = event;
        display(event);
        assignButton();
    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {

    }

    @Override
    public void executeQuery(List<String> result, SearchType.Type type) {

    }


    @Override
    public void createUserList(List<User> userList) {

    }
}


