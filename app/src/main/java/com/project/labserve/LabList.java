package com.project.labserve;

import android.util.Log;

import java.util.ArrayList;

public class LabList {
    private ArrayList<LabEntry> labEntries;
    String TAG = "LabList class";

    LabList(){
        labEntries = new ArrayList<LabEntry>();
    }

    public int getNoOfLabEntries(){
        return labEntries.size();
    }

    public ArrayList<LabEntry> getAllLabs(){
        return labEntries;
    }

    public void addLab(LabEntry labEntry){
        Log.d(TAG, "Lab entry details: Lab name = " + labEntry.getLabName() + ", Lab building = " + labEntry.getLabBuilding());
        labEntries.add(labEntry);
    }

    public void removeLabAt(int index){
        labEntries.remove(index);
    }

    public LabEntry getLabAt(int index){
        return labEntries.get(index);
    }

    public int getLabIndex(LabEntry labEntry){
        return labEntries.indexOf(labEntry);
    }
}
