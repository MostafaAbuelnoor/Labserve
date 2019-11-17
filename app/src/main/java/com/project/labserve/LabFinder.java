package com.project.labserve;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.labserve.R;

public class LabFinder extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labfinder);

        TextView show = findViewById(R.id.textView);

    }

}
