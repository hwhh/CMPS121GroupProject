package com.groupproject.Controller.GroupActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupproject.Controller.SearchActivities.SearchType;
import com.groupproject.Controller.ViewHolder;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.DataBaseAPI.DataBaseCallBacks;
import com.groupproject.ImageStorage;
import com.groupproject.Model.Event;
import com.groupproject.Model.Group;
import com.groupproject.Model.User;
import com.groupproject.Model.Visibility;
import com.groupproject.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NewGroup extends AppCompatActivity {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private StorageReference mStorageRef;
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    String fileName;
    EditText name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group);

        Button createGroup = findViewById(R.id.createGroup);

        name = findViewById(R.id.GroupNameEdit);
        EditText description = findViewById(R.id.GroupDescriptionEdit);
        EditText tags = findViewById(R.id.GroupTagsEdit);
        ArrayList<String> options = new ArrayList<>();
        options.add("Public");
        options.add("Private");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        Spinner visibility = findViewById(R.id.GroupVisibilitySpinner);
        visibility.setAdapter(adapter);

        //TODO add tags

        createGroup.setOnClickListener(view -> {
            Group g;
            Visibility.VISIBILITY groupVis = (visibility.getSelectedItem().toString().equals("Public")) ?
                    Visibility.VISIBILITY.PUBLIC : Visibility.VISIBILITY.INVITE_ONLY;
            if (name.getText() != null && description.getText() != null) {
                g = new Group(name.getText().toString(), description.getText().toString(), groupVis, dataBaseAPI.getCurrentUserID());
                dataBaseAPI.addGroupToUser(g);//TODO fix this and use a callback
                Intent intent = new Intent(this, ImageStorage.class);
                intent.putExtra("id", g.getId());
                intent.putExtra("type", "groups");
                startActivity(intent);


            } else {
                Toast.makeText(getApplicationContext(), "Please fill in all the data",
                        Toast.LENGTH_LONG).show();
            }
        });


    }


}

