package com.groupproject.Controller.EventActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupproject.Controller.MainActivity;
import com.groupproject.R;

public class PublicFragment extends Fragment {

    TextView publicText;

    ImageButton[] eventButton = new ImageButton[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.events_public, container, false);

        publicText = (TextView)rootView.findViewById(R.id.publicText);

        String text = "<font color=#ff0000>P</font><font color=#000000>ublic</font>";
        publicText.setText(Html.fromHtml(text));

        LinearLayout buttonPanel = (LinearLayout) rootView.findViewById(R.id.buttonPanel);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //TODO: Cycle through events user is going to from db
        for (int i = 0; i < 3; i++ ){

            eventButton[i] = new ImageButton(getContext());
            ImageButton button = eventButton[i];
            button.setImageResource(R.drawable.eventpic);
            button.setLayoutParams(lp);
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setPadding(15, 15, 15, 15 );
            button.setTag(i);
            button.setId(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    //TODO: pass event as argument
                    mainActivity.switchToMaps(null);
                }
            });
            buttonPanel.addView(eventButton[i]);

        }
        return rootView;
    }

}
