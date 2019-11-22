package com.project.labserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.labserve.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends Activity {

    // initialize our items

    private Button findLabbutn, bookLabbutn, checkbutn;
    private TextView usergreeting;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();

        /* now we connect our buttons to the activity*/
        findLabbutn = findViewById(R.id.findLabbutn);
        bookLabbutn = findViewById(R.id.bookLabbutn);
        checkbutn = findViewById(R.id.checkbutn);
        usergreeting = findViewById(R.id.usergreeting);

        if(!isOnline()){
            Toast.makeText(MainActivity.this,"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }

        //Connect the firebase authenticator
        mAuth = FirebaseAuth.getInstance();
        String username = intent.getStringExtra("username"); // coming from either previous log in or registration log in
        usergreeting.setText("Welcome, " + username);


        /* decide what happens when findLabbuttn is clicked */
        findLabbutn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
                if(!isOnline()){
                    Toast.makeText(MainActivity.this,"Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
                else {
                    final Intent intent = new Intent(getApplicationContext(), LabFinder.class);
                    startActivity(intent);
                }
            }
        });
        bookLabbutn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!isOnline()){
                    Toast.makeText(MainActivity.this,"Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
                else{
                final Intent intent = new Intent(getApplicationContext(), LabReservation.class);
                startActivity(intent);
            }}
        });
        checkbutn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if(!isOnline()){
                    Toast.makeText(MainActivity.this,"Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }else{
                final Intent intent = new Intent(getApplicationContext(), ReservationList.class);
                startActivity(intent);
            }}
        });
    }

    //Create the options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_labserve_menu, menu);
        return true;
    }

    // Give every item in the options menu an action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                return true;
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
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(!isOnline()){
            Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }
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
}
