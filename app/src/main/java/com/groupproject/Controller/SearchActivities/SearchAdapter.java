package com.groupproject.Controller.SearchActivities;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;


public class SearchAdapter extends FirebaseRecyclerAdapter<DataBaseItem, SearchAdapter.ViewHolder> {


    SearchAdapter(Query query) {
        super(new FirebaseRecyclerOptions.Builder<DataBaseItem>().setQuery(query, DataBaseItem.class).build());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.search_result, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, int position, DataBaseItem model) {
        holder.vName.setText(model.getName());

        if(model instanceof User)
            holder.vButton.setText("Add Friend");
        if(model instanceof Event)
            holder.vButton.setText("View Event");
        if(model instanceof Group)
            holder.vButton.setText("Join Group");
    }



    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView vName;
        Button vButton;

        ViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtName);
            vButton = v.findViewById(R.id.interact_button);
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


