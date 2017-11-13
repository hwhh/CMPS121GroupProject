package com.groupproject.DataBaseAPI;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupproject.Model.User;

import java.util.HashMap;
import java.util.Map;


public class DataBaseAPI {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());


//    ref.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot dataSnapshot) {
//            final Profile tempProfile = new Profile(); //this is my user_class Class
//            final Field[] fields = tempProfile.getClass().getDeclaredFields();
//            for(Field field : fields){
//                Log.i(TAG, field.getName() + ": " + dataSnapshot.child(field.getName()).getValue());
//            }
//        }
//
//        @Override
//        public void onCancelled(DatabaseError databaseError) {
//        }
//    });

    public void addUser(){
        System.out.println(currentUser.getUid());

        User user = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
        db.collection("users").document(currentUser.getUid()).set(user);


//
//        Map<String, Object> user = new HashMap<>();
//        user.put("id", currentUser.getUid());
//        user.put("name", currentUser.getDisplayName());
//        user.put("email", currentUser.getEmail());
//
//        db.collection("users")
//                .document("some id")
//                .set(user);


    }

}
