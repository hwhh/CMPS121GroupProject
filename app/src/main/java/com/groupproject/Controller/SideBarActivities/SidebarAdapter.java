package com.groupproject.Controller.SideBarActivities;


import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.List;

public class SidebarAdapter extends RecyclerView.Adapter<SidebarAdapter.ViewHolder>{

    private DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private List<User> users = new ArrayList<>();

    public SidebarAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_result, viewGroup, false);
        return new SidebarAdapter.ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.vName.setText((CharSequence) users.get(position));
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder  {

        TextView vName;
        CardView cardView;
        ImageButton interact;

        ViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtName);
            cardView = v.findViewById(R.id.card_view);
            interact = v.findViewById(R.id.interact);
        }

    }


}
