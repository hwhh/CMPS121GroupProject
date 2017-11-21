package com.groupproject.Controller;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.CustomLocation;
import com.groupproject.Model.Event;
import com.groupproject.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AddEventActivity extends AppCompatActivity  {

    DataBaseAPI dataBaseAPI = DataBaseAPI.getDataBase();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private EditText name;
    private EditText description;
    private EditText startDate;
    private EditText endDate;
    private Calendar startDateCalendar;
    private Calendar endDateCalendar;
    private DatePickerDialog.OnDateSetListener start_date_picker;
    private DatePickerDialog.OnDateSetListener end_date_picker;
    private Spinner visibility;

    ArrayList<String> options = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LatLng eventLocation;

        if (getIntent().hasExtra("event_location")) {
            eventLocation = getIntent().getExtras().getParcelable("event_location");
        } else {
            throw new IllegalArgumentException("Activity cannot find  extras event_location");
        }
        setContentView(R.layout.create_event);
        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        name = (EditText) findViewById(R.id.name);
        description = (EditText) findViewById(R.id.desc);
        startDate = (EditText) findViewById(R.id.start_date);
        endDate = (EditText) findViewById(R.id.end_date);

        start_date_picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                updateCalendar(startDateCalendar, year, monthOfYear, dayOfMonth);
                endDate.setText("");
                int hour = startDateCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = startDateCalendar.get(Calendar.MINUTE);
                createTimePicker(startDateCalendar, startDate, hour, minute, null);
            }
        };

        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog =  new DatePickerDialog(
                        AddEventActivity.this,
                        start_date_picker,
                        startDateCalendar.get(Calendar.YEAR),
                        startDateCalendar.get(Calendar.MONTH),
                        startDateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        end_date_picker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                updateCalendar(endDateCalendar, year, monthOfYear, dayOfMonth);
                updateCalendarTime(endDateCalendar, startDateCalendar.get(Calendar.HOUR_OF_DAY),
                        startDateCalendar.get(Calendar.MINUTE));
                endDateCalendar.add(Calendar.HOUR, 2);
                int hour = endDateCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = endDateCalendar.get(Calendar.MINUTE);
                createTimePicker(endDateCalendar, endDate, hour, minute, startDateCalendar);
            }
        };

        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            AddEventActivity.this,
                            end_date_picker,
                            endDateCalendar.get(Calendar.YEAR),
                            endDateCalendar.get(Calendar.MONTH),
                            endDateCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getDatePicker().setMinDate(minDate);
                    datePickerDialog.show();
                }
            }
        });

        Button saveButton = (Button) findViewById(R.id.savebutton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allDataEntered()) {

                    String myFormat = "MM/dd/yy hh:mm";
                    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                    try {
                        Date sDate = sdf.parse(startDate.getText().toString());
                        Date fDate = sdf.parse(endDate.getText().toString());
                        Event e;
                        Event.VISIBILITY eventVis = (visibility.getSelectedItem().toString().equals("Public")) ?
                                Event.VISIBILITY.PUBLIC : Event.VISIBILITY.INVITE_ONLY;

                        if (eventLocation != null) {
                            e = new Event(sDate, fDate,
                                    new CustomLocation(eventLocation.latitude, eventLocation.longitude), eventVis,
                                    name.getText().toString(), description.getText().toString(), FirebaseAuth.getInstance().getUid());
                            dataBaseAPI.addEventToUser(firebaseUser, e);
                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), "Please fill in all the data",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        options.add("Public");
        options.add("Private");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, options);
        visibility = (Spinner)findViewById(R.id.visibility);
        visibility.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    private void createTimePicker(final Calendar calendar, final EditText label, int hour, int min,
                                  final Calendar calendarMin) {
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(AddEventActivity.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour,
                                          int selectedMinute) {
                        if (calendarMin != null) {
                            updateCalendarTime(calendar, selectedHour, selectedMinute);
                            if (calendar.getTime().compareTo(calendarMin.getTime()) > 0) {
                                updateLabel(label, calendar, selectedHour,
                                        selectedMinute);
                                updateCalendarTime(calendar, selectedHour, selectedMinute);
                            } else {
                                Toast.makeText(getApplicationContext(),
                                        "End time cannot be before start time",
                                        Toast.LENGTH_LONG).show();
                            }
                        } else {
                            updateLabel(label, calendar, selectedHour,
                                    selectedMinute);
                            updateCalendarTime(calendar, selectedHour, selectedMinute);
                        }
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


}