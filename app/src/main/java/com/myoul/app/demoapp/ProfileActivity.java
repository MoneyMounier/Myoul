package com.myoul.app.demoapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.myoul.app.R;

public class ProfileActivity extends AppCompatActivity {

    public static final String LEAD = "com.myoul.app";
    Global global;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(global.User.type == Profile.ProfileType.Driver)
            setContentView(R.layout.activity_driver_profile);
        else if(global.User.type == Profile.ProfileType.Normal)
            setContentView(R.layout.activity_norm_profile);

        global = (Global)this.getApplication();
    }
}
