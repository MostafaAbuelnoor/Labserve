package com.project.labserve;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.labserve.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LabReservation extends Activity implements OnClickListener {

    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference bigparent;
    DatabaseReference labs;
    DatabaseReference databaseReference;
    private TextView reservationLabName, reservationLabBuilding, confirmReservation;
    private Button sunButton, monButton, tueButton, wedButton, thurButton, reserveButton;
    private ListView reservationListView;
    private ArrayList<String> days;
    String TAG = "LabReservation";
    String dayselected;
    String slotselected;
    String labkey;
    Boolean alreadyreserved;
    String uid;
    String temp;
    int ind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labreservation);

        ind = -1;
        firebaseDatabase = FirebaseDatabase.getInstance();
        bigparent = firebaseDatabase.getReference("Labserve").child("Labs");
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        dayselected = "";
        slotselected = "";

        reservationLabName = (TextView)findViewById(R.id.reservationLabName);
        reservationLabBuilding = (TextView)findViewById(R.id.reservationLabBuilding);
        confirmReservation = (TextView)findViewById(R.id.confirmReservation);
        reservationListView = (ListView) findViewById(R.id.reservationListView);
        sunButton = (Button)findViewById(R.id.sunButton);
        monButton = (Button)findViewById(R.id.monButton);
        tueButton = (Button)findViewById(R.id.tueButton);
        wedButton = (Button) findViewById(R.id.wedButton);
        thurButton = (Button)findViewById(R.id.thurButton);
        reserveButton = (Button)findViewById(R.id.reserveButton);

        reserveButton.setOnClickListener(this);
        sunButton.setOnClickListener(this);
        monButton.setOnClickListener(this);
        tueButton.setOnClickListener(this);
        wedButton.setOnClickListener(this);
        thurButton.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        uid = firebaseUser.getUid();
        TextView show = findViewById(R.id.textView2);
        Log.d(TAG, "Current user is: " + firebaseUser.getUid() + " " + firebaseUser.getEmail());

        labkey = "";
        if(reservationLabName.getText().toString().contains("ESB 1043")){
            labkey = "lab1";
        }
        else if(reservationLabName.getText().toString().contains("EB2 103")){
            labkey = "lab2";
        }
        else if(reservationLabName.getText().toString().contains("IC 2")){
            labkey = "lab3";
        }
        else if(reservationLabName.getText().toString().contains("ART 103")){
            labkey = "lab4";
        }

        if(extras!=null) {
            reservationLabName.setText(extras.getString("labName"));
            reservationLabBuilding.setText(extras.getString("labBuilding"));
            days = new ArrayList<String>();
            days = extras.getStringArrayList("allDaysAllSlots");
        }

        reservationListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                ind = position+1;
                slotselected = ((TextView) view.findViewById(R.id.slot)).getText().toString();
                confirmReservation.setText(dayselected + " - " + slotselected.toLowerCase());
                confirmReservation.setTextColor(Color.parseColor("#2d3436"));
            }
        });

    }

    public void setListView(int i){

        String [] slots = days.get(i).split(" "); // get day in question and slit slots
        String showtimings = "";
        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
        for(String slot : slots){
            HashMap<String, String> map = new HashMap<String, String>();
            if(!slot.isEmpty()) {

                showtimings = "From " + slot.split("-")[0] + " to " + slot.split("-")[1];

            }
            else{
                showtimings = "No free lab slots on this day";
            }
            map.put("slot", showtimings);
            data.add(map);
        }

        int resource = R.layout.lab_list_view;
        String[] from = {"slot"};
        int[] to = { R.id.slot};

        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        reservationListView.setAdapter(adapter);
        reservationListView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.sunButton:
                dayselected = "Sunday";
                sunButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape2));
                monButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                tueButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                wedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                thurButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                setListView(0);
                break;
            case R.id.monButton:
                dayselected = "Monday";
                sunButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                monButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape2));
                tueButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                wedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                thurButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                setListView(1);
                break;

            case R.id.tueButton:
                dayselected = "Tuesday";
                sunButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                monButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                tueButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape2));
                wedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                thurButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                setListView(2);
                break;
            case R.id.wedButton:
                dayselected = "Wednesday";
                sunButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                monButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                tueButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                wedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape2));
                thurButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                setListView(3);
                break;
            case R.id.thurButton:
                dayselected = "Thursday";
                sunButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                monButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                tueButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                wedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                thurButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape2));
                setListView(4);
                break;
            case R.id.reserveButton:
                databaseReference = firebaseDatabase.getReference("Labserve").child("Users").child("Faculty").child(uid);
                labs = firebaseDatabase.getReference("Labserve").child("Labs").child(labkey);
                labs.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String get = dataSnapshot.child(dayselected + "_" + ind).getValue(String.class);
                        temp = get;
                        alreadyreserved = (get.toLowerCase().contains("reserved"))? true : false;
                        if (alreadyreserved) {
                            Toast.makeText(getApplicationContext(), "This slot is already reserved", Toast.LENGTH_LONG).show();
                        } else {
                            databaseReference.child("reservation").setValue("reserved " + labkey + " " + dayselected + "_" + ind);
                            labs.child(dayselected+"_"+ind).setValue(temp + " reserved");
                            Toast.makeText(getApplicationContext(), "Reservation successful", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                break;
            default:
                sunButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                monButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                tueButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                wedButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
                thurButton.setBackground(ContextCompat.getDrawable(this, R.drawable.shape3));
        }

    }

    /*@Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // from https://developer.android.com/reference/android/widget/AdapterView.OnItemClickListener
        slotselected = ((TextView) view.findViewById(R.id.slot)).getText().toString();
        Toast.makeText(this, slotselected, Toast.LENGTH_SHORT).show();
        confirmReservation.setText(dayselected + " - " + slotselected.toLowerCase());
        confirmReservation.setTextColor(Color.parseColor("#00b894"));
    }*/
}
