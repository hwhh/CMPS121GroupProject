package com.groupproject.Controller.SearchActivities;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.User;
import com.groupproject.R;

public class UserProfileFragment extends Fragment implements View.OnClickListener, ValueEventListener{

    DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    TextView name;
    TextView email;
    Button interact;
    private User user;
    private FirebaseUser currentUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.user_profile_fragment, container, false);
        name = rootView.findViewById(R.id.nameTextView);
        email = rootView.findViewById(R.id.emailTextView);
        Bundle bundle = getArguments();
        Query query = dataBaseAPI.getmUserRef().orderByChild("id").equalTo((String) bundle.get("id"));
        query.addListenerForSingleValueEvent(this);
        interact = rootView.findViewById(R.id.interactFloatingButton);
        interact.setOnClickListener(this);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        return rootView;

    }

    public void createButton(boolean clicked){
//        if(user.getId().equals(currentUser.getUid())) {
//            interact.setVisibility(View.INVISIBLE);
//        }else if(user.getFriendsIDs().contains(currentUser.getUid())){
//            interact.setText("Remove Friend");
//            if(clicked){
//                dataBaseAPI.getmUserRef().child(currentUser.getUid()).child("friendsIDs").child(user.getId()).removeValue();
//                dataBaseAPI.getmUserRef().child(user.getId()).child("friendsIDs").child(currentUser.getUid()).removeValue();
//                interact.setText("User Removed");
//            }
//        }else if(user.getRequestsID().contains(currentUser.getUid())){
//            interact.setText("Delete Request");
//            if(clicked){
//                dataBaseAPI.getmUserRef().child(user.getId()).child("requestsID").child(currentUser.getUid()).removeValue();
//                interact.setText("Request Deleted");
//            }
//        }else{
//            interact.setText("Add Friend");
//            if(clicked){
//                String key = dataBaseAPI.getmUserRef().child(user.getId()).child("requestsID").push().getKey();
//                dataBaseAPI.getmUserRef().child(user.getId()).child("requestsID").child(key).setValue(currentUser.getUid());
//                interact.setText("Request Sent");
//            }
//        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.interactFloatingButton){
            createButton(true);
        }
    }

    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot users: dataSnapshot.getChildren()) {
                user = users.getValue(com.groupproject.Model.User.class);
                name.setText(user.getName());
                email.setText(user.getEmail());
                createButton(false);
            }
        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
}
