package com.groupproject.Controller.EventActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.groupproject.R;

public class EventInfoActivity extends AppCompatActivity {

    TextView eventText;
    TextView dateText;
    TextView locationText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_info);
        eventText = (TextView) findViewById(R.id.eventName);
        dateText = (TextView) findViewById(R.id.dateText);
        locationText = (TextView) findViewById(R.id.locationText);

        //TODO:Get event by ID

        if (getIntent().hasExtra("name")) {
            String name = getIntent().getStringExtra("name");
            String event = "<font color=#ff0000>" + name.charAt(0) + "</font><font color=#000000>"
                    + name.substring(1, name.length()) + "</font>";
            eventText.setText(Html.fromHtml(event));
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras event_location");
        }

        String date = "<font color=#ff0000>D</font><font color=#000000>ate: 11/11/2017</font>";
        dateText.setText(Html.fromHtml(date));

        String location = "<font color=#ff0000>L</font><font color=#000000>ocation: College Nine</font>";
        locationText.setText(Html.fromHtml(location));

        final Button join_btn = (Button) findViewById(R.id.btn_join);
        join_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: Check if user has already joined
                join_btn.setBackgroundColor(Color.RED);
                join_btn.setText(R.string.leave);
            }
        });
    }
}
