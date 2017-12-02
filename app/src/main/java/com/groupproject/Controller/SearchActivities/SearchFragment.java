package com.groupproject.Controller.SearchActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.R;


public class SearchFragment extends Fragment {

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private RecyclerView mRecyclerView;
    private DatabaseReference reference;
    private SearchAdapter mSearchAdapter;

    public enum SearchType{
        USERS,
        EVENTS,
        GROUPS
    }

    private SearchType searchType;

    public interface SwitchFragment{
        void switchFragment(Fragment frag, Bundle args);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.search_results, container, false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRecyclerView = rootView.findViewById(R.id.search_fragment);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RadioGroup radioGroup = rootView.findViewById(R.id.rGroup);
        RadioButton radioButton = rootView.findViewById(R.id.friendsRadio);
        radioButton.setChecked(true);
        reference = dataBaseAPI.getmUserRef();
        searchType = SearchType.USERS;

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.friendsRadio) {
                reference = dataBaseAPI.getmUserRef();
                searchType = SearchType.USERS;
            } else if (checkedId == R.id.eventsRadio) {
                reference = dataBaseAPI.getmEventRef();
                searchType = SearchType.EVENTS;
            } else if (checkedId == R.id.groupsRadio) {
                reference = dataBaseAPI.getmGroupRef();
                searchType = SearchType.GROUPS;
            }
            setQ("");
        });
        setQ("");
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mSearchAdapter != null)
            mSearchAdapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void setQ(String q) {
        Query query = reference.orderByChild("lowerCaseName").startAt(q).endAt(q + "\uf8ff");
        mSearchAdapter = new SearchAdapter(query, this);
        mRecyclerView.swapAdapter(mSearchAdapter, true);
        mSearchAdapter.startListening();
    }


    public static SearchFragment newInstance(){
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public void switchFrag(Fragment fragment, Bundle args) {
        SwitchFragment callback = (SwitchFragment) this.getActivity();
        if (callback != null) {
            callback.switchFragment(fragment, args);
        }
    }

    public SearchType getSearchType() {
        return searchType;
    }
}
