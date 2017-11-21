package com.groupproject.Controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.groupproject.R;

public class EventInfoFragment extends Fragment{

    TextView eventText;
    TextView dateText;
    TextView locationText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.eventinfo_frag, container, false);

        eventText = (TextView) rootView.findViewById(R.id.eventName);
        dateText = (TextView) rootView.findViewById(R.id.dateText);
        locationText = (TextView) rootView.findViewById(R.id.locationText);

        String event = "<font color=#ff0000>E</font><font color=#000000>vent</font> <font color=#ff0000>N</font><font color=#000000>ame</font>";
        eventText.setText(Html.fromHtml(event));

        String date = "<font color=#ff0000>D</font><font color=#000000>ate: 11/11/2017</font>";
        dateText.setText(Html.fromHtml(date));

        String location = "<font color=#ff0000>L</font><font color=#000000>ocation: College Nine</font>";
        locationText.setText(Html.fromHtml(location));

        return rootView;

    }
}
