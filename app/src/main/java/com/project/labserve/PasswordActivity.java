package com.project.labserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.labserve.R;
import com.google.firebase.auth.FirebaseAuth;

//This class is to reset the password of the user if they forgot it.

public class PasswordActivity extends Activity {

    // initialize the buttons

    private Button resetPasswordButton;
    private EditText emailEditText;
    private Button cancelButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        /* now we connect our buttons to the activity*/
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        emailEditText = findViewById(R.id.emailEditText);
        cancelButton = findViewById(R.id.cancelButton);

        mAuth = FirebaseAuth.getInstance(); //Connecting our firebase authenticator

        if(!isOnline()){
            Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        }); //If the user wants to cancel they can get back to the login page if they wish

        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString(); //Convert what the user types into the edit text as string
                if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()||!email.contains("aus.edu")){ //If the email is missing or it's not an aus email don't proceed and give error message
                    emailEditText.setError("Please enter valid aus email");
                    emailEditText.requestFocus();
                }
                else {
                    mAuth.sendPasswordResetEmail(email); //Send password reset email
                    Toast.makeText(PasswordActivity.this,"Please check your email!",Toast.LENGTH_SHORT).show();
                    String link = "http://email.aus.edu";
                    Uri uri = Uri.parse(link);// Creates a Uri which parses the given encoded URI string
                    // create the intent and start it
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(viewIntent);
                    //final Intent intent = new Intent(getApplicationContext(), LoginActivity.class); //Get back to login page
                   // startActivity(intent);
                }
            }
        });
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(!isOnline()){
            Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }
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