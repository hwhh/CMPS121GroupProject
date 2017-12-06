package com.groupproject.Controller.SideBarActivities;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.DataBaseItem;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SidebarAdapter extends RecyclerView.Adapter<ViewHolder> implements Filterable {

    private static final DataBaseAPI databaseAPI = DataBaseAPI.getDataBase();
    private final SearchType.Type type;
    private Set<String> invited = new HashSet<>();
    private Set<String> univited = new HashSet<>();
    private List<DataBaseItem> items;
    private final List<DataBaseItem> filteredItemsList;
    private String dataBaseItemID;

    private final Activity activity;
    private UserFilter filter;

    public SidebarAdapter(Activity activity, SearchType.Type type, @Nullable  String dataBaseItemID) {
        super();
        items = new ArrayList<>();
        filteredItemsList = new ArrayList<>();
        this.activity = activity;
        this.type = type;
        this.dataBaseItemID = dataBaseItemID;
    }

    public List<DataBaseItem> getItems() {
        return items;
    }

    void setItems(List<DataBaseItem> items) {
        this.items = items;
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
                holder.selected.setVisibility(View.GONE);

                break;
            case EVENTS:
                //On card click view Event
                holder.interact.setImageDrawable(null);
                holder.selected.setVisibility(View.GONE);
                break;
            case GROUPS:
                holder.interact.setImageDrawable(null);
                holder.selected.setVisibility(View.GONE);
                //On card click view Group
                break;
            case NOTIFICATIONS:
                holder.selected.setVisibility(View.GONE);
                holder.interact.setImageDrawable(ContextCompat.getDrawable(activity, R.drawable.button_accept));
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
            case INVITE:
                User user = (User) dataBaseItem;
                if(user.invitedEventsIDs.contains(dataBaseItemID)|| user.invitedGroupIDs.contains(dataBaseItemID)){
                    holder.selected.setChecked(true);
                }else {
                    holder.selected.setChecked(false);
                }
                holder.interact.setImageDrawable(null);
                holder.selected.setOnCheckedChangeListener((compoundButton, b) -> {
                    boolean userInvited =user.invitedEventsIDs.contains(dataBaseItemID)|| user.invitedGroupIDs.contains(dataBaseItemID);
                    if (b && !userInvited) {
                        invited.add(dataBaseItem.getId());
                        if(univited.contains(dataBaseItem.getId()))
                            univited.remove(dataBaseItem.getId());
                    }else if(!b && userInvited){
                        holder.selected.setChecked(false);
                        univited.add(dataBaseItem.getId());
                        if(invited.contains(dataBaseItem.getId()))
                            invited.remove(dataBaseItem.getId());
                    } else {
//                        invited.remove(dataBaseItem.getId());
                    }
                });
        }
    }

    public Set<String> getInvited() {
        return invited;
    }


    public Set<String> getUnivited() {
        return univited;
    }



    @Override
    public int getItemCount() {
        return items.size();
    }


    @Override
    public Filter getFilter() {
        if(filter == null) {
            filter = new UserFilter(this, items);

        }
        return filter;
    }

    private static class UserFilter extends Filter {

        private final SidebarAdapter adapter;

        private final List<DataBaseItem> originalList;

        private final List<DataBaseItem> filteredList;

        private UserFilter(SidebarAdapter adapter, List<DataBaseItem> originalList) {
            super();
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final DataBaseItem item : originalList) {
                    if (item.getNameLower().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            adapter.filteredItemsList.clear();
            adapter.filteredItemsList.addAll((ArrayList<DataBaseItem>) results.values);
            adapter.setItems((ArrayList<DataBaseItem>) results.values);
            adapter.notifyDataSetChanged();
        }
    }

}
