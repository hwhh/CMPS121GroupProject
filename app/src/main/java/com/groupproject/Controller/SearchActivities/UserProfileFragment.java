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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.User;
import com.groupproject.R;

public class UserProfileFragment extends Fragment {

    DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    TextView name;
    TextView email;
    Button interact;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.user_profile_fragment, container, false);

        name = rootView.findViewById(R.id.nameTextView);
        email = rootView.findViewById(R.id.emailTextView);

        Bundle bundle = getArguments();

        Query query = dataBaseAPI.getmUserRef().orderByChild("id").equalTo((String) bundle.get("id"));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot users: dataSnapshot.getChildren()) {
                        User user = users.getValue(com.groupproject.Model.User.class);
                        name.setText(user.getName());
                        email.setText(user.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;

    }
}
