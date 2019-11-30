package com.project.labserve;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.core.content.ContextCompat;

import com.example.labserve.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class LabActivity extends Activity {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private ImageView labImageView;
    private TextView lab_name, lab_building, lab_availability, daytimings;
    private ListView labListview;
    private Button show_on_map,schedule,reserve;
    private boolean isFaculty;
    String TAG = "LabActivity";

    protected void onCreate(Bundle savedInstanceState) {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        isFaculty = (firebaseUser.getEmail().charAt(1) != '0')? true : false;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab1);
        Intent intent = getIntent();
        final Bundle extras = intent.getExtras();
        labImageView = (ImageView)findViewById(R.id.labImageView);
        lab_name = (TextView) findViewById(R.id.lab_name);
        lab_building = (TextView) findViewById(R.id.lab_building);
        lab_availability = (TextView) findViewById(R.id.lab_availability);
        daytimings = (TextView) findViewById(R.id.daytimings);
        show_on_map = (Button) findViewById(R.id.show_on_map);
        schedule = (Button) findViewById(R.id.schedule);
        reserve = (Button) findViewById(R.id.reserve);
        //labListview = (ListView) findViewById(R.id.labListview);
        //labListview.setOnItemClickListener(this);

        if(isFaculty){
            reserve.setVisibility(View.VISIBLE);
        }
        else{
            reserve.setVisibility(View.GONE);
        }

        if(extras!=null){
            Log.d(TAG, "outside update list");
            ArrayList<String> days = extras.getStringArrayList("allDaysAllSlots");
            UpdateList(days);
            lab_name.setText(extras.getString("labName"));
            if(lab_name.getText().toString().contains("ESB 1043")){
                // from https://stackoverflow.com/questions/12523005/how-set-background-drawable-programmatically-in-android/12523109
                labImageView.setBackground(ContextCompat.getDrawable(this, R.drawable.lab1));
            }
            else if(lab_name.getText().toString().contains("EB2 103")){
                labImageView.setBackground(ContextCompat.getDrawable(this, R.drawable.lab4));
            }
            else if(lab_name.getText().toString().contains("IC 2")){
                labImageView.setBackground(ContextCompat.getDrawable(this, R.drawable.lab2));
            }
            else if(lab_name.getText().toString().contains("ART 103")){
                labImageView.setBackground(ContextCompat.getDrawable(this, R.drawable.lab3));
            }
            lab_building.setText(extras.getString("labBuilding"));
            if(extras.getBoolean("labAvailability")){
                lab_availability.setText("Available");
                // from https://www.android-examples.com/set-textview-text-color-in-android-programmatically/
                lab_availability.setTextColor(Color.parseColor("#00b894"));
            }
            else{
                lab_availability.setText("Unavailable");
                lab_availability.setTextColor(Color.parseColor("#d63031"));
            }

        }

        show_on_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Finding Labs", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("labName", lab_name.getText() ); // pass labName to intent
                startActivity(intent);
            }
        });

        reserve.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent prevIntent = getIntent();
                // Toast.makeText(getApplicationContext(), "Finding Labs", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getApplicationContext(), LabReservation.class);
                Bundle extras = new Bundle();
                extras.putString("labName", lab_name.getText().toString());
                extras.putString("labBuilding", lab_building.getText().toString());
                final Bundle b = prevIntent.getExtras();
                extras.putStringArrayList("allDaysAllSlots", b.getStringArrayList("allDaysAllSlots"));
                intent.putExtras(extras);
                startActivity(intent);
            }
        });

        schedule.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "Finding Labs", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getApplicationContext(), WeeklyScheduleActivity.class);
                Bundle b = new Bundle();
                if(extras!=null){
                    ArrayList<String> toSend = extras.getStringArrayList("allDaysAllSlots");
                    b.putStringArrayList("allDaysAllSlots",toSend);
                    intent.putExtras(b);
                }
                startActivity(intent);
            }
        });
    }

    public void UpdateList(ArrayList<String> days){
        Log.d(TAG, "inside update list");

        //ArrayList<String> days = extras.getStringArrayList("allDaysAllSlots");

        Date currentDate = new Date();
        String today = currentDate.toString().split(" ")[0];
        int index = -1;
        if(today.toLowerCase().contains("sun")){
            index = 0;
        }
        else if(today.toLowerCase().contains("mon")){
            index = 1;
        }
        else if(today.toLowerCase().contains("tue")){
            index = 2;
        }
        else if(today.toLowerCase().contains("wed")){
            index = 3;
        }
        else if(today.toLowerCase().contains("thu")){
            index = 4;
        }
        String [] slots = days.get(index).split(" "); // get Sunday and split the slots
        Log.d(TAG, slots[0]);
        String showtimings = "";
        for(String slot : slots){
            showtimings += "From " + slot.split("-")[0] + " to " + slot.split("-")[1] + "\n";
        }

        daytimings.setText(showtimings);

    }

}
