package com.groupproject.Controller.EventActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.CustomLocation;
import com.groupproject.Model.Event;
import com.groupproject.Model.Visibility;
import com.groupproject.R;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    private static final DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();

    private EditText name;
    private EditText description;
    private EditText startDate;
    private EditText endDate;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private DatePickerDialog.OnDateSetListener start_date_picker;
    private DatePickerDialog.OnDateSetListener end_date_picker;
    private Spinner visibility;
    private ImageButton upload;
    private StorageReference mStorageRef;
    private InputStream image;
    private static final int PICK_PHOTO_FOR_AVATAR = 0;

    ArrayList<String> options = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LatLng eventLocation;

        if (getIntent().hasExtra("event_location")) {
            eventLocation = getIntent().getExtras().getParcelable("event_location");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras event_location");
        }
        setContentView(R.layout.event_create);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        setContentView(R.layout.event_create);
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        name =findViewById(R.id.name);
        description =findViewById(R.id.desc);
        startDate =findViewById(R.id.start_date);
        endDate =findViewById(R.id.end_date);

        name = findViewById(R.id.name);
        description = findViewById(R.id.desc);
        startDate = findViewById(R.id.start_date);
        endDate = findViewById(R.id.end_date);
        upload = findViewById(R.id.uploadEvent);
        upload.setOnClickListener(view -> {
            pickImage();
        });

        start_date_picker = (view, year, monthOfYear, dayOfMonth) -> {
            updateCalendar(startDateCalendar, year, monthOfYear, dayOfMonth);
            endDate.setText("");
            int hour = startDateCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = startDateCalendar.get(Calendar.MINUTE);
            createTimePicker(startDateCalendar, startDate, hour, minute, null);
        };

        startDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog =  new DatePickerDialog(
                    this,
                    start_date_picker,
                    startDateCalendar.get(Calendar.YEAR),
                    startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            datePickerDialog.show();
        });

        end_date_picker = (view, year, monthOfYear, dayOfMonth) -> {
            updateCalendar(endDateCalendar, year, monthOfYear, dayOfMonth);
            updateCalendarTime(endDateCalendar, startDateCalendar.get(Calendar.HOUR_OF_DAY),
                    startDateCalendar.get(Calendar.MINUTE));
            endDateCalendar.add(Calendar.HOUR, 2);
            int hour = endDateCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = endDateCalendar.get(Calendar.MINUTE);
            createTimePicker(endDateCalendar, endDate, hour, minute, startDateCalendar);
        };

        endDate.setOnClickListener(v -> {
            if (!startDate.getText().toString().trim().isEmpty()) {
                //Get start date's date and assign in to the end date calendar
                DateFormat format = new SimpleDateFormat("MM/dd/yy", Locale.US);
                Date date = null;
                try {
                    date = format.parse(startDate.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date != null)
                    endDateCalendar.setTime(date);
                long minDate = endDateCalendar.getTimeInMillis();
                DatePickerDialog datePickerDialog =  new DatePickerDialog(
                        this,
                        end_date_picker,
                        endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.show();
            }
        });

        Button saveButton = findViewById(R.id.savebutton);
        saveButton.setOnClickListener(v -> {
            if (allDataEntered()) {

                String myFormat = "MM/dd/yy hh:mm";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                try {
                    Date sDate = sdf.parse(startDate.getText().toString());
                    Date fDate = sdf.parse(endDate.getText().toString());
                    Event e;
                    Visibility.VISIBILITY eventVis = (visibility.getSelectedItem().toString().equals("Public")) ?
                            Visibility.VISIBILITY.PUBLIC : Visibility.VISIBILITY.INVITE_ONLY;

                    if (eventLocation != null) {
                        e = new Event(sDate, fDate,
                                new CustomLocation(eventLocation.latitude, eventLocation.longitude), eventVis,
                                name.getText().toString(), description.getText().toString(), dataBaseAPI.getCurrentUserID());
                        dataBaseAPI.addEventToUser(e);

                        Intent intent = new Intent(this, EventInfoActivity.class);
                        intent.putExtra("key", e.getId());
                        startActivity(intent);
                        StorageReference groupRef = mStorageRef.child(e.getId()+".jpg");
                        if (image != null) {
                            UploadTask uploadTask = groupRef.putStream(image);
                            uploadTask.addOnFailureListener(exception -> {
                                // Handle unsuccessful uploads
                            }).addOnSuccessListener(taskSnapshot -> {
                                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            });
                        }
                        finish();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, "Please fill in all the data",
                        Toast.LENGTH_LONG).show();
            }
        });

        options.add("Public");
        options.add("Private");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        visibility = findViewById(R.id.visibility);
        visibility.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    private void createTimePicker(final Calendar calendar, final EditText label, int hour, int min,
                                  final Calendar calendarMin) {
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(this,
                (timePicker, selectedHour, selectedMinute) -> {
                    if (calendarMin != null) {
                        updateCalendarTime(calendar, selectedHour, selectedMinute);
                        if (calendar.getTime().compareTo(calendarMin.getTime()) > 0) {
                            updateLabel(label, calendar, selectedHour,
                                    selectedMinute);
                            updateCalendarTime(calendar, selectedHour, selectedMinute);
                        } else {
                            Toast.makeText(this,
                                    "End time cannot be before start time",
                                    Toast.LENGTH_LONG).show();
                        }
                    } else {
                        updateLabel(label, calendar, selectedHour,
                                selectedMinute);
                        updateCalendarTime(calendar, selectedHour, selectedMinute);
                    }
                }, hour, min, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    private void updateCalendarTime(Calendar calendar, int hour, int min) {
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
    }

    private void updateCalendar(Calendar calendar, int year, int month, int day) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    private void updateLabel(EditText label, Calendar calendar, int hour, int min) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String hourString = hour + "", minString = min + "";
        if (hour == 0)
            hourString = "00";
        if (min == 0)
            minString = "00";
        String text = sdf.format(calendar.getTime()) + " " + hourString + ":" + minString;
        label.setText(text);
    }

    private boolean allDataEntered() {
        return isEditTextEmpty(name) && isEditTextEmpty(description) &&
                isEditTextEmpty(startDate) && isEditTextEmpty(endDate);
    }

    private boolean isEditTextEmpty(EditText editText) {
        return !editText.getText().toString().trim().isEmpty();
    }

    public void dropdownOption(){


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