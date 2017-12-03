package com.groupproject.Controller.EventActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Event;
import com.groupproject.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class EventInfoActivity extends AppCompatActivity implements Callback {

    private TextView eventText;
    private TextView startDateText;
    private TextView endDateText;
    private TextView startTimeText;
    private TextView endTimeText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        eventText = (TextView) findViewById(R.id.eventName);
        startDateText = (TextView) findViewById(R.id.startDateText);
        endDateText = (TextView) findViewById(R.id.endDateText);
        startTimeText = (TextView) findViewById(R.id.startTimeText);
        endTimeText = (TextView) findViewById(R.id.endTimeText);


        if (getIntent().hasExtra("event_id")) {
            String event_id = getIntent().getStringExtra("event_id");
            Query q = DataBaseAPI.getDataBase().getmEventRef().child(event_id);
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        Event event = dataSnapshot.getValue(com.groupproject.Model.Event.class);
                        display(event);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras event_location");
        }

        final Button join_btn = (Button) findViewById(R.id.btn_join);
        join_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Check if user has already joined
                join_btn.setBackgroundColor(Color.RED);
                join_btn.setText(R.string.leave);
            }
        });
    }

    private String changeStringDisplay(String string) {
        return "<font color=#ff0000>" + string.charAt(0) + "</font><font color=#000000>"
                + string.substring(1, string.length()) + "</font>";
    }

    @Override
    public void display(Event event) {
        eventText.setText(Html.fromHtml(changeStringDisplay(event.getName())));
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yy", Locale.US);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.US);
        startDateText.setText(dateFormat.format(event.getStartDate()));
        endDateText.setText(dateFormat.format(event.getEndDate()));
        startTimeText.setText(timeFormat.format(event.getStartDate()));
        endTimeText.setText(timeFormat.format(event.getEndDate()));
    }
}


interface Callback {
    void display(Event e);
}