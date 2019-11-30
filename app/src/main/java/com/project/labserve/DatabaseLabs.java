package com.project.labserve;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DatabaseLabs {

    LabList labList;
    //private LabEntry labEntry;
    private FirebaseDatabase db;
    private DatabaseReference db_ref;
    String TAG = "DatabaseLabs";
    boolean isRead;


    DatabaseLabs (){
        isRead = false;
        this.db = FirebaseDatabase.getInstance();
        this.db_ref = db.getReference("Labserve").child("Labs");
        this.labList = new LabList();

        isRead = readLabs();
    }

    DatabaseLabs (DatabaseReference db_ref){
        this.db = db;
        this.db_ref = db_ref;
        this.labList = new LabList();
        this.db_ref.child("lab0").setValue(Math.random());
        Log.d(TAG, "Constructor called successfully");

        isRead = readLabs();

        //Log.d(TAG, "Database read");
    }

    /*public void ReadSnapshot(){
        Log.d(TAG, "ReadSnapshot function");
        Log.d(TAG, "entered list of labs for loop ");
        if(ds.exists()) {
            for (DataSnapshot lab : ds.getChildren()) { // iterate through lab1, lab2, etc
                if (lab.getKey() != "lab0") {
                    LabEntry labEntry = new LabEntry();
                    //set name and building location of lab
                    labEntry.setLabName(lab.child("labName").getValue().toString());
                    Log.d(TAG, labEntry.getLabName());
                    labEntry.setLabBuilding(lab.child("labBuilding").getValue().toString());

                    //get all days of the week of lab: Sunday, Monday...
                    Iterable<DataSnapshot> labTimings = lab.child("labTimings").getChildren();
                    for (DataSnapshot day : labTimings) { // iterate through Sunday, Monday, etc
                        Log.d(TAG, "entered days for loop " + day.getKey().toString());
                        LabSlot labSlot = new LabSlot();

                        // get all the slots for a certain day: 1,2,...
                        Iterable<DataSnapshot> slots = day.getChildren();
                        if (Integer.parseInt(day.child("noSlots").getValue().toString()) != 0) {
                            for (DataSnapshot oneSlot : slots) { // iterate through slot 1, 2, ...
                                Log.d(TAG, "entered slots for loop " + oneSlot.getKey().toString());
                                if (oneSlot.child("from").exists()) {
                                    labSlot.setFrom(oneSlot.child("from").getValue().toString());
                                    labSlot.setTo(oneSlot.child("to").getValue().toString());
                                }
                                labEntry.setSlot(day.getKey().toString().toLowerCase(), labSlot);
                            }
                        }
                    }
                    Log.d(TAG, "reading lab done, adding to list");
                    labList.addLab(labEntry);
                }
            }
        }

        else{
            Log.d(TAG, "ds does not exist");
        }
    }
*/

    public void readThrough(DataSnapshot dataSnapshot){
        Log.d(TAG, "entered onDataChangedFunction");
        Iterable<DataSnapshot> allChildren = dataSnapshot.getChildren(); // get all labs: lab1, lab2...
        Log.d(TAG, "entered list of labs for loop ");
        for(DataSnapshot lab : allChildren) { // iterate through lab1, lab2, etc
            if (lab.getKey() != "lab0") {
                LabEntry labEntry = new LabEntry();
                //set name and building location of lab
                labEntry.setLabName(lab.child("labName").getValue().toString());
                Log.d(TAG, labEntry.getLabName());
                labEntry.setLabBuilding(lab.child("labBuilding").getValue().toString());

                //get all days of the week of lab: Sunday, Monday...
                Iterable<DataSnapshot> labTimings = lab.child("labTimings").getChildren();
                for (DataSnapshot day : labTimings) { // iterate through Sunday, Monday, etc
                    Log.d(TAG, "entered days for loop " + day.getKey().toString());
                    LabSlot labSlot = new LabSlot();

                    // get all the slots for a certain day: 1,2,...
                    Iterable<DataSnapshot> slots = day.getChildren();
                    if (Integer.parseInt(day.child("noSlots").getValue().toString()) != 0) {
                        for (DataSnapshot oneSlot : slots) { // iterate through slot 1, 2, ...
                            Log.d(TAG, "entered slots for loop " + oneSlot.getKey().toString());
                            if (oneSlot.child("from").exists()) {
                                labSlot.setFrom(oneSlot.child("from").getValue().toString());
                                labSlot.setTo(oneSlot.child("to").getValue().toString());
                            }
                            labEntry.setSlot(day.getKey().toString().toLowerCase(), labSlot);
                        }
                    }
                }
                Log.d(TAG, "reading lab done, adding to list");
                labList.addLab(labEntry);
            }
        }
    }


    public boolean readLabs(){
        db_ref.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "entered onDataChangedFunction");
                 // get all labs: lab1, lab2...
                LabEntry labEntry = new LabEntry();

                Iterable<DataSnapshot> allChildren = dataSnapshot.child("lab1").getChildren();
                for(DataSnapshot element : allChildren){
                    if(element.getKey().contains("labName")){
                        labEntry.setLabName(element.getValue(String.class));
                    }
                    else if(element.getKey().contains("labBuilding")){
                        labEntry.setLabBuilding(element.getValue(String.class));
                    }
                    else if(element.getKey().contains("Sunday")){
                        String temp = element.getValue(String.class);
                        if(!temp.contains("0")) {
                            LabSlot slot = new LabSlot(temp);
                            labEntry.setSlot("sunday", slot);
                        }
                    }
                    else if(element.getKey().contains("Monday")){
                        String temp = element.getValue(String.class);
                        if(!temp.contains("0")) {
                            LabSlot slot = new LabSlot(temp);
                            labEntry.setSlot("monday", slot);
                        }
                    }
                    else if(element.getKey().contains("Tuesday")){
                        String temp = element.getValue(String.class);
                        if(!temp.contains("0")) {
                            LabSlot slot = new LabSlot(temp);
                            labEntry.setSlot("tuesday", slot);
                        }
                    }
                    else if(element.getKey().contains("Wednesday")){
                        String temp = element.getValue(String.class);
                        if(!temp.contains("0")) {
                            LabSlot slot = new LabSlot(temp);
                            labEntry.setSlot("wednesday", slot);
                        }
                    }
                    else if(element.getKey().contains("Thursday")){
                        String temp = element.getValue(String.class);
                        if(!temp.contains("0")) {
                            LabSlot slot = new LabSlot(temp);
                            labEntry.setSlot("thursday", slot);
                        }
                    }
                }

                    labList.addLab(labEntry);
                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("Reading Labs from db", "Failed to read value.", databaseError.toException());
            }
        });

        return true;
    }

    public LabList getLabList(){
        return labList;
    }

}
