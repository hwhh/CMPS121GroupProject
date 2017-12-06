package com.groupproject.Controller.EventActivities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.groupproject.Controller.SideBarActivities.SidebarFragment;
import com.groupproject.DataBaseAPI.DataBaseAPI;
import com.groupproject.Model.CustomLocation;
import com.groupproject.Model.Event;
import com.groupproject.Model.Visibility;
import com.groupproject.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateEventActivity extends Fragment {

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

    ArrayList<String> options = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_create, container, false);

        Bundle bundle = getArguments();
        LatLng eventLocation = bundle.getParcelable("event_location");


        startDateCalendar = Calendar.getInstance();
        endDateCalendar = Calendar.getInstance();
        name = rootView.findViewById(R.id.name);
        description = rootView.findViewById(R.id.desc);
        startDate = rootView.findViewById(R.id.start_date);
        endDate = rootView.findViewById(R.id.end_date);


        start_date_picker = (view, year, monthOfYear, dayOfMonth) -> {
            updateCalendar(startDateCalendar, year, monthOfYear, dayOfMonth);
            endDate.setText("");
            int hour = startDateCalendar.get(Calendar.HOUR_OF_DAY);
            int minute = startDateCalendar.get(Calendar.MINUTE);
            createTimePicker(startDateCalendar, startDate, hour, minute, null);
        };

        startDate.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog =  new DatePickerDialog(
                  getActivity(),
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
                        getActivity(),
                        end_date_picker,
                        endDateCalendar.get(Calendar.YEAR),
                        endDateCalendar.get(Calendar.MONTH),
                        endDateCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate);
                datePickerDialog.show();
            }
        });


        Button inviteButton = rootView.findViewById(R.id.invitebutton);
        inviteButton.setOnClickListener(view -> {
            Bundle newBundle = new Bundle();
            newBundle.putString("type", "friends");
            SidebarFragment sidebarFragment = new SidebarFragment();
            sidebarFragment.setArguments(newBundle);
            getFragmentManager().beginTransaction().replace(R.id.dashboard_content, sidebarFragment, "sidebar").addToBackStack(null).commit();

        });

        Button saveButton = rootView.findViewById(R.id.savebutton);
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

                        Bundle newBundle = new Bundle();
                        Intent intent = new Intent(getActivity(), EventInfoActivity.class);
                        newBundle.putString("key", e.getId());
                        startActivity(intent);
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getActivity(), "Please fill in all the data",
                        Toast.LENGTH_LONG).show();
            }
        });

        options.add("Public");
        options.add("Private");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
        visibility = rootView.findViewById(R.id.visibility);
        visibility.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return rootView;
    }


    private void createTimePicker(final Calendar calendar, final EditText label, int hour, int min,
                                  final Calendar calendarMin) {
        TimePickerDialog timePickerDialog;
        timePickerDialog = new TimePickerDialog(getActivity(),
                (timePicker, selectedHour, selectedMinute) -> {
                    if (calendarMin != null) {
                        updateCalendarTime(calendar, selectedHour, selectedMinute);
                        if (calendar.getTime().compareTo(calendarMin.getTime()) > 0) {
                            updateLabel(label, calendar, selectedHour,
                                    selectedMinute);
                            updateCalendarTime(calendar, selectedHour, selectedMinute);
                        } else {
                            Toast.makeText(getActivity(),
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


}