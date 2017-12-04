package com.groupproject.Controller.GroupActivities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.Group;
import com.groupproject.Model.Visibility;
import com.groupproject.R;

import java.util.ArrayList;

public class NewGroup extends Activity {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_group);

        Button createGroup = findViewById(R.id.createGroup);

        EditText name = findViewById(R.id.GroupNameEdit);
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
            if(name.getText() != null && description.getText() != null){
                g = new Group(name.getText().toString(), description.getText().toString(), groupVis, dataBaseAPI.getCurrentUserID());
                dataBaseAPI.addGroupToUser(g);
            }
            else {
                Toast.makeText(getApplicationContext(), "Please fill in all the data",
                        Toast.LENGTH_LONG).show();
            }
        });

    }


}
