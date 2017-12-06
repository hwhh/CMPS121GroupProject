package com.groupproject.Controller.GroupActivities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Group;
import com.groupproject.Model.Visibility;
import com.groupproject.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class NewGroup extends AppCompatActivity {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    private StorageReference mStorageRef;
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private InputStream image;
    EditText name;
    ImageButton upload;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group);

        Button createGroup = findViewById(R.id.createGroup);
        upload = findViewById(R.id.upload);

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

        mStorageRef = FirebaseStorage.getInstance().getReference();

        createGroup.setOnClickListener(view -> {
            Group g;
            Visibility.VISIBILITY groupVis = (visibility.getSelectedItem().toString().equals("Public")) ?
                    Visibility.VISIBILITY.PUBLIC : Visibility.VISIBILITY.INVITE_ONLY;
            if (name.getText() != null && description.getText() != null) {
                g = new Group(name.getText().toString(), description.getText().toString(), groupVis, dataBaseAPI.getCurrentUserID());
                dataBaseAPI.addGroupToUser(g);//TODO fix this and use a callback
                StorageReference groupRef = mStorageRef.child(g.getId()+".jpg");
                UploadTask uploadTask = groupRef.putStream(image);
                uploadTask.addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                });
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Please fill in all the data",
                        Toast.LENGTH_LONG).show();
            }
        });

        upload.setOnClickListener(view -> {
            pickImage();
        });


    }

    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                //e.g. create user, then change "images" to where i was called from
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                image = inputStream;
                Toast.makeText(getApplicationContext(), "Image uploaded.", Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }


}

