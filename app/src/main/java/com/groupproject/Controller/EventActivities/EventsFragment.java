package com.groupproject.Controller.EventActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.groupproject.R;

public class EventsFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.event_views, container, false);

        PublicFragment publicFragment = new PublicFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.public_frag, publicFragment).commit();

        PrivateFragment privateFragment = new PrivateFragment();
        FragmentManager manager= getFragmentManager();
        manager.beginTransaction()
                .add(R.id.private_frag, privateFragment).commit();

        return rootView;
    }
}
