package com.groupproject.Controller;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;

import com.groupproject.R;

public class MainActivity extends FragmentActivity {

    private static final int MAPS_INDEX = 1;
    // Fragment TabHost as mTabHost
    private FragmentTabHost mTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("events").setIndicator("events"),
                        EventsFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("maps").setIndicator("maps"),
                        MapsFragment.class, null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void switchToMaps(Event event) {
        mTabHost.setCurrentTab(MAPS_INDEX);
        //TODO: Open maps on event's location
    }

}
