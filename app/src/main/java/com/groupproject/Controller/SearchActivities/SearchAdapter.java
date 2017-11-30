package com.groupproject.Controller.SearchActivities;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.groupproject.Model.User;
import com.groupproject.R;


public class SearchAdapter extends FirebaseRecyclerAdapter<User, SearchAdapter.UserViewHolder> {


    SearchAdapter(Query query) {
        super(new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build());
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.search_result, viewGroup, false);

        return new UserViewHolder(itemView);
    }

    @Override
    protected void onBindViewHolder(UserViewHolder holder, int position, User model) {
        holder.vName.setText(model.getName());
        holder.vEmail.setText(model.getEmail());
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView vName;
        TextView vEmail;

        UserViewHolder(View v) {
            super(v);
            vName = v.findViewById(R.id.txtName);
            vEmail = v.findViewById(R.id.txtEmail);
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
// FirebaseRecyclerAdapter<User, SearchAdapter.UserViewHolder>{
//
//    private List<User> users;
//
//    SearchAdapter(Query query) {
//        super(new FirebaseRecyclerOptions.Builder<User>().setQuery(query, User.class).build());
//        users = new ArrayList<>();
//    }
//
//    void setUsers() {
//        users = new ArrayList<>();
//    }
//
//    List<User> getUsers() {
//        return users;
//    }
//
//    @Override
//    protected void onBindViewHolder(UserViewHolder holder, int position, User model) {
//        holder.vName.setText(model.getName());
//        holder.vEmail.setText(model.getEmail());
//    }
//
//    @Override
//    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result, parent, false);
//        return new UserViewHolder(view);
//    }
//
//    @Override
//    public int getItemCount() {
//        return users.size();
//    }
//
//    static class UserViewHolder extends RecyclerView.ViewHolder {
//
//        TextView vName;
//        TextView vEmail;
//
//        UserViewHolder(View v) {
//            super(v);
//            vName = v.findViewById(R.id.txtName);
//            vEmail = v.findViewById(R.id.txtEmail);
//        }
//    }
//
//    @Override
//    public void onDataChanged() {
//        System.out.println("here");
//
//        // Called each time there is a new data snapshot. You may want to use this method
//        // to hide a loading spinner or check for the "no documents" state and update your UI.
//        // ...
//    }
//
//    @Override
//    public void onError(DatabaseError e) {
//        // Called when there is an error getting data. You may want to update
//        // your UI to display an error message to the user.
//        // ...
//    }
//
//}

