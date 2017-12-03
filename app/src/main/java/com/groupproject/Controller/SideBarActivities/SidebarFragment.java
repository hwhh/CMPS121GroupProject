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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.Controller.EventActivities.AddEventActivity;
import com.groupproject.Controller.GroupActivities.NewGroup;
import com.groupproject.Controller.SearchActivities.SearchAdapter;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.R;

public class SidebarFragment extends Fragment implements SearchType {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    private SearchAdapter mSearchAdapter;

    private Type searchType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_results, container, false);
        FrameLayout radioGroup = rootView.findViewById(R.id.radio_buttons);
        radioGroup.setVisibility(View.GONE);
        Bundle bundle = getArguments();

        String type = bundle.getString("type");
        FloatingActionButton create  = rootView.findViewById(R.id.new_group);
        Query query = null;
        Intent intent = null;

        if(type.equals("friend")){
            searchType = Type.USERS;
            create.setVisibility(View.GONE);
            query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child();
        }
        if(type.equals("events")){
            searchType = Type.EVENTS;
            intent = new Intent(getActivity(), AddEventActivity.class);
            query = dataBaseAPI.getmUserRef().child(dataBaseAPI.getCurrentUserID()).child("goingEventsIDs");
        }if(type.equals("groups")){
            searchType = Type.GROUPS;
            intent = new Intent(getActivity(), NewGroup.class);
            query = dataBaseAPI.getmGroupRef().orderByChild("membersIDs/").equalTo(FirebaseAuth.getInstance().getUid());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        System.out.println(dataSnapshot.getValue());
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        Intent finalIntent = intent;
        create.setOnClickListener(view -> {
            if(finalIntent != null)
                startActivity(finalIntent);
        });


        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        RecyclerView mRecyclerView = rootView.findViewById(R.id.search_fragment);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mSearchAdapter = new SearchAdapter(query, this);
        mRecyclerView.swapAdapter(mSearchAdapter, true);
        mSearchAdapter.startListening();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mSearchAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSearchAdapter.startListening();
    }


    public Type getSearchType() {
        return searchType;
    }
}
