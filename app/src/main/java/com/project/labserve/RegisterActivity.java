package com.project.labserve;

import android.app.Activity;
import android.content.Intent;
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

public class RegisterActivity extends Activity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //Initialize and connect the components
        EditText nameEditText = findViewById(R.id.nameEditText);
        final EditText emailEditText = findViewById(R.id.emailEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        CheckBox facultyCheckBox = findViewById(R.id.facultyCheckBox);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button registerButton= findViewById(R.id.registerButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });//Go back to the login page if the user wants to login

        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //Convert the password and the email to strings to use them for registeration later
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if(email.isEmpty()||!email.contains("aus.edu")){ //ONLY accept AUS emails and give error if empty or if email isnt aus
                    emailEditText.setError("Please enter a valid aus email address");
                    emailEditText.requestFocus();
                }
                else if(password.isEmpty()){ //Check if the user typed in a password
                    emailEditText.setError("Please enter a password");
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
                                Toast.makeText(RegisterActivity.this,"Account created successfully",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                            } // If the sign up is successful then go back to login page
                        }
                    });
                }

            }
        });

    }
}
