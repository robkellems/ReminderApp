package com.example.c323finalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ReminderActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextTitle, editTextDesc;
    Button timeButton, dateButton;
    FirebaseDatabase db;
    DatabaseReference dbRef;
    Intent mainIntent;
    int reminderType;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.reminder_action_bar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextDesc = findViewById(R.id.editTextDesc);
        timeButton = findViewById(R.id.timeButton);
        timeButton.setOnClickListener(this);
        dateButton = findViewById(R.id.dateButton);
        dateButton.setOnClickListener(this);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        mainIntent = getIntent();
        reminderType = Integer.parseInt(mainIntent.getStringExtra("TYPE"));
        editTextTitle.setText(mainIntent.getStringExtra("TITLE"));
        editTextDesc.setText(mainIntent.getStringExtra("DESCRIPTION"));
        timeButton.setText(mainIntent.getStringExtra("TIME"));
        dateButton.setText(mainIntent.getStringExtra("DATE"));

    }

    @Override
    public void onBackPressed() {
        String newTitle = editTextTitle.getText().toString();
        String newDesc = editTextDesc.getText().toString();
        String newTime = timeButton.getText().toString();
        String newDate = dateButton.getText().toString();
        Reminder r = new Reminder(newTitle, newDesc, newTime, newDate);

        if (!newTitle.equals("") && !newDesc.equals("")) {
            if (reminderType == 0) {
                String rId = dbRef.push().getKey();
                dbRef.child(rId).setValue(r);
            }

            else if (reminderType == 1) {
                String rId = mainIntent.getStringExtra("KEY");
                dbRef.child(rId).setValue(r);
            }
        }

        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v.equals(findViewById(R.id.timeButton))) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int i, int i1) {
                    int displayHour = i;
                    String displayMin = String.valueOf(i1);
                    String amPm = "AM";

                    if (i > 12) {
                        displayHour -= 12;
                        amPm = "PM";
                    }

                    if (i1 < 10) {
                        displayMin = "0" + displayMin;
                    }

                    timeButton.setText(displayHour + ":" + displayMin + amPm);
                }
            }, 0, 0, false);
            timePickerDialog.show();
        }

        else if (v.equals(findViewById(R.id.dateButton))) {
            final Calendar c = Calendar.getInstance();
            int curYear = c.get(Calendar.YEAR);
            int curMonth = c.get(Calendar.MONTH);
            int curDay = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    dateButton.setText((i1+1) + "-" + i2 + "-" + i);
                }
            }, curYear, curMonth, curDay);
            datePickerDialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (reminderType == 1) {
            String thisKey = mainIntent.getStringExtra("KEY");

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Delete this note?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        //if user selects "OK"...
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //remove note from database and exit activity
                            dbRef.child(thisKey).removeValue();
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        //if user selects "Cancel", do nothing
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {}
                    });
            builder.create().show();
        }

        return true;
    }
}