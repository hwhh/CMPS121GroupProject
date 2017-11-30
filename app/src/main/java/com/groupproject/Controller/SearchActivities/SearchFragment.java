package com.groupproject.Controller.SearchActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.R;


public class SearchFragment extends Fragment {

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private SearchAdapter mSearchAdapter;
    private RecyclerView mRecyclerView;
    private DatabaseReference reference;
    private String type;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_results, container, false);

        mRecyclerView = rootView.findViewById(R.id.search_fragment);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RadioGroup radioGroup = rootView.findViewById(R.id.rGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case (1):reference = dataBaseAPI.getmUserRef();
                        type = "user";
                        setQ("");
                        break;
                    case (2):reference = dataBaseAPI.getmEventRef();
                        type = "event";
                        setQ("");
                        break;
                    case (3):reference = dataBaseAPI.getmGroupRef();
                        type = "group";
                        setQ("");
                        break;
                }

            }
        });

        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
//        mSearchAdapter.stopListening();
    }


    @Override
    public void onStart() {
        super.onStart();
//        mSearchAdapter.startListening();
    }


    public void setQ(String q) {
        if(reference != null) {
            Query query = reference.orderByChild("lowerCaseName").startAt(q).endAt(q + "\uf8ff");
            mSearchAdapter = new SearchAdapter(query, type);
            mRecyclerView.swapAdapter(mSearchAdapter, true);
            mSearchAdapter.startListening();
        }
    }


    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }



}
