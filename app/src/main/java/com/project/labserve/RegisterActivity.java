package com.project.labserve;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.labserve.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends Activity {

    private FirebaseAuth mAuth;
    private FirebaseUser firebaseUser;
    private FirebaseDatabase db;
    private DatabaseUser db_user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialize and connect the components
        final EditText nameEditText = findViewById(R.id.nameEditText);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        final CheckBox facultyCheckBox = findViewById(R.id.facultyCheckBox);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button registerButton= findViewById(R.id.registerButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();
        db = FirebaseDatabase.getInstance();
        db_user  = new DatabaseUser(firebaseUser, db);

        if(!isOnline()){
            Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
        }

        // if cancel button clicked, go to Login Activity
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });//Go back to the login page if the user wants to login

        // if register button clicked
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Convert the password and the name, email and password to strings to use them for registeration later
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                String name = nameEditText.getText().toString();

                if(!isOnline()){
                    Toast.makeText(getApplicationContext(),"Please connect to the internet.", Toast.LENGTH_SHORT).show();
                }
                else if(name.isEmpty()){
                    nameEditText.setError("Please enter your name!");
                    nameEditText.requestFocus();
                }
                else if(email.isEmpty()||!email.contains("aus.edu")){ //ONLY accept AUS emails and give error if empty or if email isnt aus
                    emailEditText.setError("Please enter a valid aus email address!");
                    emailEditText.requestFocus();
                }
                else if(password.isEmpty()){ //Check if the user typed in a password
                    emailEditText.setError("Please enter a password!");
                    emailEditText.requestFocus();
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this,"Sign Up Unsuccessful, Please Try Again",Toast.LENGTH_SHORT).show();
                            } //If the sign up was unsuccessful we will ask user to try again
                            else {

                                // set the faculty flag of database to route properly
                                db_user.setFacultyFlag(facultyCheckBox.isChecked());
                                //get current user, i.e. the user registering
                                firebaseUser = mAuth.getCurrentUser();
                                db_user.setNewCurrentUser(firebaseUser); // set to new current user
                                // add the new user to database
                                db_user.addNewUser(nameEditText.getText().toString(), emailEditText.getText().toString());

                                Toast.makeText(RegisterActivity.this,"Account created successfully",Toast.LENGTH_SHORT).show();

                                // switch to login activity
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                intent.putExtra("username", db_user.getUserName()); // pass username to intent
                                intent.putExtra("faculty", facultyCheckBox.isChecked()); // pass faculty check to intent
                                startActivity(intent);
                            } // If the sign up is successful then go back to login page
                        }
                    });
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
