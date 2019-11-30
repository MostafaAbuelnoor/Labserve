package com.project.labserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import androidx.annotation.NonNull;

import com.example.labserve.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;


public class MainActivity<databaseLabs, labList> extends Activity implements OnItemClickListener {

    // initialize our items

    private Button show_reservation_button;
    private TextView label;
    ListView itemsListView;

    private SharedPreferences prefs;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase db;
    private DatabaseReference db_user;
    private DatabaseReference db_labs;
    private boolean isFaculty;
    String reservation;
    LabList labList;

    String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlist);

        Intent intent = getIntent();
        //displayed = false;
        //isFaculty = intent.getBooleanExtra("faculty");
        /* now we connect our buttons to the activity*/

        label = findViewById(R.id.textView4);
        itemsListView = (ListView) findViewById(R.id.itemsListView);
        itemsListView.setOnItemClickListener(this);
        itemsListView.setVisibility(View.GONE);
        isFaculty = false;
        reservation = "";
        db = FirebaseDatabase.getInstance();

        //Connect the firebase authenticator
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        Log.d(TAG, "Current user: " + firebaseUser.getUid() + " " + firebaseUser.getEmail() + " " + firebaseUser.getDisplayName());
        if(firebaseUser.getEmail().charAt(1) !='0'){
            isFaculty = true;
            db_user = db.getReference("Labserve").child("Users").child("Faculty");
        }
        else{
            db_user = db.getReference("Labserve").child("Users").child("Student");
        }


        labList = new LabList();
        db_labs = db.getReference("Labserve").child("Labs");

        db_labs.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "entered onDataChangedFunction");
                // get all labs: lab1, lab2...
                //LabEntry labEntry = new LabEntry();
                Iterable<DataSnapshot> allLabs = dataSnapshot.getChildren(); // get lab1, lab2, ...
                for(DataSnapshot lab : allLabs){
                    LabEntry labEntry = new LabEntry();
                    Iterable<DataSnapshot> elements = lab.getChildren(); // get labName, labBuilding, ...
                    for(DataSnapshot element : elements){
                        if(element.getKey().contains("labName")){
                            labEntry.setLabName(element.getValue(String.class));
                        }
                        else if(element.getKey().contains("labBuilding")){
                            labEntry.setLabBuilding(element.getValue(String.class));
                        }
                        else if(element.getKey().contains("Sunday")){
                            String temp = element.getValue(String.class);
                            if(temp.contains(":")) {
                                LabSlot slot = new LabSlot(temp);
                                labEntry.setSlot("sunday", slot);
                            }
                        }
                        else if(element.getKey().contains("Monday")){
                            String temp = element.getValue(String.class);
                            if(temp.contains(":")) {
                                LabSlot slot = new LabSlot(temp);
                                labEntry.setSlot("monday", slot);
                            }
                        }
                        else if(element.getKey().contains("Tuesday")){
                            String temp = element.getValue(String.class);
                            if(temp.contains(":")) {
                                LabSlot slot = new LabSlot(temp);
                                labEntry.setSlot("tuesday", slot);
                            }
                        }
                        else if(element.getKey().contains("Wednesday")){
                            String temp = element.getValue(String.class);
                            if(temp.contains(":")) {
                                LabSlot slot = new LabSlot(temp);
                                labEntry.setSlot("wednesday", slot);
                            }
                        }
                        else if(element.getKey().contains("Thursday")){
                            String temp = element.getValue(String.class);
                            if(temp.contains(":")) {
                                LabSlot slot = new LabSlot(temp);
                                labEntry.setSlot("thursday", slot);
                            }
                        }
                    }
                    labList.addLab(labEntry);
                }
                updateDisplay();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                Log.w("Reading Labs from db", "Failed to read value.", databaseError.toException());
            }
        });
        //databaseLabs = new DatabaseLabs(db_labs);

        Log.d(TAG, "calling readLabs from Main");


        if(!isOnline()){
            Toast.makeText(MainActivity.this,"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }


        //String username = intent.getStringExtra("username"); // coming from either previous log in or registration log in


        //new ReadLabs().execute(labList);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        // get default SharedPreferences object
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

    } // end of onCreate

    @Override
    protected void onStart() {
        super.onStart();

    }

    //Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_labserve_menu, menu);
        return true;
    } // end of onCreateOptionsMenu

    // Give every item in the options menu an action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_about:
                startActivity(new Intent(getApplicationContext(), AboutActivity.class));
                return true;
            case R.id.menu_contact:
                startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                return true;
            case R.id.menu_logout:
                Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show();
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class)); //Sign the user out if they press the logout button
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    } // end of onOptionsItemSelected

    @Override
    protected void onResume(){
        super.onResume();
        if(!isOnline()){
            Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }
        updateDisplay();
    } // end of onResume

    protected void onPause() {
        SharedPreferences.Editor editor = prefs.edit();
        for(LabEntry labEntry : labList.getAllLabs()){
            editor.putString("labName"+labList.getLabIndex(labEntry), labEntry.getLabName());
            editor.putString("labBuilding"+labList.getLabIndex(labEntry), labEntry.getLabBuilding());
        }
        editor.putInt("numLabs", labList.getNoOfLabEntries());
        editor.commit();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
    }

    //Checking is we're online or not from https://stackoverflow.com/questions/5474089/how-to-check-currently-internet-connection-is-available-or-not-in-android
    public boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        LabEntry labEntry = labList.getLabAt(position);
        boolean availability = labEntry.labAvailability();

        Bundle extras = new Bundle();
        //Adding key value pairs to this bundle
        //there are quite a lot data types you can store in a bundle
        extras.putString("labName",labEntry.getLabName());
        extras.putString("labBuilding", labEntry.getLabBuilding());
        extras.putBoolean("labAvailability", availability);
        ArrayList<String> toSend = new ArrayList<String>();
        ArrayList<String> days = new ArrayList<String>(Arrays.asList("sun", "mon", "tues", "wed", "thurs"));
        for(String day : days){
            String slotString = labEntry.stringifyAllSlots(day);
            Log.d(TAG,slotString);
            toSend.add(slotString);
        }

        extras.putStringArrayList("allDaysAllSlots", toSend);

        final Intent intent = new Intent(MainActivity.this, LabActivity.class);
        intent.putExtras(extras);

        startActivity(intent);

    }

    public void updateDisplay(){

        if(!isFaculty) {
            show_reservation_button.setVisibility(View.INVISIBLE);
        }
        Log.d(TAG, "updateDisplay function now");
        if ( labList == null || labList.getAllLabs() == null) {
            label.setText("Unable to get Labs");
            return;
        }

        ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

        //if(prefs.getAll().isEmpty()) {

            //ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();
            Log.d(TAG, "Array list created");
            for (LabEntry labEntry : labList.getAllLabs()) {
                HashMap<String, String> map = new HashMap<String, String>();
                Log.d(TAG, "Lab name = " + labEntry.getLabName());
                Log.d(TAG, "Lab building = " + labEntry.getLabBuilding());
                map.put("labName", labEntry.getLabName());
                map.put("labBuilding", labEntry.getLabBuilding());
                data.add(map);
            }

        int resource = R.layout.listview;
        String[] from = {"labName", "labBuilding"};
        int[] to = { R.id.labNo, R.id.buildingName};

        SimpleAdapter adapter =
                new SimpleAdapter(this, data, resource, from, to);
        itemsListView.setAdapter(adapter);
        itemsListView.setVisibility(View.VISIBLE);

    }

}
