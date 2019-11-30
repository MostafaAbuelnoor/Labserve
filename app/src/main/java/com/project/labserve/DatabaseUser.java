package com.project.labserve;

import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;
import java.util.HashMap;

public class DatabaseUser {

    private FirebaseUser firebaseUser;
    private FirebaseDatabase db;
    private DatabaseReference db_users;
    private DatabaseReference user;
    private boolean isFaculty;
    String TAG = "DatabaseUser";

    // constructor with argument
    DatabaseUser(FirebaseUser firebaseUser, FirebaseDatabase db, boolean isFaculty){
        this.firebaseUser = firebaseUser; // current user
        this.db = db; // database instance
        this.db_users = db.getReference("Labserve").child("Users"); // set path to Labserve/Users
        // if faculty, set path to Labserve/Users/Faculty
        // else set to Labserve/Users/Student
        this.user = (isFaculty && checkFaculty()) ? db_users.child("Faculty") : db_users.child("Student");

        //readFromDB();
    }

    DatabaseUser(FirebaseUser firebaseUser, FirebaseDatabase db){
        this.firebaseUser = firebaseUser; // current user
        this.db = db; // database instance
        this.db_users = db.getReference("Labserve").child("Users"); // path to Labserve/Users
        this.isFaculty = false; // by default, not faculty
        this.user = db_users.child("Student"); // set database path to Labserve/Users/Student

        //readFromDB();
    }

    // default constructor
    DatabaseUser(){
        this.firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); // current user
        this.db = FirebaseDatabase.getInstance(); // database instance
        this.db_users = db.getReference("Labserve").child("Users"); //set path to Labserve/Users
        this.isFaculty = false; // by default not faculty
        this.user = db_users.child("Student"); // set path to Labserve/Users/Student

        //readFromDB();
    }


    public void setNewCurrentUser(FirebaseUser firebaseUser){
        this.firebaseUser = firebaseUser; //set a new current user
        //toggleread = !toggleread;
        //user.child(this.firebaseUser.getUid()).child("toggleread").setValue(this.toggleread);
        //readFromDB();
    }

    public void setFacultyFlag(boolean isFaculty){
        this.isFaculty = isFaculty; // set faculty flag
        // change flag based on faculty flag
        this.user = (isFaculty) ? db_users.child("Faculty") : db_users.child("Student");
    }

    /*public void readFromDB(){ // read from database Labserve/Users/Faculty or Labserve/Users/Student
        user.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(firebaseUser != null) { // if there is a current user
                    u = dataSnapshot.child(firebaseUser.getUid()).child("username").getValue(String.class); // save username
                    e = dataSnapshot.child(firebaseUser.getUid()).child("email").getValue(String.class); // save email
                    if (isFaculty) { // if faculty member
                        r = dataSnapshot.child(firebaseUser.getUid()).child("reservation").getValue(String.class); // save reservation details
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/

    // add new user
    public void addNewUser(String username, String email){

        //adding a new user to Labserve/Users/Faculty or Labserve/Users/Student

        HashMap<String, String> map = new HashMap<String, String>(); // create key, value pairs
        map.put("username", username);
        map.put("email", email);

        if(isFaculty && checkFaculty()){
            map.put("reservation", "none");
        }
        Log.d(TAG, firebaseUser.getUid());
        user.child(firebaseUser.getUid()).setValue(map); // add a child node under Faculty or Users, give it a key of the current users uID
        // under that node, add { username : some username, email : someemail, (if faculty) reservation : none }

        //user.child(firebaseUser.getUid()).child("toggleread").setValue(toggleread);

        //readFromDB();
    }

    public void setReservation(String lab_no, Date date){
        // to be implemented later
    }

    public boolean checkFaculty(){
        boolean fac = false;

        if(firebaseUser.getEmail().charAt(1)!='0'){
            fac = true;
        }

        return fac;
    }

    /*public String getUserName(){
        //toggleread = !toggleread;
        //user.child(firebaseUser.getUid()).child("toggleread").setValue(toggleread);
        //readFromDB();
        return u;
    }*/

}
