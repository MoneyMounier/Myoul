package com.myoul.app.demoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myoul.app.R;

public class Testing extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_norm_profile);//replace with desired layout
    }
}
