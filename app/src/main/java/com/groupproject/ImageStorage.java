package com.groupproject;

import android.net.Uri;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;

/**
 * Created by haileypun on 05/12/2017.
 */

public class ImageStorage {

    private StorageReference mStorageRef;
    private String id, group;
    private InputStream image;


    public ImageStorage(String id, InputStream image){
        this.id = id;
        this.image = image;

    }

    public void storeImage(){
        StorageReference groupRef = mStorageRef.child(id+".jpg");
        UploadTask uploadTask = groupRef.putStream(image);
        uploadTask.addOnFailureListener(exception -> {
            // Handle unsuccessful uploads
        }).addOnSuccessListener(taskSnapshot -> {
            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
        });
    }
}
