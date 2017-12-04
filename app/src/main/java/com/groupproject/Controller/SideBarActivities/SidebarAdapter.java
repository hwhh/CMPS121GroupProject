package com.groupproject.Controller.SideBarActivities;

import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.List;

public class SidebarAdapter extends RecyclerView.Adapter<ViewHolder>{

    private static final DataBaseAPI databaseAPI = DataBaseAPI.getDataBase();
    private final SearchType.Type type;
    private List<DataBaseItem> items;
    private final Fragment fragment;

    SidebarAdapter(Fragment fragment, SearchType.Type type) {
        super();
        items = new ArrayList<>();
        this.fragment = fragment;
        this.type = type;
    }

    List<DataBaseItem> getItems() {
        return items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result, viewGroup, false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataBaseItem dataBaseItem = items.get(position);
        holder.vName.setText(dataBaseItem.getName());
        switch (type){
            case USERS:
                //On card click view User
                holder.interact.setImageDrawable(null);

                break;
            case EVENTS:
                //On card click view Event
                holder.interact.setImageDrawable(null);

                break;
            case GROUPS:
                holder.interact.setImageDrawable(null);
                //On card click view Group
                break;
            case NOTIFICATIONS:
                holder.interact.setImageDrawable(ContextCompat.getDrawable(fragment.getActivity(), R.drawable.accept_button));
                holder.interact.setOnClickListener(view -> {
                    if(dataBaseItem instanceof User) {
                        databaseAPI.acceptRequestUser((User) dataBaseItem);
                    }else if(dataBaseItem instanceof Event){
                        databaseAPI.acceptEventInvite((Event) dataBaseItem);
                    }else if(dataBaseItem instanceof Group){
                        databaseAPI.acceptGroupInvite((Group) dataBaseItem);
                    }
                    this.getItems().remove(dataBaseItem);
                    this.notifyDataSetChanged();
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



}
