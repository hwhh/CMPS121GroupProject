package com.groupproject.Controller.SideBarActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.groupproject.Controller.NewGroup;
import com.groupproject.Controller.SearchActivities.SearchAdapter;
import com.groupproject.Controller.SearchActivities.SearchFragment;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.R;

public class GroupsFragment extends Fragment implements SearchType {

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private RecyclerView mRecyclerView;

    private SearchAdapter mSearchAdapter;

    private Type searchType;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.search_results, container, false);
        LinearLayout radioGroup = rootView.findViewById(R.id.radio_buttons);
        ((ViewManager)radioGroup.getParent()).removeView(radioGroup);

        FloatingActionButton newGroup = rootView.findViewById(R.id.new_group);
        newGroup.setOnClickListener(view -> {
            NewGroup newGroups = new NewGroup();
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_search_fragment, newGroups, "new_groups").commitAllowingStateLoss();
        });


        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = rootView.findViewById(R.id.search_fragment);
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchType = Type.GROUPS;

        Query query = dataBaseAPI.getmGroupRef().child("membersIDs").equalTo(FirebaseAuth.getInstance().getUid());
        mSearchAdapter = new SearchAdapter(query, this);
        mRecyclerView.swapAdapter(mSearchAdapter, true);

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
        mSearchAdapter.stopListening();
    }


    public Type getSearchType() {
        return searchType;
    }
}
