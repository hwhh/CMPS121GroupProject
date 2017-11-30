package com.groupproject.Controller.SearchActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.R;


public class SearchFragment extends Fragment {

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private SearchAdapter mSearchAdapter;
    private Query query;
    private RecyclerView mRecyclerView;
    private View rootView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.search_results, container, false);
        mRecyclerView = rootView.findViewById(R.id.search_fragment);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        setQ("");
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


    public void setQ(String q) {
        query = dataBaseAPI.getmUserRef().orderByChild("name").startAt(q).endAt(q+"\uf8ff");

        query.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.exists()) {
                    System.out.println(dataSnapshot.getValue(com.groupproject.Model.User.class).getName());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mSearchAdapter = new SearchAdapter(query);
        mRecyclerView.swapAdapter(mSearchAdapter, true);
        mSearchAdapter.notifyDataSetChanged();


//        mSearchAdapter.notifyDataSetChanged();
    }


    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



}
