package com.groupproject.Controller.SearchActivities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
import com.google.firebase.database.FirebaseDatabase;
import com.groupproject.Model.User;
import com.groupproject.R;

public class SearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private SearchAdapter mSearchAdapter;

    String q = "";



    public void setQ(String q) {
        mSearchAdapter.setQuery(FirebaseDatabase.getInstance()
                .getReference()
                .child("users")
                .child("name")
                .equalTo(q));
        mAdapter.notifyDataSetChanged();
    }

    public SearchFragment() {
    }


    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.search_results, container, false);


        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.search_fragment);

        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mSearchAdapter = new SearchAdapter();
        mAdapter = mSearchAdapter;
        mRecyclerView.setAdapter(mAdapter);


        return rootView;
    }




}
