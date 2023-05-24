package com.example.c323finalapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder>{

    private List<String> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleText, descText, dateText, timeText;
        CardView cardV;
        ImageButton delButton;

        public ViewHolder(View view) {
            super(view);

            titleText = view.findViewById(R.id.titleText);
            descText = view.findViewById(R.id.descText);
            dateText = view.findViewById(R.id.dateText);
            timeText = view.findViewById(R.id.timeText);
            cardV = view.findViewById(R.id.cardV);
            delButton = view.findViewById(R.id.delButton);
        }
    }

    public CustomAdapter(List<String> dataset) { localDataSet = dataset; }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = db.getReference();

        //get the key for the reminder of interest
        String thisKey = localDataSet.get(viewHolder.getAdapterPosition());

        dbRef.child(thisKey).child("title").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.titleText.setText((CharSequence) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });

        dbRef.child(thisKey).child("desc").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.descText.setText((CharSequence) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });

        dbRef.child(thisKey).child("date").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.dateText.setText((CharSequence) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });

        dbRef.child(thisKey).child("time").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.timeText.setText((CharSequence) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("The read failed");
            }
        });

        viewHolder.cardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ReminderActivity.class);
                intent.putExtra("TYPE", "1");
                intent.putExtra("KEY", thisKey);
                intent.putExtra("TITLE", viewHolder.titleText.getText().toString());
                intent.putExtra("DESCRIPTION", viewHolder.descText.getText().toString());
                intent.putExtra("DATE", viewHolder.dateText.getText().toString());
                intent.putExtra("TIME", viewHolder.timeText.getText().toString());
                view.getContext().startActivity(intent);
            }
        });

        viewHolder.delButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Delete this note?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            //if user selects "OK"...
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //remove note from database and exit activity
                                dbRef.child(thisKey).removeValue();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            //if user selects "Cancel", do nothing
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {}
                        });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() { return localDataSet.size(); }
}
