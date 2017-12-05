package com.groupproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by haileypun on 05/12/2017.
 */

public class ImageStorage extends AppCompatActivity {

    private StorageReference mStorageRef;
    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    private String id, group;
    ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload);
        Bundle b = getIntent().getExtras();
        this.id = b.getString("id");
        this.group = b.getString("type");
        mStorageRef = FirebaseStorage.getInstance().getReference();
        imageButton = (ImageButton) findViewById(R.id.uploadImage);
        imageButton.setOnClickListener(view -> {
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
                //TODO make this into separate class, in the arguments to on create, find out which activity called it
                //e.g. create user, then chnage "images" to where i was called from
                InputStream inputStream = getApplicationContext().getContentResolver().openInputStream(data.getData());
                StorageReference groupRef = mStorageRef.child(id+".jpg");
                UploadTask uploadTask = groupRef.putStream(inputStream);
                uploadTask.addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                }).addOnSuccessListener(taskSnapshot -> {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }
}
