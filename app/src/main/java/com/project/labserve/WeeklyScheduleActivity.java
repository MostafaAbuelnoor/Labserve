package com.project.labserve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.labserve.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class WeeklyScheduleActivity extends Activity {

    private TextView sunSched, monSched, tueSched, wedSched, thurSched;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();

        sunSched = (TextView) findViewById(R.id.sunSched);
        monSched = (TextView) findViewById(R.id.monSched);
        tueSched = (TextView) findViewById(R.id.tueSched);
        wedSched = (TextView) findViewById(R.id.wedSched);
        thurSched = (TextView) findViewById(R.id.thurSched);

        if(extras!=null) {
            ArrayList<String> completeSched = extras.getStringArrayList("allDaysAllSlots");
            ArrayList<String> parsed = new ArrayList<String>();
            for(String day : completeSched){

                String s = processDay(day);
                if(s!=""){
                    parsed.add(s);
                }
                else{
                    parsed.add("No free lab slots on this day");
                }

            }

            sunSched.setText(parsed.get(0));
            monSched.setText(parsed.get(1));
            tueSched.setText(parsed.get(2));
            wedSched.setText(parsed.get(3));
            thurSched.setText(parsed.get(4));
        }
    }

    public String processDay(String day){
        String showtimings = "";

        if(day.equals("")){

        }
        else {
            String[] slots = day.split(" ");

            for (String slot : slots) {
                if (!day.equals("") || !slot.equals("")) {
                    showtimings += "From " + slot.split("-")[0] + " to " + slot.split("-")[1] + "\n";
                }
            }
        }

        return showtimings;
    }
}
