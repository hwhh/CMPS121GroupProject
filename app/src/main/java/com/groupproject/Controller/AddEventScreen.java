package com.groupproject.Controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.groupproject.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by haileypun on 31/10/2017.
 */

public class AddEventScreen extends AppCompatActivity {

    ArrayList<String> years = new ArrayList<>();
    ArrayList<String> days = new ArrayList<>();
    ArrayList<String> months = new ArrayList<>();
    ArrayList<String> hours = new ArrayList<>();
    ArrayList<String> minutes = new ArrayList<>();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        dropdownYear();
        dropdownDay();
        dropdownMonth();
        dropdownHour();
        dropdownMinute();
    }

    public void dropdownYear(){

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        for(int i = 1900; i <= currentYear; i++ ){

            years.add(Integer.toString(i));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);

        Spinner selectYear = (Spinner)findViewById(R.id.Year);

        selectYear.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void dropdownDay(){

        int currentDays = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        for(int i = 1; i <= currentDays; i++ ){

            days.add(Integer.toString(i));

        }

        days.add("29");
        days.add("30");
        days.add("31");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, days);

        Spinner selectDay = (Spinner)findViewById(R.id.Day);

        selectDay.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void dropdownMonth(){

        months.add("JAN");
        months.add("FEB");
        months.add("MAR");
        months.add("APR");
        months.add("MAY");
        months.add("JUN");
        months.add("JUL");
        months.add("AUG");
        months.add("SEP");
        months.add("OCT");
        months.add("NOV");
        months.add("DEC");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, months);

        Spinner selectMonth = (Spinner)findViewById(R.id.Month);

        selectMonth.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void dropdownHour(){

        for(int i = 0; i <= 9; i++){

            hours.add("0"+Integer.toString(i));

        }

        for(int i = 10; i <= 23; i++ ){

            hours.add(Integer.toString(i));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, hours);

        Spinner selectHour = (Spinner)findViewById(R.id.Hours);

        selectHour.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }

    public void dropdownMinute(){

        for(int i = 0; i <= 9; i++){

            minutes.add("0"+Integer.toString(i));

        }

        for (int j = 10; j <= 59; j++){

            minutes.add(Integer.toString(j));

        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, minutes);

        Spinner selectMinute = (Spinner)findViewById(R.id.Minutes);

        selectMinute.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    }
}