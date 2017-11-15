package com.groupproject.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.groupproject.R;

/**
 * Created by haileypun on 12/11/2017.
 */

public class PrivateFragment extends Fragment {

    ImageButton[] eventButton = new ImageButton[3];
    TextView privateText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.private_fragment, container, false);

        privateText = (TextView)rootView.findViewById(R.id.privateText);

        String text = "<font color=#ff0000>P</font><font color=#000000>rivate</font>";
        privateText.setText(Html.fromHtml(text));

        LinearLayout buttonPanel = (LinearLayout) rootView.findViewById(R.id.buttonPanel);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

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
                    EventInfoFragment eventInfoFragment = new EventInfoFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.private_frag, eventInfoFragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            buttonPanel.addView(eventButton[i]);

        }

        return rootView;
    }

}
