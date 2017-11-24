package com.groupproject.Controller.SearchActivities;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.firebase.ui.database.FirebaseRecyclerOptions;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.groupproject.Model.User;
import com.groupproject.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.UserViewHolder> {

    private List<User> users;

    Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child("users")
            .child("name")
            .limitToFirst(0);


//    FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
//            .setQuery(query, User.class)
//            .build();

    public void setQuery(Query query) {
        this.query = query;
    }

    SearchAdapter() {
        users = new ArrayList<>();
    }


    void addItem(User user){
        users.add(user);
        System.out.println(users.size());
    }

    @Override
    public SearchAdapter.UserViewHolder onCreateViewHolder(ViewGroup viewGroup,int viewType) {

        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.search_result, viewGroup, false);

        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserViewHolder userViewHolder, int i) {
        User user = users.get(i);
        userViewHolder.vName.setText(user.getName());
        userViewHolder.vEmail.setText(user.getEmail());
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return users.size();
    }


    static class UserViewHolder extends RecyclerView.ViewHolder {

        TextView vName;
        TextView vEmail;

        UserViewHolder(View v) {
            super(v);
            vName =  (TextView) v.findViewById(R.id.txtName);
            vEmail = (TextView)  v.findViewById(R.id.txtEmail);
        }
    }

//    FirebaseRecyclerAdapter adapter = new FirebaseRecyclerAdapter<User, UserViewHolder>(options) {
//        @Override
//        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            // Create a new instance of the ViewHolder, in this case we are using a custom
//            // layout called R.layout.message for each item
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.search_result, parent, false);
//
//            return new UserViewHolder(view);
//        }
//
//        @Override
//        protected void onBindViewHolder(UserViewHolder holder, int position, User model) {
//            // Bind the Chat object to the ChatHolder
//            // ...
//        }
//    };
}
