package com.groupproject.Controller.EventActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.widget.Button;
import android.widget.TextView;

import com.groupproject.Controller.InviteActivity;
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
    private TextView numOfPeopleText;
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
        numOfPeopleText = findViewById(R.id.numOfPeopleText);
        joinButton = findViewById(R.id.btn_join);
        resetEvent();
        database.getUser(database.getCurrentUserID(), this, null);

        joinButton.setOnClickListener(v -> {
            if (event != null && user != null) {
                if (userIsHost()) {
                    DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
                        if (which == DialogInterface.BUTTON_POSITIVE) {
                            database.deleteEvent(event);
                            finish();
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Are you sure you want to delete the event?")
                            .setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();
                } else {
                    if (userGoingToEvent()) {
                        database.leaveEvent(event);
                        resetEvent();
                    } else {
                        database.acceptEventInvite(event);
                        resetEvent();
                    }
                }
            }
        });

        Button button = findViewById(R.id.invite_friends);
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, InviteActivity.class);
            intent.putExtra("type", "event");intent.putExtra("id", event.getId());
            startActivity(intent);
        });


    }

    private void resetEvent() {
        event = null;
        if (getIntent().hasExtra("key")) {
            String event_id = getIntent().getStringExtra("key");
            database.getEvent(event_id, this, null);
        }
    }

    private String changeStringDisplay(String string) {
        return "<font color=#ff0000>" + string.charAt(0) + "</font><font color=#000000>"
                + string.substring(1, string.length()) + "</font>";
    }

    private boolean userGoingToEvent() {
        return event.goingIDs != null && event.goingIDs.contains(database.getCurrentUserID());
    }

    private boolean userIsHost() {
        return event != null && event.getHostID().equals(database.getCurrentUserID());
    }

    private void assignButton() {
        if (event != null && user != null && !userIsHost()) {
            if (userGoingToEvent()) {
                joinButton.setBackgroundColor(getResources().getColor(R.color.red));
                joinButton.setText(R.string.leave);
            } else {
                joinButton.setBackgroundColor(getResources().getColor(R.color.green));
                joinButton.setText(R.string.join);
            }
        }
    }

    private void switchButton() {
        if (event != null && user != null && !userIsHost()) {
            if (userGoingToEvent()) {
                joinButton.setBackgroundColor(getResources().getColor(R.color.green));
                joinButton.setText(R.string.join);
            } else {
                joinButton.setBackgroundColor(getResources().getColor(R.color.red));
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
        String numOfPeople;
        if (event.goingIDs == null)
            numOfPeople = "0";
        else
            numOfPeople = "" + event.goingIDs.size();
        numOfPeopleText.setText(numOfPeople);
        if (userIsHost()) {
            joinButton.setText(R.string.Delete);
            joinButton.setBackgroundColor(getResources().getColor(R.color.red));
        }
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


}