package com.groupproject.Controller.SearchActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import com.groupproject.Model.DataBaseAPI.DataBaseAPI;
import com.groupproject.R;


public class SearchFragment extends Fragment implements SearchType{

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private RecyclerView mRecyclerView;
    private DatabaseReference reference;
    private SearchAdapter mSearchAdapter;

    private Type searchType;
    private String q;

    public interface SwitchFragment{
        void switchFragment(Fragment frag, Bundle args);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.q = "";
        View rootView = inflater.inflate(R.layout.search_results, container, false);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        FloatingActionButton floatingActionButton = rootView.findViewById(R.id.new_group);
        floatingActionButton.setVisibility(View.GONE);

        mRecyclerView = rootView.findViewById(R.id.search_fragment);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RadioGroup radioGroup = rootView.findViewById(R.id.rGroup);
        RadioButton radioButton = rootView.findViewById(R.id.friendsRadio);
        radioButton.setChecked(true);
        reference = dataBaseAPI.getmUserRef();
        searchType = SearchType.Type.USERS;

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.friendsRadio) {
                reference = dataBaseAPI.getmUserRef();
                searchType = SearchType.Type.USERS;
            } else if (checkedId == R.id.eventsRadio) {
                reference = dataBaseAPI.getmEventRef();
                searchType = SearchType.Type.EVENTS;
            } else if (checkedId == R.id.groupsRadio) {
                reference = dataBaseAPI.getmGroupRef();
                searchType = SearchType.Type.GROUPS;
            }
            setQ("");
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setQ(q);
    }


    public void setData(SearchAdapter mSearchAdapter) {
        this.mSearchAdapter = mSearchAdapter;
    }

    public SearchAdapter getData() {
        return mSearchAdapter;
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
        this.q = q;
        if(reference != null) {
            Query query = reference.orderByChild("nameLower").startAt(q).endAt(q + "\uf8ff");
            if (searchType.equals(Type.EVENTS))
                query = reference.orderByChild("expired_vis_nameLower").startAt("false_PUBLIC_"+q).endAt("false_PUBLIC_"+q + "\uf8ff");
            else if (searchType.equals(Type.GROUPS))
                query = reference.orderByChild("vis_nameLower").startAt("PUBLIC_"+q).endAt("PUBLIC_"+q + "\uf8ff");


            mSearchAdapter = new SearchAdapter(query, this);
            mRecyclerView.swapAdapter(mSearchAdapter, true);
            mSearchAdapter.startListening();
        }
    }

    public void switchFrag(Fragment fragment, Bundle args) {
        SwitchFragment callback = (SwitchFragment) this.getActivity();
        if (callback != null) {
            callback.switchFragment(fragment, args);
        }
    }

    public Type getSearchType() {
        return searchType;
    }
}
