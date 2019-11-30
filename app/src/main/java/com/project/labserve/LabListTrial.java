package com.project.labserve;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class LabListTrial {

    private ArrayList<LabEntry> labEntries;
    String TAG = "LabList class";

    LabListTrial(){/*
        labEntries = new ArrayList<LabEntry>();
        ArrayList<LabSlot> sun = new ArrayList<LabSlot>();
        ArrayList<LabSlot> mon = new ArrayList<LabSlot>();
        ArrayList<LabSlot> tue = new ArrayList<LabSlot>();
        ArrayList<LabSlot> wed = new ArrayList<LabSlot>();
        ArrayList<LabSlot> thur = new ArrayList<LabSlot>();
        HashMap<String, String> slot = new HashMap<String, String>();
        slot.put("from", "8:00");
        slot.put("to", "9:00");
        sun.add(new LabSlot(slot));
        mon.add(new LabSlot(slot));
        tue.add(new LabSlot(slot));
        wed.add(new LabSlot(slot));
        thur.add(new LabSlot(slot));
        ArrayList<ArrayList<LabSlot>> days = new ArrayList<ArrayList<LabSlot>>();
        days.add(sun);
        days.add(mon);
        days.add(tue);
        days.add(wed);
        days.add(thur);
        LabEntry labEntry = new LabEntry("ESB 1043", "Engineering and Sciences Building", days);
        labEntries.add(labEntry);*/
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
