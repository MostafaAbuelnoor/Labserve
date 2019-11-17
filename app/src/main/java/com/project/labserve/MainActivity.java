package com.project.labserve;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.example.labserve.R;

public class MainActivity extends Activity {

    private Button findLabbutn, bookLabbutn, checkbutn; // initialize the buttons

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        /* now we connect our buttons to the activity*/
        findLabbutn = findViewById(R.id.findLabbutn);
        bookLabbutn = findViewById(R.id.bookLabbutn);
        checkbutn = findViewById(R.id.checkbutn);

        /* decide what happens when findLabbuttn is clicked */
        findLabbutn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "Finding Labs", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getApplicationContext(), LabFinder.class);
                startActivity(intent);
            }
        });
        bookLabbutn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "Booking Labs", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getApplicationContext(), LabReservation.class);
                startActivity(intent);
            }
        });
        checkbutn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Checking reservations", Toast.LENGTH_LONG).show();
                final Intent intent = new Intent(getApplicationContext(), ReservationList.class);
                startActivity(intent);
            }
        });
    }
}
