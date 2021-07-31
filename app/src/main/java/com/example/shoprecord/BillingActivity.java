package com.example.shoprecord;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class BillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
        getSupportActionBar().hide();
    }
}