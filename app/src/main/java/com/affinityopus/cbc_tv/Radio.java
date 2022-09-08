package com.affinityopus.cbc_tv;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class Radio extends AppCompatActivity {
Float name;
TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        name = getResources().getDisplayMetrics().density;
        textView = findViewById(R.id.textView);
        textView.setText("Screen Type " + name);



    }
}