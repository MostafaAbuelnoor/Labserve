package com.project.labserve;

import android.content.Intent;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LabEntry {
    private String labName;
    private String labBuilding;
    private ArrayList<LabSlot> sun, mon, tues, wed, thurs;

    LabEntry(){
        this.labBuilding = "";
        this.labName = "";
        this.sun = new ArrayList<LabSlot>();
        this.mon = new ArrayList<LabSlot>();
        this.tues = new ArrayList<LabSlot>();
        this.wed = new ArrayList<LabSlot>();
        this.thurs = new ArrayList<LabSlot>();
    }

    LabEntry(String labName, String labBuilding, ArrayList<ArrayList<LabSlot>> days ){
        this.labName = labName;
        this.labBuilding = labBuilding;
        this.sun = days.get(0);
        this.mon = days.get(1);
        this.tues = days.get(2);
        this.wed = days.get(3);
        this.thurs = days.get(4);
    }

    public String getLabName(){
        return this.labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public void setLabBuilding(String labBuilding){
        this.labBuilding = labBuilding;
    }

    public String getLabBuilding(){
        return this.labBuilding;
    }

    public ArrayList getTimingsAllDays(){
        ArrayList<ArrayList> allDays = new ArrayList<ArrayList>();
        allDays.add(this.sun);
        allDays.add(this.mon);
        allDays.add(this.tues);
        allDays.add(this.wed);
        allDays.add(this.thurs);
        return allDays;
    }

    public ArrayList<LabSlot> getAllSlotsOn(String day){
        ArrayList<LabSlot> send = new ArrayList<LabSlot>();
        switch (day.toLowerCase()){
            case "sunday":
                send = this.sun;
                break;
            case "monday":
                send = this.mon;
                break;
            case "tuesday":
                send = this.tues;
                break;
            case "wednesday":
                send = this.wed;
                break;
            case "thursday":
                send = this.thurs;
                break;
            default:
                Log.d("Slots Getting", "Slots of " + day + " resulted in error");

        }

        return send;
    }

    public String stringifyAllSlots(String day){
        String result="";
        if(day.toLowerCase().contains("sun")){
            for(LabSlot slot : this.sun){
                if(slot!=null) {
                    result += slot.getFrom() + "-" + slot.getTo() + " ";
                }
            }
        }
        else if(day.toLowerCase().contains("mon")){
            for(LabSlot slot : this.mon){
                if(slot!=null) {
                    result += slot.getFrom() + "-" + slot.getTo() + " ";
                }
            }
        }
        else if(day.toLowerCase().contains("tue")){
            for(LabSlot slot : this.tues){
                if(slot!=null) {
                    result += slot.getFrom() + "-" + slot.getTo() + " ";
                }
            }
        }
        else if(day.toLowerCase().contains("wed")){
            for(LabSlot slot : this.wed){
                if(slot!=null) {
                    result += slot.getFrom() + "-" + slot.getTo() + " ";
                }
            }
        }
        else if(day.toLowerCase().contains("thur")){
            for(LabSlot slot : this.thurs){
                if(slot!=null) {
                    result += slot.getFrom() + "-" + slot.getTo() + " ";
                }
            }
        }

        return result;
    }

    public void setSlot(String day, LabSlot slot){
        switch(day.toLowerCase()){
            case "sunday":
                this.sun.add(slot);
                break;
            case "monday":
                this.mon.add(slot);
                break;
            case "tuesday":
                this.tues.add(slot);
                break;
            case "wednesday":
                this.wed.add(slot);
                break;
            case "thursday":
                this.thurs.add(slot);
                break;
                default:
                    Log.d("Slot Adding", "Adding to " + day + " resulted in error");

        }
    }

    private int convertTime(String s){
        int hr = Integer.parseInt(s.split(":")[0]);
        int min = Integer.parseInt(s.split(":")[1]);
        return hr*60 + min;
    }

    private boolean checkAllSlots(ArrayList<LabSlot> d){
        Date today = new Date();
        String day = today.toString().split(" ")[0];
        int now = convertTime(today.toString().split(" ")[3]);
        boolean isAvailable = false;
        for(LabSlot slot : d ){
            int start = convertTime(slot.getFrom());
            int end = convertTime(slot.getTo());
            if(now > start && now < end){
                isAvailable = true;
                break;
            }
        }
        return isAvailable;
    }

    public boolean labAvailability(){
        Date today = new Date();
        String day = today.toString().split(" ")[0];
        int now = convertTime(today.toString().split(" ")[3]);
        boolean isAvailable = false;
        if(day.toLowerCase().contains("sun")){
            isAvailable = checkAllSlots(this.sun);
        }
        else if(day.toLowerCase().contains("mon")){
            isAvailable=checkAllSlots(this.mon);
        }
        else if(day.toLowerCase().contains("tue")){
            isAvailable = checkAllSlots(this.tues);
        }
        else if(day.toLowerCase().contains("wed")){
            isAvailable = checkAllSlots(this.wed);
        }
        else if(day.toLowerCase().contains("thur")){
            isAvailable = checkAllSlots(this.thurs);
        }

        return isAvailable;

    }

}
