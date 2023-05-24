package com.example.c323finalapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase db;
    DatabaseReference dbRef;
    ArrayList<String> reminderList;
    FloatingActionButton fab;
    RecyclerView recyclerView;
    CustomAdapter customAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_action_bar, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReminderActivity.class);
                intent.putExtra("TYPE", "0");
                intent.putExtra("TITLE", "");
                intent.putExtra("DESCRIPTION", "");
                intent.putExtra("TIME", "Pick Time");
                intent.putExtra("DATE", "Pick Date");
                view.getContext().startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();

        //define behavior for when database is updated
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //create a new list and populate it with all keys currently in database,
                //then use list to create new customAdapter to update the notes being displayed
                reminderList = new ArrayList<String>();
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    reminderList.add(child.getKey());
                }
                customAdapter = new CustomAdapter(reminderList);
                recyclerView.setAdapter(customAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.feedback) {
            String[] addresses = new String[]{ "rkellems@iu.edu" };

            //create implicit intent which opens user's email app with the recipient and subject lines filled out for them
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, addresses);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }

        else if (itemId == R.id.about) {
            System.out.println("in here");
            Uri webpage = Uri.parse("https://luddy.indiana.edu/");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                System.out.println("not null");
                startActivity(intent);
            }
        }

        else if (itemId == R.id.action_add_reminder) {
            Intent intent = new Intent(this, ReminderActivity.class);
            intent.putExtra("TYPE", "0");
            intent.putExtra("TITLE", "");
            intent.putExtra("DESCRIPTION", "");
            intent.putExtra("TIME", "Pick Time");
            intent.putExtra("DATE", "Pick Date");
            this.startActivity(intent);
        }

        return true;
    }
}