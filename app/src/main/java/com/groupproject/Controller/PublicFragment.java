package com.groupproject.Controller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.groupproject.R;

/**
 * Created by haileypun on 12/11/2017.
 */

public class PublicFragment extends Fragment {

//    RecyclerView mRecyclerView;
//    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    ImageButton[] eventButton = new ImageButton[3];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.public_fragment, container, false);

        LinearLayout buttonPanel = (LinearLayout) rootView.findViewById(R.id.buttonPanel);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < 3; i++ ){

            eventButton[i] = new ImageButton(getContext());
            ImageButton button = eventButton[i];
            button.setImageResource(R.drawable.pinnedbutton);
            button.setLayoutParams(lp);
            button.setBackgroundColor(Color.TRANSPARENT);
            button.setPadding(15, 15, 15, 15 );
            button.setTag(i);
            button.setId(i);
            buttonPanel.addView(eventButton[i]);

        }
//        // Calling the RecyclerView
//        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//
//        // The number of Columns
//        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);

//        mAdapter = new HLVAdapter(MainActivity.this, alName, alImage);
//        mRecyclerView.setAdapter(mAdapter);
        return rootView;
    }

}
