package com.groupproject.Controller.SearchActivities;




import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.R;


public class SearchAdapter extends FirebaseRecyclerAdapter<DataBaseItem, SearchAdapter.ViewHolder> {

    private final SearchFragment searchFragment;

    SearchAdapter(Query query, SearchFragment searchFragment) {
        super(new FirebaseRecyclerOptions.Builder<DataBaseItem>().setQuery(query, DataBaseItem.class).build());
        this.searchFragment = searchFragment;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.search_result, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(final ViewHolder holder, int position, DataBaseItem model) {
        final Bundle args = new Bundle();
        args.putString("id", model.getId());
        holder.vName.setText(model.getName());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchFragment.getSearchType() == SearchFragment.SearchType.Friends)
                    searchFragment.switchFrag(new UserProfileFragment(), args);
                if (searchFragment.getSearchType() == SearchFragment.SearchType.Events){}
                if (searchFragment.getSearchType() == SearchFragment.SearchType.Groups){}
            }
        });

    }


    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView vName;
        CardView cardView;

        ViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtName);
            cardView = v.findViewById(R.id.card_view);
        }

    }

    @Override
    public void onDataChanged() {
        // Called each time there is a new data snapshot. You may want to use this method
        // to hide a loading spinner or check for the "no documents" state and update your UI.
        // ...
    }

    @Override
    public void onError(DatabaseError e) {
        // Called when there is an error getting data. You may want to update
        // your UI to display an error message to the user.
        // ...
    }



}


