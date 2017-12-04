package com.groupproject.Controller.SideBarActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.firebase.database.Query;
import com.groupproject.Controller.EventActivities.AddEventActivity;
import com.groupproject.Controller.GroupActivities.NewGroup;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.List;

public class SidebarFragment extends Fragment implements SearchType, DataBaseCallBacks<String>{

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    private SidebarAdapter mSearchAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_results, container, false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());

        FrameLayout radioGroup = rootView.findViewById(R.id.radio_buttons);
        radioGroup.setVisibility(View.GONE);

        Type searchType = null;
        Intent intent = null;

        Bundle bundle = getArguments();
        String type = bundle.getString("type");
        FloatingActionButton create  = rootView.findViewById(R.id.new_group);
        Query query;

        assert type != null;
        switch (type) {
            case "friend":
                searchType = Type.USERS;
                create.setVisibility(View.GONE);
                query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("requestsID");
                dataBaseAPI.executeQuery(query, this, Type.USERS);
                break;
            case "events":
                searchType = Type.EVENTS;
                intent = new Intent(getActivity(), AddEventActivity.class);
                query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("goingEventsIDs");
                dataBaseAPI.executeQuery(query, this, Type.EVENTS);
                break;
            case "groups":
                searchType = Type.GROUPS;
                intent = new Intent(getActivity(), NewGroup.class);
                query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("joinedGroupIDs");
                dataBaseAPI.executeQuery(query, this, Type.GROUPS);
                break;
            case "notifications":
                searchType = Type.NOTIFICATIONS;
                query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("requestsID");
                dataBaseAPI.executeQuery(query, this, Type.USERS);
                query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("invitedEventsIDs");
                dataBaseAPI.executeQuery(query, this, Type.EVENTS);
                query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("invitedGroupIDs");
                dataBaseAPI.executeQuery(query, this, Type.GROUPS);
                break;
        }

        mSearchAdapter = new SidebarAdapter(this, searchType);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.search_fragment);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.swapAdapter(mSearchAdapter, true);

        Intent finalIntent = intent;
        create.setOnClickListener(view -> {
            if(finalIntent != null)
                startActivity(finalIntent);
        });
        return rootView;

    }


    @Override
    public void getUser(User user, ViewHolder holder) {
        mSearchAdapter.getItems().add(user);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void getEvent(Event event, ViewHolder holder) {
        mSearchAdapter.getItems().add(event);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void getGroup(Group group, ViewHolder holder) {
        mSearchAdapter.getItems().add(group);
        mSearchAdapter.notifyDataSetChanged();
    }

    @Override
    public void executeQuery(List<String> result, Type type) {
        for (String s : result) {
            if(type == Type.USERS)
                dataBaseAPI.getUser(s, this, null);
            else if(type == Type.EVENTS)
                dataBaseAPI.getEvent(s, this, null);
            else if(type == Type.GROUPS)
                dataBaseAPI.getGroup(s, this, null);
        }
    }


    @Override
    public void createUserList(List<User> userList) {

    }
}
