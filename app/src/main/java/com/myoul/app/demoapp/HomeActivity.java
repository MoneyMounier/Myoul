package com.myoul.app.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.myoul.app.R;

public class HomeActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        global = (Global)this.getApplication();

        //TODO init the info on home page
    }

    public void deliver(View view){
        Intent intent = new Intent(this, DeliveryActivity.class);
        startActivity(intent);
    }

    public void send(View view){
        Intent intent = new Intent(this, SendActivity.class);
        startActivity(intent);
    }
}
