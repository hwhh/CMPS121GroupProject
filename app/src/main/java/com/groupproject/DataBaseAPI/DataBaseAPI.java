package com.groupproject.DataBaseAPI;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.groupproject.Model.User;

import java.util.HashMap;
import java.util.Map;


public class DataBaseAPI {

    private FirebaseFirestore db;
    private FirebaseUser currentUser;
    private DatabaseReference ref;


    private static DataBaseAPI single_instance = null;


    private DataBaseAPI(){
        db = FirebaseFirestore.getInstance();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference();
    }

    public static DataBaseAPI DataBaseAPI() {
        if (single_instance == null){
            single_instance = new DataBaseAPI();
        }
        return single_instance;
    }



    public void updateElement(String collection, String child) {
        ref.child(collection).child(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // use "username" already exists
                } else {
                    // "username" does not exist yet.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }






}



















//
//        Map<String, Object> user = new HashMap<>();
//        user.put("id", currentUser.getUid());
//        user.put("name", currentUser.getDisplayName());
//        user.put("email", currentUser.getEmail());
//
//        db.collection("users")
//                .document("some id")
//                .set(user);
//    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());


//    Firebase m_objFireBaseRef = new Firebase(AppConstants.FIREBASE_URL);
//    Firebase objRef = m_objFireBaseRef.child("tasks");
//    Query pendingTasks = objRef.orderByChild("Status").equalTo("PENDING");
//    pendingTasks.addListenerForSingleValueEvent(new ValueEventListener() {
//        @Override
//        public void onDataChange(DataSnapshot tasksSnapshot) {
//            for (DataSnapshot snapshot: tasksSnapshot.getChildren()) {
//                snapshot.getRef().child("Status").setValue("COMPLETED");
//            }
//        }
//        @Override
//        public void onCancelled(FirebaseError firebaseError) {
//            System.out.println("The read failed: " + firebaseError.getMessage());
//        }
//    });
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